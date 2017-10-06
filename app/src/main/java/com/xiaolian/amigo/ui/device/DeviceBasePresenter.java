package com.xiaolian.amigo.ui.device;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.util.Log;

import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.scan.ScanResult;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
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
    private IBleDataManager manager;
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
    // 回调任务（存放结束用水的回调）
    private Callback callback;
    // 订阅设备返回的消息
    private Subscription busSubscriber;
    // 清除subscriptions标志
    private AtomicBoolean clear = new AtomicBoolean(false);
    // 信号量
    private byte[] lock = new byte[0];
    // 标志是否重连
    private boolean reconnect = false;

    public DeviceBasePresenter(IBleDataManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void onConnect(@NonNull String macAddress) {
        // 1、初始化设备消息接收者
        if (null == busSubscriber) {
            busSubscriber = RxBus.getDefault()
                    .toObservable(String.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResult, throwable -> Log.wtf(TAG, "接收设备返回的数据失败", throwable));
        }

        // 2、开启设备状态监控
        addObserver(manager.monitorStatus(macAddress), new BLEObserver<RxBleConnection.RxBleConnectionState>() {

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
                getMvpView().onConnectError();
            }
        }, Schedulers.io());

        // 3、创建共享连接
        connectionObservable = manager
                .prepareConnectionObservable(macAddress, false, disconnectTriggerSubject)
                .compose(bindUntilEvent(PAUSE));

        // 4、连接设备
        addObserver(manager.connect(connectionObservable),
                new BLEObserver<BluetoothGattCharacteristic>() {
                    @Override
                    public void onConnectError() {
                        handleDisConnectError();
                    }

                    @Override
                    public void onExecuteError(Throwable e) {
                        Log.wtf(TAG, "获取特征值失败！", e);
                        getMvpView().onConnectError();
                    }

                    @Override
                    public void onNext(BluetoothGattCharacteristic characteristic) {
                        Log.i(TAG, "设备连接成功！");
                        // 设备连接上存储mac地址供后续读写数据使用
                        currentMacAddress = macAddress;

                        // 蓝牙已启用
                        handleBleAdaptorClose.set(false);

                        // 取notify特征值
                        notifyCharacteristic = characteristic;

                        // 3、开启notify，向蓝牙设备写notify特征值描述，为后续接受蓝牙设备notify通知做铺垫
                        if (manager.getStatus(macAddress) == RxBleConnection.RxBleConnectionState.CONNECTED) {
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
        addObserver(manager.setupNotification(connectionObservable, notifyCharacteristic),
                new BLEObserver<Observable<byte[]>>() {
                    @Override
                    public void onConnectError() {
                        handleDisConnectError();
                    }

                    @Override
                    public void onExecuteError(Throwable e) {
                        Log.wtf(TAG, "开启notify通道失败！", e);
                        getMvpView().onConnectError();
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

        addObserver(manager.writeDescriptor(connectionObservable, descriptor),
                new BLEObserver<byte[]>() {
                    @Override
                    public void onConnectError() {
                        handleDisConnectError();
                    }

                    @Override
                    public void onExecuteError(Throwable e) {
                        Log.wtf(TAG, "写notify特征值描述失败！", e);
                        getMvpView().onConnectError();
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
    public void onWrite(@NonNull String command) {
        if (manager.getStatus(currentMacAddress) != RxBleConnection.RxBleConnectionState.CONNECTED) {
            getMvpView().onStatusError();

            // 清空旧连接
            clearObservers();
            // 此步骤非常重要，不加会造成重连请求掉进黑洞的现象
            resetSubscriptions();

            handleBluetoothAccidentClose();

            // 设置重连成功后的回调
            reconnect = true;
            this.setCallback(() -> onWrite(command));

            safeWait(1000);

            // 触发重连机制
            onConnect(currentMacAddress);
            return;
        }

        byte[] commandBytes = HexBytesUtils.hexStr2Bytes(command);
        addObserver(manager.write(connectionObservable, commandBytes),
                new BLEObserver<byte[]>() {
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
        if (manager.getStatus(currentMacAddress) != RxBleConnection.RxBleConnectionState.CONNECTED) {
            getMvpView().onStatusError();
            return;
        }

        addObserver(manager.notify(connectionObservable, notifyCharacteristic),
                new BLEObserver<byte[]>() {
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
                            RxBus.getDefault().post(result);
//                            handleResult(result);
                        }
                    }
                }, Schedulers.io());

        safeWait(500);
        if (!reconnect) {
            // 重连时不需要再次握手连接
            onWrite(Agreement.getInstance().createConnection());
        } else {
            // 回调下发断连前的指令
            if (null != callback) {
                callback.execute();
            }
        }
    }

    // 统一处理设备连接异常
    private void handleDisConnectError() {
        Log.wtf(TAG, "蓝牙连接已断开！");
        getMvpView().onConnectError();

        if (!clear.getAndSet(true)) {
            disconnectTriggerSubject.onNext(null);
            clearObservers();

            safeWait(5000);
            clear.set(false);
        }

        // 处理蓝牙在用水过程中被用户恶意关闭
        handleBluetoothAccidentClose();
    }

    @Override
    public void onDisConnect() {
        disconnectTriggerSubject.onNext(null);
        if (null != busSubscriber) {
            busSubscriber.unsubscribe();
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
            // ignore
        }
    }

    // 处理用户恶意在使用过程中关闭蓝牙
    private void handleBluetoothAccidentClose() {
        // 判断蓝牙模块是否打开,如果没有提示打开
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        while ((null == adapter || !adapter.isEnabled()) && !handleBleAdaptorClose.getAndSet(true)) {
            getMvpView().getBLEPermission();
            // 10s内不需要给用户重复提示
            safeWait(10000);
            handleBleAdaptorClose.set(false);
        }
    }


    /*************************** 以下为测试用 *****************************/
    @Override
    public void handleResult(String data) {
        String prefix = data.substring(0, 4);
        switch (prefix) {
            case "a801":
                if (data.length() != 28) {
                    Log.e(TAG, "握手失败！data:" + data);
                    getMvpView().onConnectError();
                } else {
                    Log.e(TAG, "握手成功！data:" + data);
                    Agreement.getInstance().InitKey(data, "AAABCDDEEFADABBB");
                    // getMvpView().onConnected();
                }
                break;
            case "a802":
                if (!"a80200aa".equals(data)) {
                    Log.e(TAG, "开阀失败！data:" + data);
                } else {
                    Log.e(TAG, "开阀成功！data:" + data);
                    // getMvpView().onOpen();
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
                    getMvpView().onFinish();
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
