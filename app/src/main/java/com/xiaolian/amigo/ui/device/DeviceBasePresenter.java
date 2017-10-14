package com.xiaolian.amigo.ui.device;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.CountDownTimer;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.util.Log;

import com.polidea.rxandroidble.RxBleConnection;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.xiaolian.amigo.data.enumeration.BleErrorType;
import com.xiaolian.amigo.data.enumeration.Command;
import com.xiaolian.amigo.data.enumeration.OrderStatus;
import com.xiaolian.amigo.data.enumeration.TradeError;
import com.xiaolian.amigo.data.enumeration.TradeStep;
import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.data.manager.intf.ITradeDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.CmdResultReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.ConnectCommandReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.PayReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.UnsettledOrderStatusCheckReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.CmdResultRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.ConnectCommandRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.PayRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.UnsettledOrderStatusCheckRespDTO;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.base.RxBus;
import com.xiaolian.amigo.ui.device.intf.IDevicePresenter;
import com.xiaolian.amigo.ui.device.intf.IDeviceView;
import com.xiaolian.amigo.util.ble.Agreement;
import com.xiaolian.amigo.util.ble.HexBytesUtils;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

import static com.trello.rxlifecycle.android.ActivityEvent.PAUSE;

/**
 * Created by caidong on 2017/9/22.
 */
public abstract class DeviceBasePresenter<V extends IDeviceView> extends BasePresenter<V>
        implements IDevicePresenter<V> {

    private static final String TAG = DeviceBasePresenter.class.getSimpleName();
    private static final String NOTIFY_DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805f9b34fb";
    private IBleDataManager bleDataManager;
    private ITradeDataManager tradeDataManager;
    private IOrderDataManager orderDataManager;
    private ISharedPreferencesHelp sharedPreferencesHelp;
    // 共享连接
    private Observable<RxBleConnection> connectionObservable;
    // notify特征值
    private BluetoothGattCharacteristic notifyCharacteristic;
    private BehaviorSubject<ActivityEvent> lifeCycleSubject = BehaviorSubject.create();
    // 断连触发器
    private PublishSubject<Void> disconnectTriggerSubject = PublishSubject.create();
    // 当前连接的设备
    private String currentMacAddress;
    // 表示是否正在处理蓝牙被主动关闭
    private AtomicBoolean handleBleAdaptorClose = new AtomicBoolean(false);
    // 处理蓝牙关闭时的时间戳(取当前时间往前推20秒)
    private Long handleTime = System.currentTimeMillis() - 20000;
    // 回调任务（存放结束用水的回调）
    private Callback callback;
    // 订阅设备返回的消息
    private Subscription busSubscriber;
    // 处理蓝牙连接错误标识
    private AtomicBoolean handleConnectError = new AtomicBoolean(false);
    // 标志是否重连
    private boolean reconnect = false;
    // 握手连接指令
    private volatile String connectCmd;
    // 开阀指令
    private String openCmd;
    // 关阀指令
    private String closeCmd;
    // 结账指令
    private String precheckCmd;
    // 结账指令
    private String checkoutCmd;
    // 结账页面重连后下一步下发指令
    private String reconnectNextCmd;
    // 重新进入设备用水界面下一步下发指令
    private String reopenNextCmd;
    // 握手连接指令信号量
    private final byte[] connectCmdLock = new byte[0];
    // 预结账标识(预结账状态时，结账后继续正常用水，非预结账状态时，结账后跳转账单详情页)
    private volatile boolean precheckFlag = false;
    // 订单id
    private Long orderId;
    // 订单状态
    private UnsettledOrderStatusCheckRespDTO orderStatus;
    // 订单状态信号量
    private final byte[] orderStatusLock = new byte[0];
    // 页面当前跳转跳转步骤 1-支付页， 2-结账页
    private TradeStep step;
    // 纯结账标识，直接连接跳转至第二页结账
    private boolean purelyCheckoutFlag = false;
    // 正在连接标识
    private boolean connecting = true;
    // 连接计时器
    private CountDownTimer timer;

    public DeviceBasePresenter(IBleDataManager bleDataManager, ITradeDataManager tradeDataManager, IOrderDataManager orderDataManager, ISharedPreferencesHelp sharedPreferencesHelp) {
        super();
        this.bleDataManager = bleDataManager;
        this.tradeDataManager = tradeDataManager;
        this.orderDataManager = orderDataManager;
        this.sharedPreferencesHelp = sharedPreferencesHelp;
    }

    @Override
    public void resetContext() {
        currentMacAddress = null;
        reconnect = false;
        connectCmd = null;
        openCmd = null;
        closeCmd = null;
        precheckCmd = null;
        checkoutCmd = null;
        reconnectNextCmd = null;
        reopenNextCmd = null;
        precheckFlag = false;
        orderId = null;
        orderStatus = null;
        purelyCheckoutFlag = false;
        step = null;
        purelyCheckoutFlag = false;
    }

    @Override
    public void onConnect(@NonNull String macAddress) {

        // 重置正在连接标识
        connecting = true;

        // 启动30s倒计时
        timer = new CountDownTimer(15 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!connecting) {
                    this.cancel();
                    Log.i(TAG, "设备已连接上，取消定时器。");
                }
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "设备连接超时。");

                // 关闭蓝牙连接
                closeBleConnecttion();

                getMvpView().post(() -> getMvpView().onError(TradeError.CONNECT_ERROR_5));
            }
        };
        Log.i(TAG, "启动30s定时器......");
        timer.start();

        // 设备连接上存储mac地址供后续读写数据使用
        currentMacAddress = macAddress;

        // 1、网络请求获取握手指令
        if (null == connectCmd) {
            getConnectCommand(macAddress);
        }
        // 检测当前用户对该设备订单使用状态
        checkOrderStatus(macAddress);

        // 2、初始化设备消息接收者
        if (null == busSubscriber) {
            busSubscriber = RxBus.getDefault()
                    .toObservable(ApiResult.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResult, throwable -> Log.wtf(TAG, "接收设备返回的数据失败", throwable));
        }

        // 3、开启设备状态监控
        addObserver(bleDataManager.monitorStatus(macAddress), new BleObserver<RxBleConnection.RxBleConnectionState>() {

            @Override
            public void onNext(RxBleConnection.RxBleConnectionState state) {
                String threadName = Thread.currentThread().getName();
                Log.i(TAG, threadName + ":设备状态发生变化：" + state.toString());
            }

            @Override
            public void onConnectError() {
                handleDisConnectError();
            }

            @Override
            public void onExecuteError(Throwable e) {
                Log.wtf(TAG, "监控设备状态失败！", e);
                getMvpView().onError(TradeError.CONNECT_ERROR_1);
            }
        }, Schedulers.io());

        // 4、创建共享连接
        connectionObservable = bleDataManager
                .prepareConnectionObservable(macAddress, false, disconnectTriggerSubject)
                .compose(bindUntilEvent(PAUSE));

        // 5、连接设备
        addObserver(bleDataManager.connect(connectionObservable),
                new BleObserver<BluetoothGattCharacteristic>() {
                    @Override
                    public void onConnectError() {
                        handleDisConnectError();
                    }

                    @Override
                    public void onExecuteError(Throwable e) {
                        Log.wtf(TAG, "获取特征值失败！", e);
                        getMvpView().onError(TradeError.CONNECT_ERROR_1);
                    }

                    @Override
                    public void onNext(BluetoothGattCharacteristic characteristic) {
                        Log.i(TAG, "设备连接成功！");

                        // 蓝牙已启用
                        handleBleAdaptorClose.set(false);

                        // 取notify特征值
                        notifyCharacteristic = characteristic;

                        // 6、开启notify，向蓝牙设备写notify特征值描述，为后续接受蓝牙设备notify通知做铺垫
                        if (bleDataManager.getStatus(macAddress) == RxBleConnection.RxBleConnectionState.CONNECTED) {
                            Log.i(TAG, "准备开启notify通道");
                            enableNotify();
                        } else {
                            getMvpView().onStatusError();
                        }

                    }
                }, Schedulers.io());
    }

    // 开启notify通道
    private void enableNotify() {
        addObserver(bleDataManager.setupNotification(connectionObservable, notifyCharacteristic),
                new BleObserver<Observable<byte[]>>() {
                    @Override
                    public void onConnectError() {
                        handleDisConnectError();
                    }

                    @Override
                    public void onExecuteError(Throwable e) {
                        Log.wtf(TAG, "开启notify通道失败！", e);
                        getMvpView().onError(TradeError.CONNECT_ERROR_1);
                    }

                    @Override
                    public void onNext(Observable<byte[]> observable) {
                        Log.i(TAG, "开启notify通道成功！");
                        Log.i(TAG, "准备开始写notify特征值描述");
                        writeNotifyCharacteristicDesc();
                    }
                }, Schedulers.io());
    }

    // 写notify特征值描述
    private void writeNotifyCharacteristicDesc() {
        BluetoothGattDescriptor descriptor = notifyCharacteristic.getDescriptor(UUID.fromString(NOTIFY_DESCRIPTOR_UUID));
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

        addObserver(bleDataManager.writeDescriptor(connectionObservable, descriptor),
                new BleObserver<byte[]>() {
                    @Override
                    public void onConnectError() {
                        handleDisConnectError();
                    }

                    @Override
                    public void onExecuteError(Throwable e) {
                        Log.wtf(TAG, "写notify特征值描述失败！", e);
                        getMvpView().onError(TradeError.CONNECT_ERROR_1);
                    }

                    @Override
                    public void onNext(byte[] descriptor) {
                        Log.i(TAG, "写notify特征值描述成功！");

                        // 注册notify通道接收数据
                        registerNotify();
                    }
                }, Schedulers.io());
    }

    @Override
    public void onReconnect(@NonNull String macAddress) {
        // 重置蓝牙连接
        resetBleConnection();

        // 设置重连标识
        reconnect = true;

        safeWait(1000);

        // 触发重连机制
        onConnect(macAddress);
    }

    @Override
    public void onWrite(@NonNull String command) {
        if (bleDataManager.getStatus(currentMacAddress) != RxBleConnection.RxBleConnectionState.CONNECTED) {
            getMvpView().onError(TradeError.CONNECT_ERROR_1);
            return;
        }

        byte[] commandBytes = HexBytesUtils.hexStr2Bytes(command);
        addObserver(bleDataManager.write(connectionObservable, commandBytes),
                new BleObserver<byte[]>() {
                    @Override
                    public void onConnectError() {
                        handleDisConnectError();
                    }

                    @Override
                    public void onExecuteError(Throwable e) {
                        Log.wtf(TAG, "发送指令失败！command:" + command, e);
                        getMvpView().onWriteError();
                    }

                    @Override
                    public void onNext(byte[] data) {
                        Log.i(TAG, "发送指令成功！command:" + HexBytesUtils.bytesToHexString(data));
                    }
                }, Schedulers.io());
    }

    @Override
    public void registerNotify() {
        if (bleDataManager.getStatus(currentMacAddress) != RxBleConnection.RxBleConnectionState.CONNECTED) {
            getMvpView().onError(TradeError.CONNECT_ERROR_1);
            return;
        }

        addObserver(bleDataManager.notify(connectionObservable, notifyCharacteristic),
                new BleObserver<byte[]>() {
                    @Override
                    public void onConnectError() {
                        handleDisConnectError();
                    }

                    @Override
                    public void onExecuteError(Throwable e) {
                        Log.wtf(TAG, "接收数据失败！", e);
                        getMvpView().onNotifyError();
                    }

                    @Override
                    public void onNext(byte[] data) {
                        if (null != data) {
                            String result = HexBytesUtils.bytesToHexString(data);
                            Log.i(TAG, "接收数据成功！data:" + result);
                            processCommandResult(result);
                        }
                    }
                }, Schedulers.io());

        connecting = false;
        handleConnectError.set(false);

        if (reconnect) {
            Log.i(TAG, "当前为重连状态");
            if (null == getStep()) {
                Log.i(TAG, "首次连接就连接不上，需要重新下发握手指令:" + connectCmd);
                waitConnectCmdResult();
                onWrite(connectCmd);
                reconnect = false; // 重置重连标志
            } else if (TradeStep.PAY == getStep()) { // 支付页面重连
                // 重新连接成功时不需要再次握手
                Log.i(TAG, "当前为支付页面重连，不需要重新下发握手指令，只需要页面显示重连成功。");
                getMvpView().post(() -> getMvpView().onReconnectSuccess());
                reconnect = false; // 重置重连标志
            } else { // 结算页面重连
                Log.i(TAG, "当前为结算页面重连");
                waitOrderCheckResult();
                if (null == orderStatus) {
                    Log.wtf(TAG, "查不到对应的未结账订单，不应该发生此种状况！！！");
                } else {
                    if (OrderStatus.getOrderStatus(orderStatus.getStatus()) == OrderStatus.FINISHED) { // 订单已结单
                        Log.i(TAG, "重连后发现订单已被结算，跳转至订单详情页。orderId:" + orderStatus.getOrderId());
                        getMvpView().onFinish(orderStatus.getOrderId()); // 跳转订单详情页
                    } else { // 未结单
                        // 重连状态下继续下发握手指令
                        // 1、如果设备没有长按结束用水按钮，握手会失败，但连接不会被设备中断，继续下发关阀指令走结账流程即可
                        // 2、如果设备已被长按结束用水按钮，握手会成功，此时需要走预结账->结账流程
                        Log.i(TAG, String.format("重连后发现订单仍未被结算，继续下发握手指令。command:%s, orderId: %s", connectCmd, orderStatus.getOrderId()));
                        onWrite(connectCmd);
                    }
                }
            }
        } else {
            Log.i(TAG, "当前为正常连接状态");

            // 查询订单状态
            waitOrderCheckResult();

            // String savedConnectCmd = sharedPreferencesHelp.getConnectCmd(currentMacAddress);
            // Log.i(TAG, "获取已保存的握手指令：" + savedConnectCmd);
            if (null != orderStatus && null != orderStatus.getStatus() && OrderStatus.getOrderStatus(orderStatus.getStatus()) == OrderStatus.USING) {  // 有订单未计算拿上次连接的握手指令（这里待验证是否有影响）
                Log.i(TAG, String.format("正常连接发现有订单未被结算，继续下发握手指令。command: %s, orderId:%s", connectCmd, orderStatus.getOrderId()));
                orderId = orderStatus.getOrderId();
                onWrite(connectCmd);
                purelyCheckoutFlag = true;
            } else {
                // 握手连接
                Log.i(TAG, "正常连接成功，下发握手指令。command:" + connectCmd);
                waitConnectCmdResult();
                onWrite(connectCmd);
            }
        }
    }

    // 等待握手指令到达
    private void waitConnectCmdResult() {
        if (null == connectCmd) {
            synchronized (connectCmdLock) {
                if (null == connectCmd) {
                    try {
                        Log.i(TAG, "物理连接成功，等待握手指令到达");
                        connectCmdLock.wait();
                        Log.i(TAG, "物理连接成功，握手指令成功到达");
                    } catch (InterruptedException e) {
                        Log.wtf(TAG, e);
                    }
                }
            }
        }
    }

    private void waitOrderCheckResult() {
        if (null == orderStatus) {
            synchronized (orderStatusLock) {
                if (null == orderStatus) {
                    try {
                        Log.i(TAG, "物理连接成功，等待订单状态到达");
                        orderStatusLock.wait();
                        Log.i(TAG, "物理连接成功，订单状态成功到达");
                    } catch (InterruptedException e) {
                        Log.wtf(TAG, e);
                    }
                }
            }
        }
    }

    // 统一处理设备连接异常
    private void handleDisConnectError() {
        Log.wtf(TAG, "蓝牙连接已断开！");

        if (!handleConnectError.getAndSet(true)) {
            // 关闭蓝牙连接
            closeBleConnecttion();

            // 跳转至连接失败页面
            getMvpView().post(() -> getMvpView().onError(TradeError.CONNECT_ERROR_1));
        }

        // 处理蓝牙在用水过程中被用户恶意关闭
        // handleBluetoothAccidentClose();
    }

    @Override
    public void onDisConnect() {
        disconnectTriggerSubject.onNext(null);
        if (null != busSubscriber) {
            busSubscriber.unsubscribe();
        }
        if (null != timer) {
            timer.cancel();
        }
    }

    @NonNull
    @CheckResult
    private <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifeCycleSubject, event);
    }

    public interface Callback {
        void execute();
    }

    @Override
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    // 安全等待时长间隔
    private void safeWait(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            Log.wtf(TAG, e);
        }
    }

    // 处理用户恶意在使用过程中关闭蓝牙
    private void handleBluetoothAccidentClose() {
        // 判断蓝牙模块是否打开,如果没有提示打开
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Log.i(TAG, Thread.currentThread().getName() + " 状态改变前，handleBleAdaptorClose:" + handleBleAdaptorClose.get());
        while ((null == adapter || !adapter.isEnabled()) && !handleBleAdaptorClose.getAndSet(true)) {
            // 20s内不需要给用户重复提示
            long now = System.currentTimeMillis();
            if (now - handleTime > 20000) {
                Log.i(TAG, Thread.currentThread().getName() + " 状态改变后，handleBleAdaptorClose:" + handleBleAdaptorClose.get());
                getMvpView().getBlePermission();
                handleTime = now;
            }
            handleBleAdaptorClose.set(false);
        }
    }

    // 网络请求获取握手连接指令
    private void getConnectCommand(String macAddress) {
        ConnectCommandReqDTO reqDTO = new ConnectCommandReqDTO();
        reqDTO.setMacAddress(macAddress);
        addObserver(tradeDataManager.getConnectCommand(reqDTO), new NetworkObserver<ApiResult<ConnectCommandRespDTO>>() {
            @Override
            public void onReady(ApiResult<ConnectCommandRespDTO> result) {
                if (null == result.getError()) {
                    synchronized (connectCmdLock) {
                        connectCmd = result.getData().getConnectCmd();
                        Log.i(TAG, "获取握手指令成功。command:" + connectCmd);
                        connectCmdLock.notifyAll();
                    }
                } else {
                    Log.wtf(TAG, "获取握手失败");
                    getMvpView().post(() -> getMvpView().onError(TradeError.SYSTEM_ERROR));
                }
            }
        }, Schedulers.io());
    }

    // 校验用户对指定设备使用订单的结算状态
    private void checkOrderStatus(String macAddress) {
        UnsettledOrderStatusCheckReqDTO reqDTO = new UnsettledOrderStatusCheckReqDTO();
        reqDTO.setMacAddress(macAddress);
        addObserver(orderDataManager.checkOrderStatus(reqDTO), new NetworkObserver<ApiResult<UnsettledOrderStatusCheckRespDTO>>() {
            @Override
            public void onReady(ApiResult<UnsettledOrderStatusCheckRespDTO> result) {
                if (null == result.getError()) {
                    synchronized (orderStatusLock) {
                        orderStatus = result.getData();
                        Log.i(TAG, "获取订单状态成功。orderStatus:" + orderStatus);
                        orderStatusLock.notifyAll();
                    }
                } else {
                    Log.wtf(TAG, "获取握手失败");
                    getMvpView().post(() -> getMvpView().onError(TradeError.SYSTEM_ERROR));
                }
            }
        }, Schedulers.io());
    }

    // 网络请求处理设备响应结果
    private void processCommandResult(String result) {
        CmdResultReqDTO reqDTO = new CmdResultReqDTO();
        reqDTO.setData(result);
        reqDTO.setMacAddress(currentMacAddress);
        addObserver(tradeDataManager.processCmdResult(reqDTO), new NetworkObserver<ApiResult<CmdResultRespDTO>>(false) {
            @Override
            public void onReady(ApiResult<CmdResultRespDTO> result) {
                Log.i(TAG, "通知主线程更新数据。" + result.getData());
                RxBus.getDefault().post(result);
            }
        }, Schedulers.io());
    }

    @Override
    public void handleResult(ApiResult<CmdResultRespDTO> result) {
        Log.i(TAG, "主线程开始处理指令响应结果");
        if (null == result.getError()) {
            // 下一步执行指令
            String nextCommand = result.getData().getNextCommand();
            switch (Command.getCommand(result.getData().getSrcCommandType())) {
                case CONNECT:
                    // 如果用户本人在三小时之内再次连接该设备，需要进入第二步账单结算页面
                    if (null != orderStatus && null != orderStatus.getStatus() && OrderStatus.getOrderStatus(orderStatus.getStatus()) == OrderStatus.USING) {
                        // 记录预结账指令，此时阀门已经被长按关闭，但是订单没有被其它用户带回
                        Log.i(TAG, "用户在该设备上存在未结账订单，直接跳转至结算页面，获取到预结账指令。command:" + nextCommand);
                        reopenNextCmd = nextCommand;
                        getMvpView().onConnectSuccess(TradeStep.SETTLE, orderStatus);

                        connectCmd = sharedPreferencesHelp.getConnectCmd(currentMacAddress);
                        closeCmd = sharedPreferencesHelp.getCloseCmd(currentMacAddress);

                        return;
                    }
                    // 如果存在未结账订单，需要先结算旧账单
                    if (null != result.getData().getNextCommand()) {
                        precheckCmd = nextCommand;
                        if (!reconnect) { // 正常流程
                            // 下发预结账指令
                            Log.i(TAG, "正常流程，设备上存在未结账订单，获取到预结账指令。command:" + nextCommand);
                            onWrite(precheckCmd);
                        } else {
                            Log.i(TAG, "用户在结算页面重新连接成功，设备上存在未结账订单，获取到预结账指令。command:" + nextCommand);
                            getMvpView().onReconnectSuccess();
                            reconnectNextCmd = precheckCmd;
                        }
                    } else {
                        // 握手连接成功，进入正常用水流程
                        Log.i(TAG, "正常流程，设备上不存在未结账订单。");
                        getMvpView().onConnectSuccess(TradeStep.PAY);
                    }
                    break;
                case OPEN_VALVE:
                    Log.i(TAG, "正常流程，获取到关阀指令。command:" + nextCommand);
                    closeCmd = nextCommand;
                    sharedPreferencesHelp.setCloseCmd(currentMacAddress, closeCmd);
                    getMvpView().onOpen();
                    break;
                case CLOSE_VALVE:
                    Log.i(TAG, "正常流程，获取到结账指令。command:" + nextCommand);
                    checkoutCmd = nextCommand;
                    // 下发结账指令
                    onWrite(checkoutCmd);
                    break;
                case CHECK_OUT:
                    if (purelyCheckoutFlag) {
                        // 纯结账操作，直接跳转至第二步结账，结账完毕跳转账单详情页面
                        Log.i(TAG, "结账完成");
                        getMvpView().onFinish(orderId);
                    } else if (precheckFlag) {
                        // 当前为预结账状态，结账完毕后关闭更改标识
                        precheckFlag = false;
                        if (reconnect) {
                            // 重连状态下，走预结账->结账流程时，是本人主动结账，跳转账单详情页
                            Log.i(TAG, "结账完成");
                            getMvpView().onFinish(orderId);
                        } else {
                            // 非重连状态下，是主动帮别人结账，此时用户还未进入用水流程，需要再次握手，否则会报设备使用次数不对
                            Log.i(TAG, "结账完成,当前为主动帮别人结账，正常流程，继续下发握手指令");
                            onWrite(connectCmd);
                        }
                    } else {
                        // 当前为非预结账状态，结账完毕后跳转账单详情页
                        Log.i(TAG, "结账完成");
                        getMvpView().onFinish(orderId);
                    }
                    break;
                case PRE_CHECK:
                    Log.i(TAG, "正常流程，获取到结账指令。command:" + nextCommand);
                    checkoutCmd = nextCommand;
                    // 标识当前状态为预结账状态
                    precheckFlag = true;
                    // 下发结账指令
                    onWrite(checkoutCmd);
                    break;
            }
        } else {
            Log.e(TAG, String.format("处理异常返回结果。code:{}, msg:{}", result.getError().getCode(), result.getError().getDisplayMessage()));
            if (result.getError().getCode() == BleErrorType.BLE_DEVICE_BUSY.getCode()) {
                Log.e(TAG, "设备正在被使用");
                // 如果是重连状态下，此时可以直接下发关阀指令
                if (reconnect) {
                    // 显示重连成功
                    getMvpView().onReconnectSuccess();
                    reconnectNextCmd = closeCmd;
                } else { // 首次连接
                    if (null != orderStatus && null != orderStatus.getStatus() && OrderStatus.getOrderStatus(orderStatus.getStatus()) == OrderStatus.USING) { // 用户主动上来结账
                        getMvpView().onConnectSuccess(TradeStep.SETTLE, orderStatus);
                        reopenNextCmd = sharedPreferencesHelp.getCloseCmd(currentMacAddress);

                        connectCmd = sharedPreferencesHelp.getConnectCmd(currentMacAddress);
                        closeCmd = reopenNextCmd;
                    } else {
                        // 提示用户设备已被其它用户使用
                        closeBleConnecttion();
                        getMvpView().onError(TradeError.DEVICE_BUSY);
                    }
                }
            }
            Integer cmdType = result.getError().getBleCmdType();
            if (null != cmdType) {
                // 确认支付时异常
                if (Command.OPEN_VALVE == Command.getCommand(cmdType)) {
                    Log.wtf(TAG, "设备开阀异常");
                    closeBleConnecttion();
                    getMvpView().onError(TradeError.DEVICE_BROKEN_2);
                } else if (Command.CLOSE_VALVE == Command.getCommand(cmdType) || Command.PRE_CHECK == Command.getCommand(cmdType) || Command.CHECK_OUT == Command.getCommand(cmdType)) {
                    Log.wtf(TAG, "订单结算异常");
                    closeBleConnecttion();
                    // 结算时异常
                    getMvpView().onError(TradeError.DEVICE_BROKEN_1);
                }
            } else {
                // 系统异常
                Log.wtf(TAG, String.format("服务器后台出错, errorCode:%s, errorMsg:%s", result.getError().getCode(), result.getError().getDisplayMessage()));
                closeBleConnecttion();
                getMvpView().onError(TradeError.SYSTEM_ERROR);
            }
        }
    }

    @Override
    public void onPay(int method, Double prepay, Long bonusId) {
        // 校验网络
        if (!getMvpView().isNetworkAvailable()) {
            getMvpView().onError(TradeError.CONNECT_ERROR_3);
        }

        PayReqDTO reqDTO = new PayReqDTO();
        reqDTO.setMacAddress(currentMacAddress);
        reqDTO.setBonusId(bonusId);
        reqDTO.setPrepay(prepay);
        reqDTO.setMethod(method);

        addObserver(tradeDataManager.pay(reqDTO), new NetworkObserver<ApiResult<PayRespDTO>>() {
            @Override
            public void onReady(ApiResult<PayRespDTO> result) {
                if (null == result.getError()) {
                    // 初始化订单id
                    orderId = result.getData().getOrderId();

                    Log.i(TAG, "支付创建订单成功。orderId：" + orderId);

                    // 向设备下发开阀指令
                    openCmd = result.getData().getOpenValveCommand();
                    Log.i(TAG, "开始下发开阀指令。command：" + openCmd);
                    onWrite(openCmd);
                } else {
                    Log.wtf(TAG, "支付创建订单失败。");
                    getMvpView().post(() -> getMvpView().onError(TradeError.SYSTEM_ERROR));
                }
            }
        }, Schedulers.io());
    }

    @Override
    public void onClose() {
        Log.i(TAG, "开始关阀");
        // 校验网络
        if (!getMvpView().isNetworkAvailable()) {
            Log.wtf(TAG, "网络不可用");
            getMvpView().onError(TradeError.CONNECT_ERROR_3);
        }

        if (reconnect) {
            // 重连状态下指令有两种 1 - 关阀 2 - 预结账
            onWrite(reconnectNextCmd);
        } else {
            if (purelyCheckoutFlag) { // 直接跳转至第二步结算
                if (null == reopenNextCmd) { // 用户卸载掉app时取回的关阀指令为空
                    closeBleConnecttion();
                    getMvpView().onError(TradeError.CONNECT_ERROR_2);
                } else {
                    onWrite(reopenNextCmd); // 正常下发
                }
            } else {
                // 向设备下发关阀指令
                onWrite(closeCmd);
            }
        }
    }

    @Override
    public void closeBleConnecttion() {
        // 不再接收数据
        disconnectTriggerSubject.onNext(null);

        // 清空连接观察者
        clearObservers();
    }

    @Override
    public void resetBleConnection() {
        // 清空旧连接
        clearObservers();

        // 此步骤非常重要，不加会造成重连请求掉进黑洞的现象
        resetSubscriptions();
    }

    @Override
    public TradeStep getStep() {
        return step;
    }

    @Override
    public void setStep(TradeStep step) {
        this.step = step;
    }

    /*************************** 以下为测试用 *****************************/
    @Override
    public void handleResult(String data) {
        String prefix = data.substring(0, 4);
        switch (prefix) {
            case "a801":
                if (data.length() != 28) {
                    Log.e(TAG, "握手失败！data:" + data);
                    getMvpView().onError(TradeError.CONNECT_ERROR_1);
                } else {
                    Log.e(TAG, "握手成功！data:" + data);
                    Agreement.getInstance().InitKey(data, "AAABCDDEEFADABBB");
                    // getMvpView().onConnected();
                    getMvpView().onConnectSuccess(TradeStep.PAY);
                }
                break;
            case "a802":
                if (!"a80200aa".equals(data)) {
                    Log.e(TAG, "开阀失败！data:" + data);
                } else {
                    Log.e(TAG, "开阀成功！data:" + data);
                    getMvpView().onOpen();
                    // 关闭连接
                    // clearObservers(true);
                }
                break;
            case "a804":
                if (data.length() == 40) {
                    Log.e(TAG, "关阀成功：" + data);
                    BigDecimal bal = Agreement.getInstance().getYE(data);
                    Log.e(TAG, "结账金额：" + bal);
                    onWrite(Agreement.getInstance().Checkout("12345678"));
                } else {
                    Log.e(TAG, "关阀失败：" + data);
                }
                break;
            case "a807":
                if (data.length() == 24) {
                    Log.e(TAG, "结账成功：" + data);
                    getMvpView().onFinish(orderId);
                } else {
                    Log.e(TAG, "结账失败：" + data);
                }
                break;
            case "a808":
                if (data.length() == 24) {
                    Log.e(TAG, "预结账成功：" + data);
                    BigDecimal bal = Agreement.getInstance().getYE(data);
                    Log.e(TAG, "预结账金额：" + bal);
                    onWrite(Agreement.getInstance().Checkout("12345678"));
                } else {
                    Log.e(TAG, "预结账失败：" + data);
                }
                break;
            case "a80a":
                if (data.equalsIgnoreCase("a80a00b2")) {
                    Log.e(TAG, "初始化key成功：" + data);
                    // getMvpView().onKeyInitSuccess();
                } else {
                    Log.e(TAG, "初始化key失败：" + data);
                }
                break;
        }
    }

}
