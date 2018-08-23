package com.xiaolian.amigo.ui.device;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v4.util.ObjectsCompat;
import android.text.TextUtils;

import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.xiaolian.amigo.data.base.TimeHolder;
import com.xiaolian.amigo.data.enumeration.AgreementVersion;
import com.xiaolian.amigo.data.enumeration.BizError;
import com.xiaolian.amigo.data.enumeration.BleErrorType;
import com.xiaolian.amigo.data.enumeration.Command;
import com.xiaolian.amigo.data.enumeration.ConnectErrorType;
import com.xiaolian.amigo.data.enumeration.DisplayErrorType;
import com.xiaolian.amigo.data.enumeration.OrderStatus;
import com.xiaolian.amigo.data.enumeration.TradeError;
import com.xiaolian.amigo.data.enumeration.TradeStep;
import com.xiaolian.amigo.data.manager.BleDataManager;
import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
import com.xiaolian.amigo.data.manager.intf.IDeviceDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.connecterror.DeviceConnectErrorReqDTO;
import com.xiaolian.amigo.data.network.model.device.Supplier;
import com.xiaolian.amigo.data.network.model.order.UnsettledOrderStatusCheckReqDTO;
import com.xiaolian.amigo.data.network.model.order.UnsettledOrderStatusCheckRespDTO;
import com.xiaolian.amigo.data.network.model.trade.CmdResultReqDTO;
import com.xiaolian.amigo.data.network.model.trade.CmdResultRespDTO;
import com.xiaolian.amigo.data.network.model.trade.ConnectCommandReqDTO;
import com.xiaolian.amigo.data.network.model.trade.ConnectCommandRespDTO;
import com.xiaolian.amigo.data.network.model.trade.PayReqDTO;
import com.xiaolian.amigo.data.network.model.trade.PayRespDTO;
import com.xiaolian.amigo.data.vo.DeviceCategory;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.base.RxBus;
import com.xiaolian.amigo.ui.device.intf.IDevicePresenter;
import com.xiaolian.amigo.ui.device.intf.IDeviceView;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;
import com.xiaolian.amigo.util.ble.Agreement;
import com.xiaolian.amigo.util.ble.HexBytesUtils;
import com.xiaolian.blelib.BluetoothConstants;
import com.xiaolian.blelib.ScanRecord;
import com.xiaolian.blelib.scan.BluetoothScanResponse;
import com.xiaolian.blelib.scan.BluetoothScanResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

/**
 * 设备BasePresenter
 *
 * @author caidong
 * @date 17/9/22
 */
public abstract class DeviceBasePresenter<V extends IDeviceView> extends BasePresenter<V>
        implements IDevicePresenter<V> {

    private static final String TAG = DeviceBasePresenter.class.getSimpleName();
    private IBleDataManager bleDataManager;
    private IDeviceDataManager deviceDataManager;
    private BehaviorSubject<ActivityEvent> lifeCycleSubject = BehaviorSubject.create();
    // 断连触发器
    private PublishSubject<Void> disconnectTriggerSubject = PublishSubject.create();
    // 当前连接的设备
    private String currentMacAddress;
    // 设备编号 macAddress后六位 用于和服务器交互
    private String deviceNo;
    // 表示蓝牙是否连接 在连接上后设置为true 只有在handleDisConnectError 里才能设置为false
    private AtomicBoolean handleBleClose = new AtomicBoolean(false);
    // 订阅设备返回的消息
    private Subscription busSubscriber;
    // 标志是否重连
    private volatile boolean reconnect = false;
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
    private volatile long orderId;
    // 订单状态，这个订单表明是自己的订单
    private UnsettledOrderStatusCheckRespDTO orderStatus;
    // 订单状态信号量
    private final byte[] orderStatusLock = new byte[0];
    // 页面当前跳转跳转步骤 1-支付页， 2-结账页
    private TradeStep step;
    // 纯结账标识，直接连接跳转至第二页结账
    private volatile boolean purelyCheckoutFlag = false;
    // 正在连接标识
    private volatile boolean connecting = true;
    // 连接计时器
    private CountDownTimer timer;
    // 从首页点击设备用水跳转标识
    private volatile boolean homePageJump = true;
    // 校验订单出现错误
    private volatile boolean checkOrderErrorFlag = false;
    // 结束标识
    private volatile boolean closeFlag = false;
    // 故障设备标志
    private volatile boolean brokenFlag = false;
    // 供应商
    private Supplier supplier;
    // 页面关闭触发器
    private PublishSubject<Void> closeTriggerSubject = PublishSubject.create();
    // 扫描方式
    private int scanType = BluetoothConstants.SCAN_TYPE_BLE;



    DeviceBasePresenter(IBleDataManager bleDataManager, IDeviceDataManager deviceDataManager) {
        super();
        this.bleDataManager = bleDataManager;
        this.deviceDataManager = deviceDataManager;
        this.scanType = deviceDataManager.getScanType();
    }

    @Override
    public void setSupplierId(Long supplierId) {
        Log.i(TAG, "设置supplierId" + supplierId);
        List<DeviceCategory> deviceCategories = deviceDataManager.getDeviceCategory();
        for (DeviceCategory deviceCategory : deviceCategories) {
            for (Supplier s : deviceCategory.getSuppliers()) {
                if (ObjectsCompat.equals(s.getId(), supplierId)) {
                    this.supplier = s;
                }
            }
        }
        // 如果缓存里没有该供应商则默认为好年华
        if (supplier == null) {
            supplier = new Supplier();
            supplier.setWriteUuid(BleDataManager.WRITE_CHARACTERISTIC_UUID);
            supplier.setServiceUuid(BleDataManager.SERVICE_UUID);
            supplier.setNotifyUuid(BleDataManager.NOTIFY_DESCRIPTOR_UUID);
            supplier.setAgreement(AgreementVersion.HAONIANHUA.getType());
        }
    }

    /**
     * 用于子类获取供应商来判断是否需要显示温馨提示guideDialog
     */
    protected Supplier getSupplier() {
        return supplier;
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
        orderId = 0L;
        orderStatus = null;
        purelyCheckoutFlag = false;
        step = null;
    }

    /**
     * 不同地方跳转，显示不同页面
     * @param homePageJump 是否是从首页跳转而来
     */
    @Override
    public void setHomePageJump(boolean homePageJump) {
        this.homePageJump = homePageJump;
    }

    @Override
    public void cancelTimer() {
        if (null != timer) {
            timer.cancel();
        }
    }


    /**
     * 连接间隔为5s
     * @param macAddress 设备mac地址
     */
    @Override
    public void onPreConnect(@NonNull String macAddress) {

        // 校验macAddress
        if (TextUtils.isEmpty(macAddress)) {
            if (getMvpView() != null) {
                getMvpView().post(() -> getMvpView().onError(TradeError.DEVICE_BROKEN_2));
                getMvpView().post(() -> getMvpView().hideLoading());
            }
            Log.wtf(TAG, "macAddress为空!");
            reportError(getStep().getStep(), ConnectErrorType.BLE_CONNECT_ERROR.getType(),
                    DisplayErrorType.CONNECT_ERROR.getType(), "macAddress为空!", "");
            return;
        }

        Long lastConnectTime = TimeHolder.get().getLastConnectTime();
        if (null != lastConnectTime) {
            Long diff = System.currentTimeMillis() - lastConnectTime;
            if (diff < 5000) {
                new Handler().postDelayed(() -> onConnect(macAddress), 5000 - diff);
            } else {
                onConnect(macAddress);
            }
        } else {
            onConnect(macAddress);
        }

    }


    /**
     * 连接间隔为5s
     * @param macAddress 设备mac地址
     */
    @Override
    public void onPreConnect(@NonNull String macAddress  , boolean isScan) {
        // 校验macAddress
        if (TextUtils.isEmpty(macAddress)) {
            if (getMvpView() != null) {
                getMvpView().post(() -> getMvpView().onError(TradeError.DEVICE_BROKEN_2));
                getMvpView().post(() -> getMvpView().hideLoading());
            }
            Log.wtf(TAG, "macAddress为空!");
            reportError(getStep().getStep(), ConnectErrorType.BLE_CONNECT_ERROR.getType(),
                    DisplayErrorType.CONNECT_ERROR.getType(), "macAddress为空!", "");
            return;
        }

        Long lastConnectTime = TimeHolder.get().getLastConnectTime();
        if (null != lastConnectTime) {
            Long diff = System.currentTimeMillis() - lastConnectTime;
            if (diff < 5000) {
                new Handler().postDelayed(() -> onConnect(macAddress), 5000 - diff);
            } else {
                onConnect(macAddress);
            }
        } else {
            onConnect(macAddress);
        }

    }

    @Override
    public void onConnect(@NonNull String macAddress, Long supplierId) {
        setSupplierId(supplierId);
        onConnect(macAddress);
    }

    @Override
    public void onConnect(@NonNull String macAddress) {
        deviceNo = macAddress;

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
                reportError(getStep().getStep(), ConnectErrorType.BLE_CONNECT_ERROR.getType(),
                        DisplayErrorType.CONNECT_ERROR.getType(), "设备连接超时。", "");
                // 关闭蓝牙连接
                closeBleConnection();

                if (getMvpView() != null) {
                    getMvpView().post(() -> getMvpView().onError(TradeError.CONNECT_ERROR_5));
                }
            }
        };
        Log.i(TAG, "启动15s定时器......");
        timer.start();

        //  macAddress是服务器返回的deviceNo  ,currentMacAddress是全局变量，是正确的蓝牙设备mac地址
        // 设备连接上存储mac地址供后续读写数据使用
        // 查询是否存在该deviceNo的macAddress
            if (deviceDataManager.getMacAddressByDeviceNo(macAddress) != null) {
                Log.i(TAG, "缓存中存在macAddress，不需要扫描");
                currentMacAddress = deviceDataManager.getMacAddressByDeviceNo(macAddress);
                realConnect(macAddress);
                return;
            }




        // 扫描macAddress
        Log.i(TAG, "开始扫描macAddress");
        bleDataManager.scan(BluetoothConstants.SCAN_TYPE_BLE, new BluetoothScanResponse() {
            boolean savedScanType = false;
            @Override
            public void onScanStarted() {
                Log.d(TAG, "onScanStarted thread" + Thread.currentThread().getName());
            }

            @Override
            public void onDeviceFounded(BluetoothScanResult result) {
                if (!savedScanType) {
                    Log.d(TAG, "扫描到设备，缓存当前扫描方式" + scanType);
                    savedScanType = true;
                    deviceDataManager.saveScanType(scanType);
                }
                Log.d(TAG, "onDeviceFounded thread" + Thread.currentThread().getName());
                boolean validDevice = isValidDevice(result, macAddress);
                if (!validDevice) {
                    return;
                }

                if (currentMacAddress != null && currentMacAddress.equalsIgnoreCase(result.getAddress())) {
                    Log.i(TAG, "扫描获取macAddress在当前上下文已经存在，无需重复计算。macAddress:" + currentMacAddress);
                    bleDataManager.stopScan();
                    return;
                }

                currentMacAddress = result.getAddress();
                Log.i(TAG, "扫描获取macAddress成功。macAddress:" + currentMacAddress);
                deviceDataManager.setDeviceNoAndMacAddress(macAddress, currentMacAddress);
                realConnect(macAddress);
                bleDataManager.stopScan();
            }

            @Override
            public void onScanStopped() {
                Log.d(TAG, "onScanStopped thread" + Thread.currentThread().getName());
            }

            @Override
            public void onScanCanceled() {
                Log.d(TAG, "onScanCanceled thread" + Thread.currentThread().getName());
            }
        });
    }

    private boolean isValidDevice(BluetoothScanResult result, String deviceNo) {
        if (!TextUtils.equals(deviceNo, result.getName())) {
            return false;
        }
        if (scanType == BluetoothConstants.SCAN_TYPE_CLASSIC) {
            return true;
        }
        boolean validDevice = false;
        ScanRecord scanRecord = ScanRecord.parseFromBytes(result.getScanRecord());
        if (null != scanRecord && null != scanRecord.getServiceUuids()) {
            for (ParcelUuid parcelUuid : scanRecord.getServiceUuids()) {
                if (parcelUuid.toString().equalsIgnoreCase(supplier.getServiceUuid())) {
                    validDevice = true;
                    break;
                }
            }
        }
        return validDevice;
    }

    @Override
    public void toggleScanType() {
        if (scanType == BluetoothConstants.SCAN_TYPE_CLASSIC) {
            scanType = BluetoothConstants.SCAN_TYPE_BLE;
        } else if (scanType == BluetoothConstants.SCAN_TYPE_BLE) {
            scanType = BluetoothConstants.SCAN_TYPE_CLASSIC;
        } else {
            scanType = BluetoothConstants.SCAN_TYPE_BLE;
        }
    }

    private void realConnect(String macAddress) {
        // 正常流程
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
                    .subscribe(this::handleResult, throwable ->
                            Log.wtf(TAG, "接收设备返回的数据失败 thread" + Thread.currentThread().getName(), throwable));
        }

        // 3、创建共享连接q
        Log.d(TAG, "注册连接监听");
        bleDataManager.registerConnectStatusListener(currentMacAddress, status -> {
            // 处理蓝牙连接状态
            switch (status) {
                case BluetoothConstants.STATE_CONNECTED:
                    Log.d(TAG, "[ConnectStatusListener]蓝牙已连接");
                    break;
                case BluetoothConstants.STATE_CONNECTING:
                    Log.d(TAG, "[ConnectStatusListener]蓝牙正在连接");
                    break;
                case BluetoothConstants.STATE_DISCONNECTED:
                    handleDisConnectError("[ConnectStatusListener]蓝牙已经断开连接");
                    break;
                case BluetoothConstants.STATE_DISCONNECTING:
                    Log.d(TAG, "[ConnectStatusListener]蓝牙正在断开连接");
                    break;
            }

        });

        // 4、连接设备
        safeWait(300);
        Log.d(TAG, "开始连接设备 thread " + Thread.currentThread().getName());
        switch (AgreementVersion.getAgreement(supplier.getAgreement())) {
            case HAONIANHUA:
                connectHaoNianHaoDevice();
                break;
            case XINNA:
                connectXinNaDevice();
                break;
        }
    }

    private void connectXinNaDevice() {
        Log.i(TAG, "连接辛纳设备 thread " + Thread.currentThread().getName());
        bleDataManager.connect(currentMacAddress, (code, gatt) -> {
            if (code != BluetoothConstants.CONN_RESPONSE_SUCCESS) {
                handleDisConnectError("辛纳设备连接失败 code: " + code);
                return;
            }
            if (bleDataManager.setNotify(currentMacAddress, UUID.fromString(supplier.getServiceUuid()),
                    UUID.fromString(supplier.getNotifyUuid()), true)) {
                Log.i(TAG, "辛纳设备设置notify成功 " + Thread.currentThread().getName());
                bleDataManager.writeDescriptor(currentMacAddress,
                        UUID.fromString(supplier.getServiceUuid()),
                        UUID.fromString(supplier.getNotifyUuid()),
                        BluetoothConstants.CLIENT_CHARACTERISTIC_CONFIG,
                        BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE, code1 -> {
                            Log.d(TAG, "写入ENABLE_NOTIFICATION_VALUE code" + code1 + " thread " + Thread.currentThread().getName());
                            if (code1 != BluetoothConstants.GATT_SUCCESS) {
                                handleDisConnectError("辛纳设备写入ENABLE_NOTIFICATION_VALUE失败 code:" + code1);
                                return;
                            }
                            afterBleConnected();
                            bleDataManager.notify(currentMacAddress, UUID.fromString(supplier.getServiceUuid()),
                                    UUID.fromString(supplier.getWriteUuid()), data -> {
                                        if (null != data) {
                                            String result = HexBytesUtils.bytesToHexString(data);
                                            Log.i(TAG, "接收到设备数据" + result + " thread" + Thread.currentThread().getName());
                                            processCommandResult(result);
                                        }
                                    });
                        });
            } else {
                handleDisConnectError("辛纳设备设置notify失败");
            }
        });
    }


    private void afterBleConnected() {
        if (reconnect) {
            Log.i(TAG, "当前为重连状态");
            if (null == getStep()) {
                Log.i(TAG, "首次连接就连接不上，需要重新下发握手指令:" + connectCmd);
                waitConnectCmdResult();
                onWrite(connectCmd);
                reconnect = false; // 重置重连标志
            } else if (TradeStep.PAY == getStep()) { // 支付页面重连
                // 重新连接成功时不需要再次握手
                // Log.i(TAG, "当前为支付页面重连，不需要重新下发握手指令，只需要页面显示重连成功。");
                // getMvpView().post(() -> getMvpView().onReconnectSuccess());
                // 最新修改，支付页面重连，继续下发握手指令，否则单纯物理连接上会被设备踢掉
                waitConnectCmdResult();
                onWrite(connectCmd);
                reconnect = false; // 重置重连标志
            } else { // 结算页面重连
                Log.i(TAG, "当前为结算页面重连");
                waitOrderCheckResult();
                // 如果查询订单时出现错误，显示连接错误并跳过之后的步骤
                if (checkOrderErrorFlag) {
                    checkOrderErrorFlag = false;
                    return;
                }
                if (null == orderStatus || orderStatus.getStatus() == null) {
                    Log.wtf(TAG, "查不到对应的未结账订单，不应该发生此种状况！！！");
                    // 如果订单ID不为空，直接到订单详情页面
                    if (orderId != 0L) {
                        if (getMvpView() != null) {
                            getMvpView().onFinish(orderId);
                        }
                    } else {
                        setStep(TradeStep.PAY);
                        onWrite(connectCmd);
                    }
//                    getMvpView().post(() -> getMvpView().onError(TradeError.CONNECT_ERROR_3));
                } else {
                    if (OrderStatus.getOrderStatus(orderStatus.getStatus()) == OrderStatus.FINISHED) { // 订单已结单
                        Log.i(TAG, "重连后发现订单已被结算，跳转至订单详情页。orderId:" + orderStatus.getOrderId());
                        if (getMvpView() != null) {
                            getMvpView().onFinish(orderStatus.getOrderId()); // 跳转订单详情页
                        }
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
            // 如果查询订单时出现错误，显示连接错误并跳过之后的步骤
            if (checkOrderErrorFlag) {
                checkOrderErrorFlag = false;
                return;
            }
            // String savedConnectCmd = sharedPreferencesHelp.getConnectCmd(currentMacAddress);
            // Log.i(TAG, "获取已保存的握手指令：" + savedConnectCmd);
            if (null != orderStatus && null != orderStatus.getStatus() && OrderStatus.getOrderStatus(orderStatus.getStatus()) == OrderStatus.USING) {  // 有订单未计算拿上次连接的握手指令（这里待验证是否有影响）
                if (homePageJump && !orderStatus.isExistsUnsettledOrder()) {
                    Log.i(TAG, "首页点击继续用水，且未结账订单已超出指定时间范围，走正常流程，继续下发握手指令。command:" + connectCmd);
                    waitConnectCmdResult();
                    onWrite(connectCmd);
                } else {
                    Log.i(TAG, String.format("正常连接发现有订单未被结算，继续下发握手指令。command: %s, orderId:%s", connectCmd, orderStatus.getOrderId()));
                    // orderId = orderStatus.getOrderId();
                    waitConnectCmdResult();
                    onWrite(connectCmd);
                    purelyCheckoutFlag = true;
                }
            } else {
                // 握手连接
                Log.i(TAG, "正常连接成功，下发握手指令。command:" + connectCmd);
                waitConnectCmdResult();
                onWrite(connectCmd);
            }
        }
    }

    private void connectHaoNianHaoDevice() {
        Log.i(TAG, "连接好年华设备 thread " + Thread.currentThread().getName());
        bleDataManager.connect(currentMacAddress, (code, gatt) -> {
            Log.d(TAG, "connect code:" + code + " thread " + Thread.currentThread().getName());
            Log.d(TAG, "ServiceUuid" + supplier.getServiceUuid() + "notifyUuid" + supplier.getNotifyUuid()
                    + "writeUuid" + supplier.getWriteUuid());
            if (code != BluetoothConstants.CONN_RESPONSE_SUCCESS) {
                handleDisConnectError("连接好年华设备失败 code:" + code);
                return;
            }
            if (gatt == null || gatt.getService(UUID.fromString(supplier.getServiceUuid())) == null) {
                handleDisConnectError("连接好年华设备失败 code:" + code + "获取不到service uuid:" + supplier.getServiceUuid());
                return;
            }
            for (BluetoothGattCharacteristic characteristic : gatt.getService(UUID.fromString(supplier.getServiceUuid())).getCharacteristics()) {
                if (characteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_NOTIFY) {
                    if (bleDataManager.setNotify(currentMacAddress, UUID.fromString(supplier.getServiceUuid()),
                            characteristic.getUuid(), true)) {
                        Log.d(TAG, "设置notify成功 thread " + Thread.currentThread().getName());
                        bleDataManager.writeDescriptor(currentMacAddress, UUID.fromString(supplier.getServiceUuid()),
                                characteristic.getUuid(), UUID.fromString(supplier.getNotifyUuid()),
                                BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE, code1 -> {
                                    Log.d(TAG, "写入ENABLE_NOTIFICATION_VALUE code" + code1 + " thread " + Thread.currentThread().getName());
                                    if (code1 != BluetoothConstants.GATT_SUCCESS) {
                                        handleDisConnectError("好年华设备写入ENABLE_NOTIFICATION_VALUE失败 code:" + code1);
                                        return;
                                    }
                                    bleDataManager.notify(currentMacAddress, UUID.fromString(supplier.getServiceUuid()),
                                            characteristic.getUuid(), data -> {
                                                if (null != data) {
                                                    String result = HexBytesUtils.bytesToHexString(data);
                                                    Log.i(TAG, "接收到设备数据" + result + " thread " + Thread.currentThread().getName());
                                                    processCommandResult(result);
                                                }

                                            });
                                    afterBleConnected();
                                });
                    } else {
                        Log.d(TAG, "设置notify失败 thread " + Thread.currentThread().getName());
                        handleDisConnectError("设置notify失败");
                    }
                    return;
                }
            }
        });
    }

    @Override
    public void onReconnect(@NonNull String macAddress) {
        // 重置蓝牙连接
        resetBleConnection();

        // 设置重连标识
        reconnect = true;

        // 等待1s
        safeWait(1000);

        // 触发重连机制
        onConnect(macAddress);
    }

    @Override
    public void onWrite(@NonNull String command) {
        if (bleDataManager.getConnectStatus(currentMacAddress) != BluetoothConstants.STATE_CONNECTED) {
            reportError(getStep().getStep(), ConnectErrorType.BLE_CONNECT_ERROR.getType(),
                    DisplayErrorType.CONNECT_ERROR.getType(), "发送指令时设备未连接，command:" + command,
                    "connectStatus: " + bleDataManager.getConnectStatus(currentMacAddress));
            if (getMvpView() != null) {
                getMvpView().post(() -> getMvpView().onError(TradeError.CONNECT_ERROR_1));
            }
            return;
        }

        if (TextUtils.isEmpty(command)) {
            reportError(getStep().getStep(), ConnectErrorType.BLE_CONNECT_ERROR.getType(),
                    DisplayErrorType.CONNECT_ERROR.getType(), "发送指令时指令丢失",
                    "");
            if (getMvpView() != null) {
                getMvpView().post(() -> getMvpView().onError(TradeError.CONNECT_ERROR_4));
            }
            return;
        }
        byte[] commandBytes = HexBytesUtils.hexStr2Bytes(command);
        bleDataManager.writeNoRsp(currentMacAddress, UUID.fromString(supplier.getServiceUuid()),
                UUID.fromString(supplier.getWriteUuid()), commandBytes, code -> {
                    if (code == BluetoothConstants.GATT_OTHER_FAILURE) {
                        handleWriteError(command);
                    } else if (code == BluetoothConstants.GATT_SUCCESS) {
                        Log.d(TAG, "发送指令成功 command: " + command + " thread " + Thread.currentThread().getName());
                    }
                });
    }

    private void handleWriteError(String command) {
        reportError(getStep().getStep(), ConnectErrorType.BLE_CONNECT_ERROR.getType(),
                DisplayErrorType.CONNECT_ERROR.getType(), "发送指令失败！command:" + command, "");
        // 结算找零时写入设备失败
        if (TextUtils.equals(command, checkoutCmd)) {
            if (getMvpView() != null) {
                getMvpView().post(() -> getMvpView().onError(TradeError.DEVICE_BROKEN_1));
            }
        } else {
            if (getMvpView() != null) {
                getMvpView().post(() -> getMvpView().onError(TradeError.CONNECT_ERROR_4));
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

    private void handleDisConnectError(String disconnectReason) {
        if (handleBleClose.compareAndSet(false, true)) {
            Log.d(TAG, disconnectReason + " thread " + Thread.currentThread().getName());
            reportError(getStep().getStep(), ConnectErrorType.BLE_CONNECT_ERROR.getType(),
                    DisplayErrorType.CONNECT_ERROR.getType(), disconnectReason,
                    "handleDisConnectError() thread " + Thread.currentThread().getName());

            // 跳转至连接失败页面
            if (getMvpView() != null) {
                getMvpView().post(() -> getMvpView().onError(TradeError.CONNECT_ERROR_1));
            }
            handleBleClose.set(false);
        }
    }

    // 统一处理设备连接异常
    private void handleDisConnectError() {
        handleDisConnectError("蓝牙连接已断开！");
    }

    @Override
    public void onDisConnect() {
        Log.d(TAG, "onDisConnect");
        if (null != busSubscriber) {
            busSubscriber.unsubscribe();
        }
        if (null != timer) {
            timer.cancel();
        }
        closeBleConnection();
    }

    @NonNull
    @CheckResult
    private <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifeCycleSubject, event);
    }

    // 安全等待时长间隔
    @SuppressWarnings("all")
    private void safeWait(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            Log.wtf(TAG, e);
        }
    }

    // 网络请求获取握手连接指令
    private void getConnectCommand(String macAddress) {
        ConnectCommandReqDTO reqDTO = new ConnectCommandReqDTO();
        reqDTO.setMacAddress(macAddress);
        addObserver(deviceDataManager.getConnectCommand(reqDTO), new NetworkObserver<ApiResult<ConnectCommandRespDTO>>(false) {
            @Override
            public void onReady(ApiResult<ConnectCommandRespDTO> result) {
                if (null == result.getError()) {
                    // 存储deviceToken
                    if (result.getData() != null && result.getData().getMacAddress() != null
                            && result.getData().getDeviceToken() != null) {
                        deviceDataManager.setDeviceToken(result.getData().getMacAddress(), result.getData().getDeviceToken());
                        Log.i(TAG, "收到deviceToken：" + result.getData().getDeviceToken());
                    }
                    synchronized (connectCmdLock) {
                        connectCmd = result.getData().getConnectCmd();
                        Log.i(TAG, "获取握手指令成功。command:" + connectCmd);
                        connectCmdLock.notifyAll();
                    }
                } else {
                    if (result.getError().getCode() == BizError.DEVICE_BREAKDOWN.getCode()) {
                        reportError(getStep().getStep(), ConnectErrorType.SERVER_ERROR.getType(),
                                DisplayErrorType.DEVICE_ERROR.getType(), "服务器返回该设备为故障设备",
                                "");
                        if (getMvpView() != null) {
                            getMvpView().post(() -> getMvpView().onError(TradeError.DEVICE_BROKEN_3));
                        }
                    } else {
                        Log.wtf(TAG, "服务器返回,获取开阀指令失败");
                        reportError(getStep().getStep(), ConnectErrorType.SERVER_ERROR.getType(),
                                DisplayErrorType.SYSTEM_ERROR.getType(), "获取开阀指令失败(服务器响应)",
                                "");
                        if (getMvpView() != null) {
                            getMvpView().post(() -> getMvpView().onError(TradeError.SYSTEM_ERROR));
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                reportError(getStep().getStep(), ConnectErrorType.SERVER_ERROR.getType(),
                        DisplayErrorType.CONNECT_ERROR.getType(), "获取开阀指令失败(服务器未响应)",
                        "");
                Log.wtf(TAG, "服务器未返回,获取开阀指令失败");
                if (getMvpView() != null) {
                    getMvpView().post(() -> getMvpView().onError(TradeError.CONNECT_ERROR_3));
                }
            }
        }, Schedulers.io());
    }

    // 校验用户对指定设备使用订单的结算状态
    private void checkOrderStatus(String macAddress) {
        UnsettledOrderStatusCheckReqDTO reqDTO = new UnsettledOrderStatusCheckReqDTO();
        reqDTO.setMacAddress(macAddress);
        addObserver(deviceDataManager.checkOrderStatus(reqDTO), new NetworkObserver<ApiResult<UnsettledOrderStatusCheckRespDTO>>(false) {
            @Override
            public void onReady(ApiResult<UnsettledOrderStatusCheckRespDTO> result) {
                if (null == result.getError()) {
                    synchronized (orderStatusLock) {
                        orderStatus = result.getData();
                        if (orderStatus.getOrderId() != null) {
                            orderId = orderStatus.getOrderId();
                        }
                        Log.i(TAG, "获取订单状态成功。orderStatus:" + orderStatus);
                        orderStatusLock.notifyAll();
                    }
                } else {
                    reportError(getStep().getStep(), ConnectErrorType.SERVER_ERROR.getType(),
                            DisplayErrorType.SYSTEM_ERROR.getType(), "获取订单状态失败(服务器响应)",
                            "");
                    Log.wtf(TAG, "服务器返回,获取订单状态失败");
                    if (getMvpView() != null) {
                        getMvpView().post(() -> getMvpView().onError(TradeError.SYSTEM_ERROR));
                    }
                    synchronized (orderStatusLock) {
                        checkOrderErrorFlag = true;
                        orderStatusLock.notifyAll();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.wtf(TAG, "服务器未返回,获取订单状态失败");
                reportError(getStep().getStep(), ConnectErrorType.SERVER_ERROR.getType(),
                        DisplayErrorType.CONNECT_ERROR.getType(), "获取订单状态失败(服务器未响应)",
                        "");
                if (getMvpView() != null) {
                    getMvpView().post(() -> getMvpView().onError(TradeError.CONNECT_ERROR_3));
                }
                synchronized (orderStatusLock) {
                    checkOrderErrorFlag = true;
                    orderStatusLock.notifyAll();
                }
            }
        }, Schedulers.io());
    }

    // 网络请求处理设备响应结果
    private void processCommandResult(String result) {
        try {
            String prefix = result.substring(0, 4);
            if (TextUtils.equals(Command.CLOSE_VALVE.getRespPrefix(), prefix)) {
                setStep(TradeStep.CLOSE_VALVE);
            } else if (TextUtils.equals(Command.OPEN_VALVE.getRespPrefix(), prefix)) {
                // 存储设备响应结果
                saveDeviceResult(result, orderId);
            }
        } catch (Exception e) {
            Log.wtf(TAG, "获取设备响应结果前缀失败");
            reportError(getStep().getStep(), ConnectErrorType.RESULT_INVALID.getType(),
                    DisplayErrorType.CONNECT_ERROR.getType(), "获取设备响应结果前缀失败,result:" + result,
                    e.getMessage());
            if (getMvpView() != null) {
                getMvpView().onError(TradeError.CONNECT_ERROR_4);
            }
        }
        CmdResultReqDTO reqDTO = new CmdResultReqDTO();
        reqDTO.setData(result);
        reqDTO.setMacAddress(deviceNo);
        addObserver(deviceDataManager.processCmdResult(reqDTO), new NetworkObserver<ApiResult<CmdResultRespDTO>>(false) {
            @Override
            public void onReady(ApiResult<CmdResultRespDTO> result) {
                // 存储deviceToken
                if (result.getData() != null && result.getData().getMacAddress() != null
                        && result.getData().getDeviceToken() != null) {
                    deviceDataManager.setDeviceToken(result.getData().getMacAddress(), result.getData().getDeviceToken());
                    Log.i(TAG, "收到deviceToken：" + result.getData().getDeviceToken());
                }
                Log.i(TAG, "通知主线程更新数据。" + result.getData());
                RxBus.getDefault().post(result);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (getStep() == TradeStep.CLOSE_VALVE) {
                    setStep(TradeStep.SETTLE);
                }
                reportError(getStep().getStep(), ConnectErrorType.SERVER_ERROR.getType(),
                        DisplayErrorType.CONNECT_ERROR.getType(), "发送设备响应到服务器失败,result:" + result,
                        e == null ? "" : e.getMessage());
                if (getMvpView() != null) {
                    getMvpView().post(() -> getMvpView().onError(TradeError.CONNECT_ERROR_3));
                }
            }
        }, Schedulers.io());
    }

    private void handleResult(ApiResult<CmdResultRespDTO> result) {
        Log.i(TAG, "主线程开始处理指令响应结果");
        if (null == result.getError()) {
            // 下一步执行指令
            String nextCommand = result.getData().getNextCommand();
            if (result.getData().getSrcCommandType() == null) {
                Log.wtf(TAG, "服务器未返回ScrCommandType");
                reportError(getStep().getStep(), ConnectErrorType.SERVER_ERROR.getType(),
                        DisplayErrorType.SYSTEM_ERROR.getType(), "服务器未返回ScrCommandType",
                        "");
                closeBleConnection();
                if (getMvpView() != null) {
                    getMvpView().onError(TradeError.SYSTEM_ERROR);
                }
                return;
            }
            switch (Command.getCommand(result.getData().getSrcCommandType())) {
                case CONNECT:
                    // 如果用户本人在三小时之内再次连接该设备，需要进入第二步账单结算页面
                    // 表明用户是正在使用状态
                    if (!reconnect && null != orderStatus && null != orderStatus.getStatus() && OrderStatus.getOrderStatus(orderStatus.getStatus()) == OrderStatus.USING) {
                        // 记录预结账指令，此时阀门已经被长按关闭，但是订单没有被其它用户带回
                        Log.i(TAG, "用户在该设备上存在未结账订单，直接跳转至结算页面，获取到预结账指令。command:" + nextCommand);
                        if (orderStatus.isExistsUnsettledOrder() || !homePageJump) { // 未结算订单在指定时间范围内
                            Log.i(TAG, "未结算订单在指定时间范围内或者由未结账单页面跳转过来，显示结算页面");
                            reopenNextCmd = nextCommand;
                            if (getMvpView() != null) {
                                getMvpView().onConnectSuccess(TradeStep.SETTLE, orderStatus);
                            }
                        } else {
                            Log.i(TAG, "未结算订单已经超出指定时间范围，继续下发预结账指令，走正常流程");
                            precheckCmd = nextCommand;
                            onWrite(precheckCmd);
                        }
                        return;
                    }
                    // 存在未结账订单，直接后台预结账处理
                    // 如果存在未结账订单，需要先结算旧账单
                    if (null != result.getData().getNextCommand()) {  //  != null   表明存在未结账订单
                        precheckCmd = nextCommand;
                        if (!reconnect) { // 正常流程
                            // 下发预结账指令
                            Log.i(TAG, "正常流程，设备上存在未结账订单，获取到预结账指令。command:" + nextCommand);
                            onWrite(precheckCmd);
                        } else {
                            //  自己的未结账订单，显示未结账状态
                            Log.i(TAG, "用户在结算页面重新连接成功，设备上存在未结账订单，获取到预结账指令。command:" + nextCommand);
                            if (getMvpView() != null) {
                                getMvpView().onReconnectSuccess(orderStatus);
                            }
                            reconnectNextCmd = precheckCmd;
                        }
                    } else {
                        // 握手连接成功，进入正常用水流程
                        Log.i(TAG, "正常流程，设备上不存在未结账订单。");
                        if (getMvpView() != null) {
                            getMvpView().onConnectSuccess(TradeStep.PAY);
                        }
                    }
                    break;
                case OPEN_VALVE:
                    Log.i(TAG, "正常流程，获取到关阀指令。command:" + nextCommand);
                    closeCmd = nextCommand;
                    saveCloseCmd(closeCmd, orderId);
                    if (getMvpView() != null) {
                        getMvpView().onOpen();
                    }
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
                        if (getMvpView() != null) {
                            getMvpView().onFinish(orderId);
                        }
                    } else if (precheckFlag) {
                        // 当前为预结账状态，结账完毕后关闭更改标识
                        precheckFlag = false;
                        if (reconnect) {
                            // 重连状态下，走预结账->结账流程时，是本人主动结账，跳转账单详情页
                            Log.i(TAG, "结账完成");
                            if (getMvpView() != null) {
                                getMvpView().onFinish(orderId);
                            }
                        } else {
                            // 非重连状态下，是主动帮别人结账，此时用户还未进入用水流程，需要再次握手，否则会报设备使用次数不对
                            Log.i(TAG, "结账完成,当前为主动帮别人结账，正常流程，继续下发握手指令。");
                            if (null != orderStatus) {
                                Log.i(TAG, "orderStatus不为空，说明是给自己超过2小时的订单结账，需要重置orderStatus的状态。");
                                orderStatus = null;
                            }
                            onWrite(connectCmd);
                        }
                    } else {
                        // 当前为非预结账状态，结账完毕后跳转账单详情页
                        Log.i(TAG, "结账完成");
                        if (getMvpView() != null) {
                            getMvpView().onFinish(orderId);
                        }
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
                case UNKNOWN:
                    Log.wtf(TAG, "服务器返回未知指令");
                    break;
            }
        } else {
            Log.e(TAG, String.format("处理异常返回结果。code:%s, msg:%s", result.getError().getCode(), result.getError().getDisplayMessage()));
            // 如果在结账时服务器返回异常则状态改为SETTLE
            if (getStep() == TradeStep.CLOSE_VALVE) {
                setStep(TradeStep.SETTLE);
            }
            if (result.getError().getCode() == BleErrorType.BLE_DEVICE_BUSY.getCode()) {
                Log.e(TAG, "设备正在被使用");
                // 如果是重连状态下，此时可以直接下发关阀指令
                if (reconnect) {
                    // 显示重连成功
                    if (getMvpView() != null) {
                        getMvpView().onReconnectSuccess(orderStatus);
                    }
                    if (checkCloseCmd()) {
                        reconnectNextCmd = closeCmd;
                    }
                } else { // 首次连接
                    if (null != orderStatus && null != orderStatus.getStatus() && OrderStatus.getOrderStatus(orderStatus.getStatus()) == OrderStatus.USING) { // 用户主动上来结账
                        //if (orderStatus.isExistsUnsettledOrder() || !homePageJump) { // 未结账订单在指定时间范围内
                        Log.i(TAG, "处理未结账订单，此时阀门处于打开状态，下发关阀指令即可。");
                        if (getMvpView() != null) {
                            getMvpView().onConnectSuccess(TradeStep.SETTLE, orderStatus);
                        }
                        if (checkCloseCmd()) {
                            reopenNextCmd = closeCmd;
                        }
                        //} else {
                        //    Log.i(TAG, "处理未结账订单，订单时间超出指定时间范围内并且从首页跳转过来，此时阀门处于打开状态，");
                        //    Log.wtf(TAG, "未结账订单超出两个小时时间范围，阀门仍处于打开状态，不应该出现这种状况");
                        //    getMvpView().onError(TradeError.DEVICE_BROKEN_2);
                        // }
                    } else {
                        // 提示用户设备已被其它用户使用
                        closeBleConnection();
                        if (getMvpView() != null) {
                            getMvpView().onError(TradeError.DEVICE_BUSY);
                        }
                    }
                }
            } else if (result.getError().getCode() == BleErrorType.BLE_UNKNOWN_ERROR.getCode()) {
                reportError(getStep().getStep(), ConnectErrorType.SERVER_ERROR.getType(),
                        DisplayErrorType.DEVICE_ERROR.getType(), "服务器返回未知错误,code:" + result.getError().getCode(),
                        "");
                closeBleConnection();
                if (getMvpView() != null) {
                    getMvpView().onError(TradeError.DEVICE_BROKEN_2);
                }
            } else if (result.getError().getCode() == BleErrorType.BLE_CMD_RESULT_ERROR.getCode()) {
                Log.i(TAG, "设备未完全开启");
                reportError(getStep().getStep(), ConnectErrorType.RESULT_INVALID.getType(),
                        DisplayErrorType.CONNECT_ERROR.getType(), "服务器返回指令返回结果不合法,设备未完全开启,code:" + result.getError().getCode(),
                        "");
                if (getMvpView() != null) {
                    getMvpView().onError(TradeError.CONNECT_ERROR_1);
                }
            }
            Integer cmdType = result.getError().getBleCmdType();
            if (null != cmdType) {
                // 确认支付时异常
                if (Command.OPEN_VALVE == Command.getCommand(cmdType)) {
                    Log.wtf(TAG, "设备开阀异常");
                    reportError(getStep().getStep(), ConnectErrorType.SERVER_ERROR.getType(),
                            DisplayErrorType.DEVICE_ERROR.getType(), "设备开阀异常,code:" + result.getError().getCode(),
                            "");
                    closeBleConnection();
                    if (getMvpView() != null) {
                        getMvpView().onError(TradeError.DEVICE_BROKEN_2);
                    }
                } else if (Command.CLOSE_VALVE == Command.getCommand(cmdType) || Command.PRE_CHECK == Command.getCommand(cmdType) || Command.CHECK_OUT == Command.getCommand(cmdType)) {
                    Log.wtf(TAG, "订单结算异常");
                    reportError(getStep().getStep(), ConnectErrorType.SERVER_ERROR.getType(),
                            DisplayErrorType.DEVICE_ERROR.getType(), "订单结算异常,code:" + result.getError().getCode(),
                            "");
                    closeBleConnection();
                    // 结算时异常
                    if (getMvpView() != null) {
                        getMvpView().onError(TradeError.DEVICE_BROKEN_1);
                    }
                }
            } else {
                // 系统异常
                Log.wtf(TAG, String.format("服务器后台出错, errorCode:%s, errorMsg:%s", result.getError().getCode(), result.getError().getDisplayMessage()));
                reportError(getStep().getStep(), ConnectErrorType.SERVER_ERROR.getType(),
                        DisplayErrorType.SYSTEM_ERROR.getType(), "服务器后台出错,code:" + result.getError().getCode(),
                        "");
                closeBleConnection();
                if (getMvpView() != null) {
                    getMvpView().onError(TradeError.SYSTEM_ERROR);
                }
            }
        }
    }


    /**
     * 检测管阀指令 ， 若管阀指令为空，先在缓存中获取指令，获取到将管阀指令赋值给closeCmd ，若获取不到，再从服务器获取管阀指令，
     * 若服务器获取不到管阀指令，则提示用户手动管阀。
     * @return
     */
    private boolean checkCloseCmd() {
        if (TextUtils.isEmpty(closeCmd)) {
            Log.e(TAG, "重连状态关阀指令为空, 从缓存中获取关阀指令");
            String savedCloseCmd = getCloseCmd(orderId);
            if (TextUtils.isEmpty(savedCloseCmd)) {
                Log.e(TAG, "从缓存中获取关阀指令为空，获取上次设备响应重新向服务器请求关阀指令");
                String savedDeviceResult = getDeviceResult(orderId);
                if (TextUtils.isEmpty(savedCloseCmd)) {
                    Log.e(TAG, "从缓存中获取设备响应为空");
                    if (getMvpView() != null) {
                        getMvpView().onError(TradeError.CONNECT_ERROR_2);
                    }
                } else {
                    processCommandResult(savedDeviceResult);
                }
                return false;
            } else {
                closeCmd = savedCloseCmd;
            }
        }
        return true;
    }

    // 只存开阀设备响应结果
    private void saveDeviceResult(String result, Long orderId) {
        if (result == null || orderId == null || orderId <= 0) {
            return;
        }
        deviceDataManager.setDeviceResult(deviceNo,
                orderId + Constant.DIVIDER + result);
    }

    // 只存开阀设备响应结果
    private String getDeviceResult(Long orderId) {
        String deviceResultTemp = deviceDataManager.getDeviceResult(deviceNo);
        Long savedOrderId;
        String savedDeviceResult;
        try {
            String[] temp = deviceResultTemp.split(Constant.DIVIDER);
            savedOrderId = Long.valueOf(temp[0]);
            savedDeviceResult = temp[1];
            if (ObjectsCompat.equals(savedOrderId, orderId) && !TextUtils.isEmpty(savedDeviceResult)) {
                return savedDeviceResult;
            }
        } catch (Exception e) {
            Log.wtf(TAG, "从缓存中取设备响应失败", e);
        }
        return null;
    }

    private void saveCloseCmd(String closeCmd, Long orderId) {
        if (TextUtils.isEmpty(closeCmd) || orderId == null || orderId <= 0) {
            return;
        }
        deviceDataManager.setCloseCmd(deviceNo,
                orderId + Constant.DIVIDER + closeCmd);
    }

    private String getCloseCmd(Long orderId) {
        String closeCmdTemp = deviceDataManager.getCloseCmd(deviceNo);
        Long savedOrderId;
        String savedCloseCmd;
        try {
            String[] temp = closeCmdTemp.split(Constant.DIVIDER);
            savedOrderId = Long.valueOf(temp[0]);
            savedCloseCmd = temp[1];
            if (ObjectsCompat.equals(savedOrderId, orderId) && !TextUtils.isEmpty(savedCloseCmd)) {
                return savedCloseCmd;
            }
        } catch (Exception e) {
            Log.wtf(TAG, "从缓存中取关阀指令失败", e);
        }
        return null;
    }


    @Override
    public void onPay(Double prepay, Long bonusId) {
        // 校验网络
        if (getMvpView() != null && !getMvpView().isNetworkAvailable()) {
            getMvpView().onError(TradeError.CONNECT_ERROR_3);
            return;
        }

        PayReqDTO reqDTO = new PayReqDTO();
        reqDTO.setMacAddress(deviceNo);
        reqDTO.setBonusId(bonusId);
        reqDTO.setPrepay(prepay);

        addObserver(deviceDataManager.pay(reqDTO), new NetworkObserver<ApiResult<PayRespDTO>>(false) {
            @Override
            public void onReady(ApiResult<PayRespDTO> result) {
                if (null == result.getError()) {
                    // 存储deviceToken
                    if (result.getData() != null && result.getData().getMacAddress() != null
                            && result.getData().getDeviceToken() != null) {
                        deviceDataManager.setDeviceToken(result.getData().getMacAddress(), result.getData().getDeviceToken());
                        Log.i(TAG, "收到deviceToken：" + result.getData().getDeviceToken());
                    }
                    // 初始化订单id
                    orderId = result.getData().getOrderId();

                    Log.i(TAG, "支付创建订单成功。orderId：" + orderId);

                    // 向设备下发开阀指令
                    openCmd = result.getData().getOpenValveCommand();
                    Log.i(TAG, "开始下发开阀指令。command：" + openCmd);
                    onWrite(openCmd);
                } else {
                    Log.wtf(TAG, "支付创建订单失败。");
                    if (getMvpView() != null) {
                        getMvpView().post(() -> getMvpView().onError(TradeError.DEVICE_BROKEN_2));
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (getMvpView() != null) {
                    getMvpView().post(() -> getMvpView().onError(TradeError.CONNECT_ERROR_3));
                }
            }
        }, Schedulers.io());
    }

    @Override
    public void onClose() {
        Log.i(TAG, "开始关阀");
        // 校验网络
        if (getMvpView() != null && !getMvpView().isNetworkAvailable()) {
            Log.wtf(TAG, "网络不可用");
            reportError(getStep().getStep(), ConnectErrorType.BLE_CONNECT_ERROR.getType(),
                    DisplayErrorType.CONNECT_ERROR.getType(), "关阀时网络不可用",
                    "");
            getMvpView().onError(TradeError.CONNECT_ERROR_3);
            return;
        }

        if (reconnect) {
            // 重连状态下指令有两种 1 - 关阀 2 - 预结账
            Log.i(TAG, "重连状态，开始关阀" + reconnectNextCmd);
            onWrite(reconnectNextCmd);
        } else {
            Log.i(TAG, "非重连状态，开始关阀");
            if (purelyCheckoutFlag) { // 直接跳转至第二步结算
                if (null == reopenNextCmd) { // 用户卸载掉app时取回的关阀指令为空
                    reopenNextCmd = getCloseCmd(orderId);
                    if (!TextUtils.isEmpty(reopenNextCmd)) {
                        onWrite(reopenNextCmd);
                    } else {
                        // 获取缓存中的设备响应，去重新获取关阀指令
                        String saveDeviceResult = getDeviceResult(orderId);
                        if (TextUtils.isEmpty(saveDeviceResult)) {
                            Log.wtf(TAG, "从缓存中获取上一次的设备响应失败");
                            reportError(getStep().getStep(), ConnectErrorType.BLE_CONNECT_ERROR.getType(),
                                    DisplayErrorType.CONNECT_ERROR.getType(), "关阀时关阀指令丢失且上一次设备响应也丢失",
                                    "");
                            closeBleConnection();
                            if (getMvpView() != null) {
                                getMvpView().onError(TradeError.CONNECT_ERROR_2);
                            }
                        } else {
                            Log.i(TAG, "关阀指令获取失败，重新使用设备响应请求指令");
                            processCommandResult(saveDeviceResult);
                        }
                    }
                } else {
                    onWrite(reopenNextCmd); // 正常下发
                }
            } else {
                // 向设备下发关阀指令
                if (checkCloseCmd()) {
                    onWrite(closeCmd);
                }
            }
        }
    }

    @Override
    public void closeBleConnection() {
        // 不再接收数据
        disconnectTriggerSubject.onNext(null);
        // 清空连接观察者
        clearObservers();
        bleDataManager.unregisterConnectStatusListener(currentMacAddress);
        Log.d(TAG, "关闭蓝牙连接");
        bleDataManager.disconnect(currentMacAddress);
        Log.d(TAG, "停止蓝牙扫描");
        bleDataManager.stopScan();
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
        if (step == null) {
            return TradeStep.PAY;
        }
        return step;
    }

    @Override
    public void setStep(TradeStep step) {
        this.step = step;
    }

    public void setConnecting(boolean connecting) {
        this.connecting = connecting;
    }

    private void reportError(int step, int connnectErrorType, int displayErrorType,
                             String reason, String extra) {
        Log.d(TAG, "reason: " + reason + "extra: " + extra + " thread " + Thread.currentThread().getName());
        DeviceConnectErrorReqDTO reqDTO = new DeviceConnectErrorReqDTO();
        reqDTO.setConnnectErrorType(connnectErrorType);
        reqDTO.setDisplayErrorType(displayErrorType);
        reqDTO.setReason(reason);
        reqDTO.setExtra(extra);
        reqDTO.setMacAddress(deviceNo);
        if (step == 3) {
            step = 2;
        }
        reqDTO.setStep(step);
        deviceDataManager.reportDeviceConnectError(reqDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(closeTriggerSubject)
                .subscribe(new NetworkObserver<ApiResult<BooleanRespDTO>>(false) {

                    @Override
                    public void onReady(ApiResult<BooleanRespDTO> booleanRespDTOApiResult) {
                        // do nothing
                    }
                });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        closeTriggerSubject.onNext(null);
    }

    /*************************** 以下为测试用 *****************************/
    @SuppressWarnings("unused")
    private void handleResult(String data) {
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
