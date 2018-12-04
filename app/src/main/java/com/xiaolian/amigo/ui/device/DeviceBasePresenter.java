package com.xiaolian.amigo.ui.device;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ObjectsCompat;
import android.text.TextUtils;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.data.base.TimeHolder;
import com.xiaolian.amigo.data.enumeration.AgreementVersion;
import com.xiaolian.amigo.data.enumeration.BizError;
import com.xiaolian.amigo.data.enumeration.BleErrorType;
import com.xiaolian.amigo.data.enumeration.Command;
import com.xiaolian.amigo.data.enumeration.OrderStatus;
import com.xiaolian.amigo.data.enumeration.Result;
import com.xiaolian.amigo.data.enumeration.Target;
import com.xiaolian.amigo.data.enumeration.TradeError;
import com.xiaolian.amigo.data.enumeration.TradeStep;
import com.xiaolian.amigo.data.enumeration.Type;
import com.xiaolian.amigo.data.manager.BleDataManager;
import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
import com.xiaolian.amigo.data.manager.intf.IDeviceDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.device.Supplier;
import com.xiaolian.amigo.data.network.model.order.UnsettledOrderStatusCheckReqDTO;
import com.xiaolian.amigo.data.network.model.order.UnsettledOrderStatusCheckRespDTO;
import com.xiaolian.amigo.data.network.model.trade.CmdResultReqDTO;
import com.xiaolian.amigo.data.network.model.trade.CmdResultRespDTO;
import com.xiaolian.amigo.data.network.model.trade.ConnectCommandReqDTO;
import com.xiaolian.amigo.data.network.model.trade.ConnectCommandRespDTO;
import com.xiaolian.amigo.data.network.model.trade.PayReqDTO;
import com.xiaolian.amigo.data.network.model.trade.PayRespDTO;
import com.xiaolian.amigo.data.network.model.trade.UpdateDeviceRateCommandReqDTO;
import com.xiaolian.amigo.data.network.model.trade.UpdateDeviceRateCommandRespDTO;
import com.xiaolian.amigo.data.vo.DeviceCategory;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.intf.IDevicePresenter;
import com.xiaolian.amigo.ui.device.intf.IDeviceView;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.FileIOUtils;
import com.xiaolian.amigo.util.Log;
import com.xiaolian.amigo.util.TimeUtils;
import com.xiaolian.amigo.util.ble.HexBytesUtils;
import com.xiaolian.blelib.BluetoothConstants;
import com.xiaolian.blelib.ScanRecord;
import com.xiaolian.blelib.scan.BluetoothScanResponse;
import com.xiaolian.blelib.scan.BluetoothScanResult;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * 设备BasePresenter
 * @author caidong
 * @date 17/9/22
 */
public abstract class DeviceBasePresenter<V extends IDeviceView> extends BasePresenter<V>
        implements IDevicePresenter<V> {

    private static final String TAG = DeviceBasePresenter.class.getSimpleName();

    private static final String DeviceLogFileName ="DeviceLog.txt" ;

    public static final String COUNT_NAME = "TradeStatistics";

    private IBleDataManager bleDataManager;
    private IDeviceDataManager deviceDataManager;
    // 当前连接的设备
    private String currentMacAddress;
    // 设备编号 macAddress后六位 用于和服务器交互
    private String deviceNo;
    // 表示蓝牙是否连接 在连接上后设置为true 只有在handleDisConnectError 里才能设置为false
    private AtomicBoolean handleBleClose = new AtomicBoolean(false);
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

    // 供应商
    private Supplier supplier;


    private Handler handler = new Handler();

    private Runnable connectTask;

    private long  timeStamps ;   //记录时间

    private long deviceTimeStamps ; // 与设备连接发启时间

    private long serviceTimeStamps ;  // 与服务器连接发启时间


    private Type type ;


    // 页面关闭触发器
    private PublishSubject<Void> closeTriggerSubject = PublishSubject.create();

    /**
     * 正在使用的设备类型
     */
    private int  deviceType ;

    /**
     * 设备位置id
     */
    private long residenceId ;

    DeviceBasePresenter(IBleDataManager bleDataManager, IDeviceDataManager deviceDataManager) {
        super();
        this.bleDataManager = bleDataManager;
        this.deviceDataManager = deviceDataManager;
    }


    @Override
    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public void setResidenceId(long residenceId) {
        this.residenceId = residenceId;
    }

    @Override
    public void setSupplierId(Long supplierId) {
        Log.i(TAG, "设置supplierId" + supplierId);
        List<DeviceCategory> deviceCategories = deviceDataManager.getDeviceCategory();
        for (DeviceCategory deviceCategory : deviceCategories) {
            for (Supplier s : deviceCategory.getSuppliers()) {
                if (ObjectsCompat.equals(s.getId(), supplierId)) {
                    this.supplier = s;
                    writeLogFile("setSupplierId" ,"supplierId: "+supplierId , "从缓存设置供应商");
                    break;
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
            writeLogFile("setSupplierId" ,"supplierId: "+supplierId , "设置默认好年华供应商");
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
        writeLogFile("resetContext" ,"" ,"初始化参数");
    }

    /**
     * 不同地方跳转，显示不同页面
     * @param homePageJump 是否是从首页跳转而来
     */
    @Override
    public void setHomePageJump(boolean homePageJump) {
        this.homePageJump = homePageJump;
        writeLogFile("setHomePageJump" ,"" + homePageJump,"是否从首页跳转");
    }

    @Override
    public void cancelTimer() {
        if (null != timer) {
            timer.cancel();
            writeLogFile("cancelTimer" , "","取消倒计时");
        } else {
            writeLogFile("cancelTimer" , "","倒计时为空，不需要取消");
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
            writeLogFile("onPreConnect" ,"macAddress : "  + macAddress ,"macAddress为空");
            return;
        }

        Long lastConnectTime = TimeHolder.get().getLastConnectTime();
        if (null != lastConnectTime) {
            Long diff = System.currentTimeMillis() - lastConnectTime;
            if (diff < 5000) {
                if (connectTask == null) {
                    connectTask = new Runnable() {
                        @Override
                        public void run() {
                            onConnect(macAddress);
                            writeLogFile("onPreConnect" ,"macAddress : "  + macAddress ,"低于5秒再次重连");
                        }
                    };
                }
                handler.postDelayed(connectTask,5000 - diff);
            } else {
                onConnect(macAddress);
                writeLogFile("onPreConnect" ,"macAddress : "  + macAddress ,"大于5秒再次重连");
            }
        } else {
            onConnect(macAddress);
            writeLogFile("onPreConnect" ,"macAddress : "  + macAddress ,"打开app首次连接");
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
            writeLogFile("onPreConnect" ,"macAddress : "  + macAddress +"    isScan：" + isScan ,"macAddress为空");
            Log.wtf(TAG, "macAddress为空!");
            return;
        }

        Long lastConnectTime = TimeHolder.get().getLastConnectTime();
        if (null != lastConnectTime) {
            Long diff = System.currentTimeMillis() - lastConnectTime;
            if (diff < 5000) {
                if (connectTask == null) {
                    connectTask = () -> {
                        onConnect(macAddress);
                        writeLogFile("onPreConnect" ,"macAddress : "  + macAddress  +"  isScan :" + isScan,"低于5秒再次重连");
                    };

                }
               handler.postDelayed(connectTask,5000 - diff);
            } else {
                onConnect(macAddress);
                writeLogFile("onPreConnect" ,"macAddress : "  + macAddress  +"  isScan:" + isScan ,"大于5秒再次重连");
            }
        } else {
            onConnect(macAddress);
            writeLogFile("onPreConnect" ,"macAddress : "  + macAddress  +"  isScan:" + isScan ,"打开app再次重连");
        }

    }

    @Override
    public void onConnect(@NonNull String macAddress, Long supplierId) {
        setSupplierId(supplierId);
        onConnect(macAddress);
        writeLogFile("onConnect" ,"macAddress : "  + macAddress  +"  supplierId : " + supplierId ,"真正连接蓝牙");
    }

    boolean scanDevice = false ;  // 是否扫描到设备

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
                    writeLogFile("onConnect" ,"macAddress : "  + macAddress   ,"设备已连接上，取消定时器。");
                }
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "设备连接超时。");
                // 关闭蓝牙连接
                closeBleConnection();
                writeLogFile("onConnect" ,"macAddress : "  + macAddress   ,"设备连接超时");
                if (getMvpView() != null) {
                    getMvpView().post(() -> getMvpView().onError(TradeError.CONNECT_ERROR_5));
                }
                if (!scanDevice){

                }

                recordUseNumber(Type.SCAN , Target.DEVICE,Result.FAILED ,TimeUtils.diffTime(System.currentTimeMillis() ,timeStamps));
            }
        };


        Log.i(TAG, "启动15s定时器......");

        // 初始化扫描时间
        timeStamps = System.currentTimeMillis() ;
        timer.start();

        //  macAddress是服务器返回的deviceNo  ,currentMacAddress是全局变量，是正确的蓝牙设备mac地址
        // 设备连接上存储mac地址供后续读写数据使用
        // 查询是否存在该deviceNo的macAddress
        if (deviceDataManager.getMacAddressByDeviceNo(macAddress) != null) {

            currentMacAddress = deviceDataManager.getMacAddressByDeviceNo(macAddress);
            Log.i(TAG, "缓存中存在macAddress，不需要扫描" + currentMacAddress);
            recordUseNumber(Type.SCAN , Target.DEVICE,Result.SUCCESS ,TimeUtils.diffTime(System.currentTimeMillis() ,timeStamps));
            writeLogFile("onConnect" ,"macAddress : "  + macAddress   ,"缓存中存在macAddress，不需要扫描");
            realConnect(macAddress);
            return;
        }

        // 扫描macAddress
        Log.i(TAG, "开始扫描macAddress");
        bleDataManager.scan(new BluetoothScanResponse() {
            @Override
            public void onScanStarted() {
                Log.d(TAG, "onScanStarted thread" + Thread.currentThread().getName());
                writeLogFile("onConnect" ,"macAddress : "  + macAddress   ,"开始扫描macAddress");
                scanDevice = false ;
            }

            @Override
            public void onDeviceFounded(BluetoothScanResult result) {
                Log.d(TAG, "onDeviceFounded thread" + Thread.currentThread().getName());

                boolean validDevice = isValidDevice(result, macAddress);
                if (!validDevice) {
                    return;
                }

                if (currentMacAddress != null && currentMacAddress.equalsIgnoreCase(result.getAddress())) {
                    Log.i(TAG, "扫描获取macAddress在当前上下文已经存在，无需重复计算。macAddress:" + currentMacAddress);
                    writeLogFile("onConnect" ,"macAddress : "  + macAddress   ,"扫描获取macAddress在当前上下文已经存在，无需重复计算。macAddress:" + currentMacAddress);
                    recordUseNumber(Type.SCAN , Target.DEVICE,Result.SUCCESS ,TimeUtils.diffTime(System.currentTimeMillis() ,timeStamps));
                    bleDataManager.stopScan();
                    scanDevice = true ;
                    return;
                }

                scanDevice = true ;
                currentMacAddress = result.getAddress();
                Log.i(TAG, "扫描获取macAddress成功。macAddress:" + currentMacAddress);
                deviceDataManager.setDeviceNoAndMacAddress(macAddress, currentMacAddress);
                recordUseNumber(Type.SCAN , Target.DEVICE,Result.SUCCESS ,TimeUtils.diffTime(System.currentTimeMillis() ,timeStamps));
                realConnect(macAddress);
                bleDataManager.stopScan();
                writeLogFile("onConnect" ,"macAddress : "  + macAddress   ,"扫描获取macAddress成功。macAddress:" + currentMacAddress);
            }

            @Override
            public void onScanStopped() {
                Log.d(TAG, "onScanStopped thread" + Thread.currentThread().getName());
                writeLogFile("onConnect" ,"macAddress : "  + macAddress   ,"停止扫描"  );
            }

            @Override
            public void onScanCanceled() {
                Log.d(TAG, "onScanCanceled thread" + Thread.currentThread().getName());
                writeLogFile("onConnect" ,"macAddress : "  + macAddress   ,"扫描取消"  );
            }
        });

    }

    private boolean isValidDevice(BluetoothScanResult result, String deviceNo) {
        if (!TextUtils.equals(deviceNo, result.getName())) {
            return false;
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

    private void realConnect(String macAddress) {
        // 正常流程
        // 1、网络请求获取握手指令
        if (null == connectCmd) {
            getConnectCommand(macAddress);
            writeLogFile("realConnect" ,"macAddress : "  + macAddress   ,"connectCmd为空");
        } else {
            writeLogFile("realConnect" ,"macAddress : "  + macAddress   ,"connectCmd不为空:"+connectCmd);
        }
        // 检测当前用户对该设备订单使用状态
        checkOrderStatus(macAddress);



        // 3、创建共享连接q
        Log.d(TAG, "注册连接监听");
        bleDataManager.registerConnectStatusListener(currentMacAddress, status -> {
            // 处理蓝牙连接状态
            switch (status) {
                case BluetoothConstants.STATE_CONNECTED:
                    writeLogFile("ConnectStatusListener" ,"currentMacAddress : "  + currentMacAddress   ,"蓝牙已连接");
                    Log.d(TAG, "[ConnectStatusListener]蓝牙已连接");
                    break;
                case BluetoothConstants.STATE_CONNECTING:
                    writeLogFile("ConnectStatusListener" ,"currentMacAddress : "  + currentMacAddress   ,"蓝牙正在连接");
                    Log.d(TAG, "[ConnectStatusListener]蓝牙正在连接");

                    //  开始连接蓝牙
                    break;
                case BluetoothConstants.STATE_DISCONNECTED:
//                    getMvpView().showMessage("蓝牙已经断开连接...");
                    writeLogFile("ConnectStatusListener" ,"currentMacAddress : "  + currentMacAddress   ,"蓝牙已经断开连接");
                    handleDisConnectError("[ConnectStatusListener]蓝牙已经断开连接");
                    break;
                case BluetoothConstants.STATE_DISCONNECTING:
//                    getMvpView().showMessage("蓝牙正在断开连接...");
                    writeLogFile("ConnectStatusListener" ,"currentMacAddress : "  + currentMacAddress   ,"蓝牙正在断开连接");
                    Log.d(TAG, "[ConnectStatusListener]蓝牙正在断开连接");
                    break;
            }

        });

        // 4、连接设备
        safeWait(300);

//        Log.d(TAG, "开始连接设备 thread " + Thread.currentThread().getName()  + "    id  >>>> " + supplier.getAgreement());
        switch (AgreementVersion.getAgreement(supplier.getAgreement())) {
            case HAONIANHUA:
                writeLogFile("realConnect" , "" ,"开始连接好年华设备"+supplier.getId());
                connectHaoNianHaoDevice();
                break;
            case XINNA:
                writeLogFile("realConnect" , "" ,"开始连接辛纳设备"+supplier.getId());
                connectXinNaDevice();
                break;
        }
    }

    private void connectXinNaDevice() {
        Log.i(TAG, "连接辛纳设备 thread " + Thread.currentThread().getName());
        timeStamps = System.currentTimeMillis() ;
        bleDataManager.connect(currentMacAddress, (code, gatt) -> {
            if (code != BluetoothConstants.CONN_RESPONSE_SUCCESS) {
                writeLogFile("connectXinNaDevice" , "" ,"连接失败currentMacAddress："+currentMacAddress+"  code:"+code);
                handleDisConnectError("辛纳设备连接失败 code: " + code);
                recordUseNumber(Type.CONNECT , Target.DEVICE,Result.FAILED ,TimeUtils.diffTime(System.currentTimeMillis() ,timeStamps));
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
                                writeLogFile("connectXinNaDevice" , "" ,"辛纳写入失败currentMacAddress："+currentMacAddress+"  code:"+code1);
                                handleDisConnectError("辛纳设备写入ENABLE_NOTIFICATION_VALUE失败 code:" + code1);
                                recordUseNumber(Type.CONNECT , Target.DEVICE,Result.FAILED ,TimeUtils.diffTime(System.currentTimeMillis() ,timeStamps));
                                return;
                            }

                            afterBleConnected();
                            bleDataManager.notify(currentMacAddress, UUID.fromString(supplier.getServiceUuid()),
                                    UUID.fromString(supplier.getWriteUuid()), data -> {
                                        if (null != data) {
                                            String result = HexBytesUtils.bytesToHexString(data);
                                            Log.i(TAG, "接收到设备数据" + result + " thread" + Thread.currentThread().getName());
                                            writeLogFile("connectXinNaDevice" , "" ,"接收到设备数据"+result);
                                            processCommandResult(result);
                                        } else {
                                            writeLogFile("connectXinNaDevice" , "" ,"接收到设备数据为空");
                                        }
                                    });
                            });
            }
            else {
                writeLogFile("connectXinNaDevice" , "" ,"辛纳设置notify失败currentMacAddress："+currentMacAddress);
                handleDisConnectError("辛纳设备设置notify失败");
                recordUseNumber(Type.CONNECT , Target.DEVICE,Result.FAILED ,TimeUtils.diffTime(System.currentTimeMillis() ,timeStamps));
            }
        });
    }


    private void afterBleConnected() {
        recordUseNumber(Type.CONNECT , Target.DEVICE,Result.SUCCESS ,TimeUtils.diffTime(System.currentTimeMillis() ,timeStamps));
        if (reconnect) {
            Log.i(TAG, "当前为重连状态");
            if (null == getStep()) {
                Log.i(TAG, "首次连接就连接不上，需要重新下发握手指令:" + connectCmd);
                waitConnectCmdResult();
                Log.wtf(TAG , "   afterBleConnected 首次连接 >>>>>>>>>>  " +currentMacAddress);

                //  向设备下发握手指令
                onWrite(connectCmd);
                reconnect = false; // 重置重连标志
                writeLogFile("afterBleConnected" , "" ,"首次连接就连接不上，需要重新下发握手指令connectCmd："+connectCmd + "  currentMacAddress:"+currentMacAddress);
            } else if (TradeStep.PAY == getStep()) { // 支付页面重连
                // 重新连接成功时不需要再次握手
                // 最新修改，支付页面重连，继续下发握手指令，否则单纯物理连接上会被设备踢掉
                waitConnectCmdResult();
                Log.wtf(TAG , "   afterBleConnected  支付页面重连 >>>>>>>>>>  " +currentMacAddress);
                //  向设备下发握手指令
                onWrite(connectCmd);
                reconnect = false; // 重置重连标志
                writeLogFile("afterBleConnected" , "" ,"支付页面重连connectCmd："+connectCmd + "  currentMacAddress:"+currentMacAddress);
            } else { // 结算页面重连
                Log.i(TAG, "当前为结算页面重连");
                waitOrderCheckResult();
                // 如果查询订单时出现错误，显示连接错误并跳过之后的步骤
                if (checkOrderErrorFlag) {
                    checkOrderErrorFlag = false;
                    writeLogFile("afterBleConnected" , "" ,"查询订单时出现错误，显示连接错误并跳过之后的步骤");
                    return;
                }
                if (null == orderStatus || orderStatus.getStatus() == null) {
                    Log.wtf(TAG, "查不到对应的未结账订单，不应该发生此种状况！！！");
                    writeLogFile("afterBleConnected" , "" ,"查不到对应的未结账订单，不应该发生此种状况！！orderId:"+orderId);
                    // 如果订单ID不为空，直接到订单详情页面
                    if (orderId != 0L) {
                        if (getMvpView() != null) {
                            getMvpView().onFinish(orderId);
                        }
                    } else {
                        setStep(TradeStep.PAY);
                        //  向设备下发握手指令
                        onWrite(connectCmd);
                    }

                } else {
                    if (OrderStatus.getOrderStatus(orderStatus.getStatus()) == OrderStatus.FINISHED) { // 订单已结单
                        Log.i(TAG, "重连后发现订单已被结算，跳转至订单详情页。orderId:" + orderStatus.getOrderId());
                        writeLogFile("afterBleConnected" , "" ,"重连后发现订单已被结算，跳转至订单详情页！orderId:"+ (orderStatus.getOrderId()== null ? -1 : orderStatus.getOrderId()));
                        if (getMvpView() != null) {
                            getMvpView().onFinish(orderStatus.getOrderId()); // 跳转订单详情页
                        }
                    } else { // 未结单
                        // 重连状态下继续下发握手指令
                        // 1、如果设备没有长按结束用水按钮，握手会失败，但连接不会被设备中断，继续下发关阀指令走结账流程即可
                        // 2、如果设备已被长按结束用水按钮，握手会成功，此时需要走预结账->结账流程
                        writeLogFile("afterBleConnected" , "" ,"未结单，重连状态下继续下发握手指令。connectCmd"+connectCmd+", orderId:"+ (orderStatus.getOrderId() == null ? -1 : orderStatus.getOrderId()));
                        Log.i(TAG, String.format("重连后发现订单仍未被结算，继续下发握手指令。command:%s, orderId: %s", connectCmd, orderStatus.getOrderId()));
                        //  向设备下发握手指令
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
                writeLogFile("afterBleConnected" , "" ,"正常连接状态，查询订单时出现错误，显示连接错误并跳过之后的步骤");
                return;
            }

            if (null != orderStatus && null != orderStatus.getStatus() && OrderStatus.getOrderStatus(orderStatus.getStatus()) == OrderStatus.USING) {  // 有订单未计算拿上次连接的握手指令（这里待验证是否有影响）
                if (homePageJump && !orderStatus.isExistsUnsettledOrder()) {
                    writeLogFile("afterBleConnected" , "" ,"首页点击继续用水，且未结账订单已超出指定时间范围，走正常流程，继续下发握手指令。connectCmd："+connectCmd);
                    Log.i(TAG, "首页点击继续用水，且未结账订单已超出指定时间范围，走正常流程，继续下发握手指令。command:" + connectCmd);
                    waitConnectCmdResult();
                    onWrite(connectCmd);
                    //  向设备下发握手指令
                } else {
                    writeLogFile("afterBleConnected" , "" ,"正常连接发现有订单未被结算，继续下发握手指令。connectCmd："+connectCmd+ "， orderId："+(orderStatus.getOrderId() == null ? -1 : orderStatus.getOrderId()));
                    Log.i(TAG, String.format("正常连接发现有订单未被结算，继续下发握手指令。command: %s, orderId:%s", connectCmd, orderStatus.getOrderId()));
                    // orderId = orderStatus.getOrderId();
                    waitConnectCmdResult();
                    onWrite(connectCmd);
                    //  向设备下发握手指令
                    purelyCheckoutFlag = true;
                }
            } else {
                // 握手连接
                Log.i(TAG, "正常连接成功，下发握手指令。command:" + connectCmd);
                waitConnectCmdResult();
                onWrite(connectCmd);

                //  向设备下发握手指令
            }
        }
    }

    private void connectHaoNianHaoDevice() {
        Log.i(TAG, "连接好年华设备 thread " + Thread.currentThread().getName());
        timeStamps = System.currentTimeMillis() ;
        bleDataManager.connect(currentMacAddress, (code, gatt) -> {
            Log.d(TAG, "connect code:" + code + " thread " + Thread.currentThread().getName());
            Log.d(TAG, "ServiceUuid" + supplier.getServiceUuid() + "notifyUuid" + supplier.getNotifyUuid()
                    + "writeUuid" + supplier.getWriteUuid());
            if (code != BluetoothConstants.CONN_RESPONSE_SUCCESS) {
                writeLogFile("connectHaoNianHaoDevice" , "" ,"连接失败currentMacAddress："+currentMacAddress+"  code:"+code);
                handleDisConnectError("连接好年华设备失败 code:" + code);
                recordUseNumber(Type.CONNECT , Target.DEVICE,Result.FAILED ,TimeUtils.diffTime(System.currentTimeMillis() ,timeStamps));
//                uploadLog();
                return;
            }
            if (gatt == null || gatt.getService(UUID.fromString(supplier.getServiceUuid())) == null) {
                writeLogFile("connectHaoNianHaoDevice" , "" ,"连接好年华设备失败 code:："+ code + "获取不到service uuid:" + supplier.getServiceUuid());
                handleDisConnectError("连接好年华设备失败 code:" + code + "获取不到service uuid:" + supplier.getServiceUuid());
//                uploadLog();
                recordUseNumber(Type.CONNECT , Target.DEVICE,Result.FAILED ,TimeUtils.diffTime(System.currentTimeMillis() ,timeStamps));
                return;
            }
            for (BluetoothGattCharacteristic characteristic : gatt.getService(UUID.fromString(supplier.getServiceUuid())).getCharacteristics()) {
                if (characteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_NOTIFY) {
                    if (bleDataManager.setNotify(currentMacAddress, UUID.fromString(supplier.getServiceUuid()),
                            characteristic.getUuid(), true)) {
                        Log.d(TAG, "设置notify成功 thread " + Thread.currentThread().getName()  + " serviceUUID>>>> " + supplier.getServiceUuid() +"  characteristic>>>>>" + characteristic.getUuid());
                        bleDataManager.writeDescriptor(currentMacAddress, UUID.fromString(supplier.getServiceUuid()),
                                characteristic.getUuid(), UUID.fromString(supplier.getNotifyUuid()),
                                BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE, code1 -> {
                                    Log.d(TAG, "写入ENABLE_NOTIFICATION_VALUE code" + code1 + " thread " + Thread.currentThread().getName());
                                    if (code1 != BluetoothConstants.GATT_SUCCESS) {
                                        writeLogFile("connectHaoNianHaoDevice" , "" ,"辛纳写入失败currentMacAddress："+currentMacAddress+"  code:"+code1);
                                        handleDisConnectError("好年华设备写入ENABLE_NOTIFICATION_VALUE失败 code:" + code1);
//                                        uploadLog();
                                        recordUseNumber(Type.CONNECT , Target.DEVICE,Result.FAILED ,TimeUtils.diffTime(System.currentTimeMillis() ,timeStamps));
                                        return;
                                    }

                                    bleDataManager.notify(currentMacAddress, UUID.fromString(supplier.getServiceUuid()),
                                            characteristic.getUuid(), data -> {
                                                if (null != data) {
                                                    String result = HexBytesUtils.bytesToHexString(data);
                                                    Log.i(TAG, "接收到设备数据" + result + " thread " + Thread.currentThread().getName());
                                                    writeLogFile("connectHaoNianHaoDevice" , "" ,"接收到设备数据"+result);
                                                    processCommandResult(result);
                                                } else {
                                                    writeLogFile("connectHaoNianHaoDevice" , "" ,"接收到设备数据为空");
//                                                    uploadLog();
                                                }

                                            });
                                    afterBleConnected();
                                });
                    } else {
                        recordUseNumber(Type.CONNECT , Target.DEVICE,Result.FAILED ,TimeUtils.diffTime(System.currentTimeMillis() ,timeStamps));
                        Log.d(TAG, "设置notify失败 thread " + Thread.currentThread().getName());
                        writeLogFile("connectHaoNianHaoDevice" , "" ,"辛纳好年华notify失败currentMacAddress："+currentMacAddress);
                        handleDisConnectError("设置notify失败");
//                        uploadLog();
                    }
                    return;
                }
            }
        });
    }

    @Override
    public void onReconnect(@NonNull String macAddress) {
        writeLogFile("onReconnect" , "macAddress：" + macAddress,"");
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
        Log.wtf(TAG , "   onWrite >>>>>>>>>>  " +currentMacAddress);
        deviceTimeStamps = System.currentTimeMillis();
        if (bleDataManager.getConnectStatus(currentMacAddress) != BluetoothConstants.STATE_CONNECTED) {
            writeLogFile("onWrite" , "command：" + command,"蓝牙未连接status："+ bleDataManager.getConnectStatus(currentMacAddress));

            if (getMvpView() != null) {
                getMvpView().post(() -> getMvpView().onError(TradeError.CONNECT_ERROR_1));
            }
            return;
        }

        if (TextUtils.isEmpty(command)) {
            writeLogFile("onWrite" , "command：null","待指令为空");
            if (getMvpView() != null) {
                getMvpView().post(() -> getMvpView().onError(TradeError.CONNECT_ERROR_4));
            }
            return;
        }
        byte[] commandBytes = HexBytesUtils.hexStr2Bytes(command);
        bleDataManager.writeNoRsp(currentMacAddress, UUID.fromString(supplier.getServiceUuid()),
                UUID.fromString(supplier.getWriteUuid()), commandBytes, code -> {
                    if (code == BluetoothConstants.GATT_OTHER_FAILURE) {
                        writeLogFile("onWrite" , "command：" + command,"指令写入失败  code："+code);
                        handleWriteError(command);
                    } else if (code == BluetoothConstants.GATT_SUCCESS) {
                        writeLogFile("onWrite" , "command：" + command,"指令写入成功");
                        Log.d(TAG, "发送指令成功 command: " + command + " thread " + Thread.currentThread().getName());
                    }
                });
    }

    private void handleWriteError(String command) {
        // 结算找零时写入设备失败
        if (TextUtils.equals(command, checkoutCmd)) {
            if (getMvpView() != null) {
                getMvpView().post(() -> getMvpView().onError(TradeError.CONNECT_ERROR_2));
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
        writeLogFile("handleDisConnectError" , "disconnectReason：" + disconnectReason,"");
        if (handleBleClose.compareAndSet(false, true)) {
            Log.d(TAG, disconnectReason + " thread " + Thread.currentThread().getName());

            // 跳转至连接失败页面
            if (getMvpView() != null) {
                getMvpView().post(() -> getMvpView().onError(TradeError.CONNECT_ERROR_1));
            }
            handleBleClose.set(false);
        }
    }

    @Override
    public void onDisConnect() {
        writeLogFile("onDisConnect" , "" ,"");
        Log.d(TAG, "onDisConnect");
        if (connectTask != null) {
            handler.removeCallbacks(connectTask);
        }

        if (null != timer) {
            timer.cancel();
        }
        closeBleConnection();
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

    // 网络请求获取下发费率指令，若存在则直接先对设备进行费率更新
    @Override
    public void onUpdateDeviceRate(@Nullable String macAddress) {
        UpdateDeviceRateCommandReqDTO reqDTO = new UpdateDeviceRateCommandReqDTO();
        reqDTO.setMacAddress(macAddress);
        addObserver(deviceDataManager.getUpdateDeviceRateCommand(reqDTO), new NetworkObserver<ApiResult<UpdateDeviceRateCommandRespDTO>>(false) {
            @Override
            public void onReady(ApiResult<UpdateDeviceRateCommandRespDTO> result) {
                if (null == result.getError()) /*获取更新费率指令*/{
                    String updateDeviceRateCmd = result.getData().getConfigCommand();
                    if (!TextUtils.isEmpty(updateDeviceRateCmd)) /*指令存在，需要进行费率更新*/{
                        writeLogFile("onUpdateDeviceRate" , "macAddress："+ macAddress ,"指令存在，需要进行费率更新");
                        onWrite(updateDeviceRateCmd);
                    } else /*指令不存在，直接进行预支付开阀使用*/{
                        getMvpView().realPay();
                        writeLogFile("onUpdateDeviceRate" , "macAddress："+ macAddress ,"指令不存在，直接进行预支付开阀使用");
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                //异常情况处理，容错处理，进行握手
                writeLogFile("onUpdateDeviceRate" , "macAddress："+ macAddress ,"异常情况处理，容错处理，进行握手");
                getMvpView().realPay();
            }
        }, Schedulers.io());
    }

    // 网络请求获取握手连接指令
    private void getConnectCommand(String macAddress) {

        // 获取握手指令开始时间
        serviceTimeStamps = System.currentTimeMillis() ;
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
                        recordUseNumber(Type.SHAKE_HANDS_DEVICE , Target.SERVER,Result.SUCCESS ,TimeUtils.diffTime(System.currentTimeMillis() ,serviceTimeStamps));
                    }
                    synchronized (connectCmdLock) {
                        connectCmd = result.getData().getConnectCmd();
                        Log.i(TAG, "获取握手指令成功。command:" + connectCmd);
                        connectCmdLock.notifyAll();
                    }
                    writeLogFile("getConnectCommand" , "macAddress："+ macAddress ,"获取握手指令成功。command:"+ connectCmd);

                } else {
                    writeLogFile("getConnectCommand" , "macAddress："+ macAddress ,"服务器返回,获取指令失败code"+ result.getError().getCode());

                    if (result.getError().getCode() == BizError.DEVICE_BREAKDOWN.getCode()) {
                        if (getMvpView() != null) {
                            getMvpView().post(() -> getMvpView().onError(TradeError.DEVICE_BROKEN_3));
                        }
                    } else {
                        Log.wtf(TAG, "服务器返回,获取开阀指令失败");
                        if (getMvpView() != null) {
                            getMvpView().post(() -> getMvpView().onError(TradeError.SYSTEM_ERROR));
                        }
                    }

                    recordUseNumber(Type.SHAKE_HANDS_DEVICE , Target.SERVER,Result.FAILED ,TimeUtils.diffTime(System.currentTimeMillis() ,serviceTimeStamps));
//                    uploadLog();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.wtf(TAG, "服务器未返回,获取开阀指令失败");
                writeLogFile("getConnectCommand" , "macAddress："+ macAddress ,"服务器返回,获取指令失败");
                if (getMvpView() != null) {
                    getMvpView().post(() -> getMvpView().onError(TradeError.CONNECT_ERROR_3));
                }
//                uploadLog();

                recordUseNumber(Type.SHAKE_HANDS_DEVICE , Target.SERVER,Result.FAILED ,TimeUtils.diffTime(System.currentTimeMillis() ,serviceTimeStamps));
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
                        writeLogFile("checkOrderStatus" ,"macAddress : "  + macAddress   ,"获取订单状态成功。orderStatus:" + orderStatus  );
                    }
                } else {
                    Log.wtf(TAG, "服务器返回,获取订单状态失败");
                    if (getMvpView() != null) {
                        getMvpView().post(() -> getMvpView().onError(TradeError.SYSTEM_ERROR));
                    }
                    writeLogFile("checkOrderStatus" ,"macAddress : "  + macAddress   ,"获取订单状态失败" + result.getError().getDebugMessage()   );
//                    uploadLog();
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
                writeLogFile("checkOrderStatus" ,"macAddress : "  + macAddress   ,"获取订单状态失败:" +e.getMessage()    );
                if (getMvpView() != null) {
                    getMvpView().post(() -> getMvpView().onError(TradeError.CONNECT_ERROR_3));
                }
                synchronized (orderStatusLock) {
                    checkOrderErrorFlag = true;
                    orderStatusLock.notifyAll();
                }
//                uploadLog();
            }
        }, Schedulers.io());
    }

    /**
     * 记录设备返回数据的状态
     * @param result
     */
    private void  recordCommandResult(String result){
        if (TextUtils.isEmpty(result))  return ;
        try{
            String prefix = result.substring(0 , 4);
            if (TextUtils.equals(Command.CONNECT.getRespPrefix() , prefix)){
                type = Type.CONNECT ;
            }else if (TextUtils.equals(Command.PRE_CHECK.getRespPrefix() ,prefix)){
                type = Type.PRE_CHECK ;
            }else if (TextUtils.equals(Command.CHECK_OUT.getRespPrefix() ,prefix)){
                type = Type.CHECKOUT ;
            }else if (TextUtils.equals(Command.OPEN_VALVE.getRespPrefix() , prefix)){
                type = Type.OPEN ;
            }else if (TextUtils.equals(Command.CLOSE_VALVE.getRespPrefix() , prefix)){
                type = Type.CLOSE ;
            }else if (TextUtils.equals(Command.UPDATE_DEVICE_RATE.getRespPrefix() ,prefix)){
                type = Type.UPDATE_RATE ;
            }
            recordUseNumber(type,Target.DEVICE ,Result.SUCCESS ,TimeUtils.diffTime(System.currentTimeMillis() ,deviceTimeStamps));
        }catch (Exception e){
            android.util.Log.e(TAG, "recordCommandResult: " + e.getMessage() );
        }


    }



    // 网络请求处理设备响应结果
    private void processCommandResult(String result) {
        recordCommandResult(result);
        try {
            String prefix = result.substring(0, 4);
            if (TextUtils.equals(Command.CLOSE_VALVE.getRespPrefix(), prefix)) {

                setStep(TradeStep.CLOSE_VALVE);
            } else if (TextUtils.equals(Command.OPEN_VALVE.getRespPrefix(), prefix)) {
                // 存储设备响应结果
                saveDeviceResult(result, orderId);
            }
            String prefixAgrement2 = result.substring(0, 6);

            if (TextUtils.equals("140804",prefixAgrement2)){
                setStep(TradeStep.CLOSE_VALVE);

            } else if (TextUtils.equals("140803",prefixAgrement2)){
                saveDeviceResult(result ,orderId);
            }
            writeLogFile("processCommandResult" ,"result : "  + result   ,""   );
        } catch (Exception e) {
            Log.wtf(TAG, "获取设备响应结果前缀失败");
            writeLogFile("processCommandResult" ,"result : "  + result   ,"获取设备响应结果前缀失败"   );
            if (getMvpView() != null) {
                getMvpView().onError(TradeError.CONNECT_ERROR_4);
            }
//            uploadLog();
        }

        Log.wtf(TAG , "processCommandResult>>>>>" + result  + "   " + deviceNo);
        serviceTimeStamps = System.currentTimeMillis() ;
        CmdResultReqDTO reqDTO = new CmdResultReqDTO();
        reqDTO.setData(result);
        reqDTO.setMacAddress(deviceNo);
        addObserver(deviceDataManager.processCmdResult(reqDTO), new NetworkObserver<ApiResult<CmdResultRespDTO>>(false) {
            @Override
            public void onReady(ApiResult<CmdResultRespDTO> result1) {
                // 存储deviceToken
                if (result1.getData() != null && result1.getData().getMacAddress() != null
                        && result1.getData().getDeviceToken() != null) {
                    deviceDataManager.setDeviceToken(result1.getData().getMacAddress(), result1.getData().getDeviceToken());
                    Log.i(TAG, "收到deviceToken：" + result1.getData().getDeviceToken());
                }
                writeLogFile("processCommandResult" ,"result : "  + result   ,"通知主线程更新数据 ：" +  result1.getData()   );
                Log.i(TAG, "通知主线程更新数据。" + result1.getData());
                handleResult(result1);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (getStep() == TradeStep.CLOSE_VALVE) {
                    setStep(TradeStep.SETTLE);
                }
                if (getMvpView() != null) {
                    getMvpView().post(() -> getMvpView().onError(TradeError.CONNECT_ERROR_3));
                }
                writeLogFile("processCommandResult" ,"result : "  + result   ," 服务器获取指令失败 ："  + e.getMessage()    );
//                uploadLog();
                recordUseNumber(type, Target.SERVER ,Result.FAILED ,TimeUtils.diffTime(System.currentTimeMillis() , serviceTimeStamps));
            }
        }, AndroidSchedulers.mainThread());
    }

    private void handleResult(ApiResult<CmdResultRespDTO> result) {

        Log.i(TAG, "主线程开始处理指令响应结果");
        if (null == result.getError()) {
            // 下一步执行指令
            String nextCommand = result.getData().getNextCommand();
            if (result.getData().getSrcCommandType() == null) {
                Log.wtf(TAG, "服务器未返回ScrCommandType");
                closeBleConnection();
                if (getMvpView() != null) {
                    getMvpView().onError(TradeError.SYSTEM_ERROR);
                }
                writeLogFile("handleResult" ,"result : "  + result.getData()   ," 服务器未返回ScrCommandType"    );
//                uploadLog();
                return;
            }
            switch (Command.getCommand(result.getData().getSrcCommandType())) {
                case UPDATE_DEVICE_RATE:
                    getMvpView().realPay();
                    writeLogFile("handleResult" ,"result : "  + result.getData()   ," 更新费率"    );
                    recordUseNumber(Type.UPDATE_RATE ,Target.SERVER ,Result.SUCCESS ,TimeUtils.diffTime(System.currentTimeMillis() ,serviceTimeStamps));
                    break;
                case CONNECT:
                    // 如果用户本人在三小时之内再次连接该设备，需要进入第二步账单结算页面
                    // 表明用户是正在使用状态
                    recordUseNumber(Type.SHAKE_HANDS_DEVICE,Target.SERVER ,Result.SUCCESS ,TimeUtils.diffTime(System.currentTimeMillis() ,serviceTimeStamps));
                    if (!reconnect && null != orderStatus && null != orderStatus.getStatus() && OrderStatus.getOrderStatus(orderStatus.getStatus()) == OrderStatus.USING) {
                        // 记录预结账指令，此时阀门已经被长按关闭，但是订单没有被其它用户带回
                        Log.i(TAG, "用户在该设备上存在未结账订单，直接跳转至结算页面，获取到预结账指令。command:" + nextCommand);
                        if (orderStatus.isExistsUnsettledOrder() || !homePageJump) { // 未结算订单在指定时间范围内
                            Log.i(TAG, "未结算订单在指定时间范围内或者由未结账单页面跳转过来，显示结算页面");
                            reopenNextCmd = nextCommand;
                            if (getMvpView() != null) {
                                getMvpView().onConnectSuccess(TradeStep.SETTLE, orderStatus);
                            }
                            writeLogFile("handleResult" ,"result : "  + result.getData()   + " ， 原指令类型： connect "  ,"    未结算订单在指定时间范围内或者由未结账单页面跳转过来，显示结算页面  ；  下一步指令 "   +  reopenNextCmd  );
                        } else {
                            Log.i(TAG, "未结算订单已经超出指定时间范围，继续下发预结账指令，走正常流程");
                            precheckCmd = nextCommand;
                            Log.w(TAG , "CONNECT  未结账订单超过时间>>>>>>>"+currentMacAddress);
                            onWrite(precheckCmd);
                            writeLogFile("handleResult" ,"result : "  + result.getData() + " ， 原指令类型： connect "  ," 未结算订单已经超出指定时间范围，继续下发预结账指令，走正常流程  ；  下一步指令 "   +  precheckCmd  );
                        }
                        return;
                    }
                    // 存在未结账订单，直接后台预结账处理
                    // 如果存在未结账订单，需要先结算旧账单
                    if (null != result.getData().getNextCommand()) {  //  != null   表明存在未结账订单
                        if (result.getData().getNextCommandType() == Command.CLOSE_VALVE.getType()) {
                            //当前人的关阀指令恢复
                            precheckFlag = false;
                            closeCmd = nextCommand;
                            saveCloseCmd(closeCmd, orderId);
                            writeLogFile("handleResult" ,"result : "  + result.getData()  + " ， 原指令类型： connect "  ," 当前人的关阀指令恢复  ；  下一步指令 "   +  closeCmd  );
                        }
                        else if (result.getData().getNextCommandType() == Command.OTHER_CLOSE_VALVE.getType()) {
                            //他人主动对当前用户进行关阀结账
                            precheckFlag = true;
                            closeCmd = nextCommand;
                            onWrite(closeCmd);
                            writeLogFile("handleResult" ,"result : "  + result.getData()  + " ， 原指令类型： connect "  ," 他人主动对当前用户进行关阀结账  ；  下一步指令 "   +  closeCmd  );
                            break;
                        } else {
                            precheckCmd = nextCommand;
                            writeLogFile("handleResult" ,"result : "  + result.getData()  + " ， 原指令类型： connect "  ,"nextCommand："+nextCommand);
                        }
                        if (!reconnect) { // 正常流程
                            // 下发预结账指令
                            Log.i(TAG, "正常流程，设备上存在未结账订单，获取到预结账指令。command:" + nextCommand);

                            Log.w(TAG , "CONNECT  正常流程，存在未结账订单>>>>>>>"+currentMacAddress);
                            onWrite(precheckCmd);
                            writeLogFile("handleResult" ,"result : "  + result.getData()  + " ， 原指令类型： connect "  ,"正常流程，设备上存在未结账订单，获取到预结账指令。command:" + nextCommand   );
                        } else {
                            //  自己的未结账订单，显示未结账状态
                            Log.i(TAG, "用户在结算页面重新连接成功，设备上存在未结账订单，获取到预结账指令。command:" + nextCommand);
                            if (getMvpView() != null) {
                                getMvpView().onReconnectSuccess(orderStatus);
                            }
                            reconnectNextCmd = precheckCmd;
                            writeLogFile("handleResult" ,"result : "  + result.getData()   + " ， 原指令类型： connect " ,"自己的未结账订单，显示未结账状态。command:" + nextCommand   );
                        }
                    } else {
                        // 握手连接成功，进入正常用水流程
                        Log.i(TAG, "正常流程，设备上不存在未结账订单。");
                        if (getMvpView() != null) {
                            getMvpView().onConnectSuccess(TradeStep.PAY);
                        }
                        writeLogFile("handleResult" ,"result : "  + result.getData()  + " ， 原指令类型： connect "  ,"握手连接成功，进入正常用水流程。command:" + nextCommand   );
                    }
                    break;
                case OPEN_VALVE:
                    Log.i(TAG, "正常流程，获取到关阀指令。command:" + nextCommand);
                    closeCmd = nextCommand;
                    saveCloseCmd(closeCmd, orderId);
                    if (getMvpView() != null) {
                        getMvpView().onOpen();
                    }
                    writeLogFile("handleResult" ,"result : "  + result.getData() + " ， 原指令类型： OPEN_VALVE "    ,"正常流程，获取到关阀指令。command:" + nextCommand   );
                    recordUseNumber(Type.OPEN ,Target.SERVER,Result.SUCCESS ,TimeUtils.diffTime(System.currentTimeMillis() ,serviceTimeStamps));
                    break;
                case CLOSE_VALVE:
                    Log.i(TAG, "正常流程，获取到结账指令。command:" + nextCommand);
                    checkoutCmd = nextCommand;
                    // 下发结账指令

                    Log.w(TAG , "CLOSE_VALVE >>>>>>>"+currentMacAddress);
                    onWrite(checkoutCmd);
                    writeLogFile("handleResult" ,"result : "  + result.getData() + " ， 原指令类型： CLOSE_VALVE "    ,"正常流程，获取到关阀指令。command:" + nextCommand   );
                    recordUseNumber(Type.CLOSE,Target.SERVER,Result.SUCCESS,TimeUtils.diffTime(System.currentTimeMillis() ,serviceTimeStamps));
                    break;
                case CHECK_OUT:
                    if (purelyCheckoutFlag) {
                        // 纯结账操作，直接跳转至第二步结账，结账完毕跳转账单详情页面
                        writeLogFile("handleResult" ,"result : "  + result.getData() + " ， 原指令类型： CHECK_OUT "    ,"纯结账操作，直接跳转至第二步结账，结账完毕跳转账单详情页面"    );
                        Log.i(TAG, "结账完成");
                        if (getMvpView() != null) {
                            getMvpView().onFinish(orderId);

                        }

                        recordUseNumber(Type.CHECKOUT,Target.SERVER,Result.SUCCESS ,TimeUtils.diffTime(System.currentTimeMillis() ,serviceTimeStamps));

                    } else if (precheckFlag) {
                        // 当前为预结账状态，结账完毕后关闭更改标识

                        recordUseNumber(Type.PRE_CHECK,Target.SERVER ,Result.SUCCESS,TimeUtils.diffTime(System.currentTimeMillis() ,serviceTimeStamps));
                        precheckFlag = false;
                        if (reconnect) {
                            // 重连状态下，走预结账->结账流程时，是本人主动结账，跳转账单详情页
                            Log.i(TAG, "结账完成");
                            writeLogFile("handleResult" ,"result : "  + result.getData() + " ， 原指令类型： CHECK_OUT "    ,"当前为预结账状态 ,重连状态下，走预结账->结账流程时，是本人主动结账，跳转账单详情页"    );
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
                            Log.wtf(TAG ,"CHECK_OUT  >>>>>>>>>>"+currentMacAddress);
                            onWrite(connectCmd);

                            //  向设备下发握手指令
                            timeStamps = System.currentTimeMillis();
                            writeLogFile("handleResult" ,"result : "  + result.getData() + " ， 原指令类型： CHECK_OUT "    ,"非重连状态下，是主动帮别人结账，此时用户还未进入用水流程，需要再次握手，否则会报设备使用次数不对 .connectCmd : " + connectCmd   );
                        }
                    } else {
                        // 当前为非预结账状态，结账完毕后跳转账单详情页
                        writeLogFile("handleResult" ,"result : "  + result.getData() + " ， 原指令类型： CHECK_OUT "    ,"当前为非预结账状态，结账完毕后跳转账单详情页"    );
                        Log.i(TAG, "结账完成");
                        if (getMvpView() != null) {
                            getMvpView().onFinish(orderId);
                        }
                        recordUseNumber(Type.CHECKOUT,Target.SERVER ,Result.SUCCESS ,TimeUtils.diffTime(System.currentTimeMillis() ,serviceTimeStamps));
                    }
                    break;
                case PRE_CHECK:
                    Log.wtf(TAG ,"PRE_CHECK  >>>>>>>>>>"+currentMacAddress);
                    Log.i(TAG, "正常流程，获取到结账指令。command:" + nextCommand);
                    checkoutCmd = nextCommand;
                    // 标识当前状态为预结账状态
                    precheckFlag = true;
                    // 下发结账指令
                    Log.wtf(TAG ,"PRE_CHECK  >>>>>>>>>>"+currentMacAddress);
                    onWrite(checkoutCmd);
                    writeLogFile("handleResult" ,"result : "  + result.getData() + " ， 原指令类型： PRE_CHECK "    ,"正常流程，获取到结账指令。command:" + nextCommand    );
                    recordUseNumber(Type.PRE_CHECK ,Target.SERVER ,Result.SUCCESS ,TimeUtils.diffTime(System.currentTimeMillis() ,serviceTimeStamps));
                    break;
                case OTHER_CLOSE_VALVE:
                    Log.wtf(TAG, "其他人的关阀指令");
                    writeLogFile("handleResult" ,"result : "  + result.getData() + " ， 原指令类型： OTHER_CLOSE_VALVE "    ,"其他人的关阀指令"     );
                    break;
                case UNKNOWN:
                    Log.wtf(TAG, "服务器返回未知指令");
                    writeLogFile("handleResult" ,"result : "  + result.getData() + " ， 原指令类型： UNKNOWN "    ,"服务器返回未知指令"     );
                    break;

            }
        } else {
            Log.e(TAG, String.format("处理异常返回结果。code:%s, msg:%s", result.getError().getCode(), result.getError().getDisplayMessage()));
            Integer rateCmdType = result.getError().getBleCmdType();
            if (null != rateCmdType) {
                //
                if (Command.UPDATE_DEVICE_RATE == Command.getCommand(rateCmdType)) {
                    //更新费率的时候异常直接跳过该流程
                    getMvpView().realPay();
                    writeLogFile("handleResult" ,"result : "  + result.getError() + " ， 服务器返回异常情况： "    ,"更新费率的时候异常直接跳过该流程"     );
                    recordUseNumber(Type.UPDATE_RATE ,Target.SERVER ,Result.SUCCESS ,TimeUtils.diffTime(System.currentTimeMillis() ,serviceTimeStamps));
                    return;
                }
            }

                    // 如果在结账时服务器返回异常则状态改为SETTLE
            if (getStep() == TradeStep.CLOSE_VALVE) {
                setStep(TradeStep.SETTLE);
                writeLogFile("handleResult" ,"result : "  + result.getError() + " ， 服务器返回异常情况： "    ,"如果在结账时服务器返回异常状态  orderId : "  + orderId    );
                recordUseNumber(Type.CLOSE ,Target.SERVER ,Result.SUCCESS ,TimeUtils.diffTime(System.currentTimeMillis() ,serviceTimeStamps));
            }
            if (result.getError().getCode() == BleErrorType.BLE_DEVICE_BUSY.getCode()) {
                Log.e(TAG, "设备正在被使用");
                // 如果是重连状态下，此时可以直接下发关阀指令
                if (reconnect) {
                    writeLogFile("handleResult" ,"result : "  + result.getError()+ " ， 服务器返回异常情况： "    ,"重连状态下，此时可以直接下发关阀指令 ");
                    // 显示重连成功
                    if (getMvpView() != null) {
                        getMvpView().onReconnectSuccess(orderStatus);
                    }

                    if (checkCloseCmd()) {
                        reconnectNextCmd = closeCmd;
                        writeLogFile("handleResult" ,"result : "  + result.getError() + " ， 服务器返回异常情况： "    ,"获取管阀指令   reconnectNextCmd："  + reconnectNextCmd);
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

                        writeLogFile("handleResult" ,"result : "  + result.getError() + " ， 服务器返回异常情况： "    ,"处理未结账订单，此时阀门处于打开状态，下发关阀指令即可："  + reconnectNextCmd);

                    } else {
                        // 提示用户设备已被其它用户使用
                        closeBleConnection();
                        if (getMvpView() != null) {
                            getMvpView().onError(TradeError.DEVICE_BUSY);
                        }

                        writeLogFile("handleResult" ,"result : "  + result.getError() + " ， 服务器返回异常情况： "    ,"提示用户设备已被其它用户使用：" );
                    }
                }
            } else if (result.getError().getCode() == BleErrorType.BLE_UNKNOWN_ERROR.getCode()) {
                closeBleConnection();
                if (getMvpView() != null) {
                    getMvpView().onError(TradeError.DEVICE_BROKEN_2);
                }
                writeLogFile("handleResult" ,"result : "  + result.getError() + " ， 服务器返回异常情况： "    ,"未知错误：" );
//                uploadLog();
            } else if (result.getError().getCode() == BleErrorType.BLE_CMD_RESULT_ERROR.getCode()) {
                Log.i(TAG, "设备未完全开启");
                if (getMvpView() != null) {
                    getMvpView().onError(TradeError.CONNECT_ERROR_1);
                }
//                uploadLog();
            }
            Integer cmdType = result.getError().getBleCmdType();
            if (null != cmdType) {
                // 确认支付时异常
                if (Command.OPEN_VALVE == Command.getCommand(cmdType)) {
                    Log.wtf(TAG, "设备开阀异常");
                    closeBleConnection();
                    if (getMvpView() != null) {
                        getMvpView().onError(TradeError.DEVICE_BROKEN_2);
                    }
                    writeLogFile("handleResult" ,"result : "  + result.getError() + " ， 服务器返回异常情况： "    ,"设备开阀异常：" );
//                    uploadLog();
                } else if (Command.CLOSE_VALVE == Command.getCommand(cmdType) || Command.PRE_CHECK == Command.getCommand(cmdType) || Command.CHECK_OUT == Command.getCommand(cmdType)) {
                    Log.wtf(TAG, "订单结算异常");
                    closeBleConnection();
                    // 结算时异常
                    if (getMvpView() != null) {
                        getMvpView().onError(TradeError.CONNECT_ERROR_2);
                    }
                    writeLogFile("handleResult", "result : " + result.getError()+ " ， 服务器返回异常情况： ", "订单结算异常：" + orderId);


                }
            } else {
                // 系统异常
                Log.wtf(TAG, String.format("服务器后台出错, errorCode:%s, errorMsg:%s", result.getError().getCode(), result.getError().getDisplayMessage()));
                closeBleConnection();
                if (getMvpView() != null) {
                    getMvpView().onError(TradeError.SYSTEM_ERROR);

                }
                writeLogFile("handleResult", "result : " + result.getError() + " ， 服务器返回异常情况： ", "订单结算异常：" + orderId);
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
            writeLogFile("checkCloseCmd" ,""    ,"获取管阀指令   closeCmd：为空" );
            Log.e(TAG, "重连状态关阀指令为空, 从缓存中获取关阀指令");
            String savedCloseCmd = getCloseCmd(orderId);
            if (TextUtils.isEmpty(savedCloseCmd)) {
                writeLogFile("checkCloseCmd" ,""    ,"  从缓存中获取关阀指令为空，获取上次设备响应重新向服务器请求关阀指令：" + orderId );
                Log.e(TAG, "从缓存中获取关阀指令为空，获取上次设备响应重新向服务器请求关阀指令");
                String savedDeviceResult = getDeviceResult(orderId);
                if (TextUtils.isEmpty(savedDeviceResult)) {
                    writeLogFile("checkCloseCmd" ,""    ,"  从缓存中获取关阀指令为空：" + orderId );
                    Log.e(TAG, "从缓存中获取设备响应为空");
                    if (getMvpView() != null) {
                        getMvpView().onError(TradeError.CONNECT_ERROR_2);
                    }
                } else {
                    writeLogFile("checkCloseCmd" ,""    ,"  网络请求：savedDeviceResult： "  + savedDeviceResult  );
                    processCommandResult(savedDeviceResult);
                }
                return false;
            } else {
                closeCmd = savedCloseCmd;
                writeLogFile("checkCloseCmd" ,""    ," 从缓存中获取关阀指令： "  + closeCmd  );
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

        writeLogFile("saveDeviceResult" ,"result :" + result +",orderId:" + orderId,"" );
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
            writeLogFile("getDeviceResult" ,"orderId:" + orderId    ,"  从缓存中获取关阀指令为空：" );
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

        writeLogFile("saveCloseCmd" ,"closeCmd:  " + closeCmd   +",orderId :" +orderId  ,"" );
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
            writeLogFile("getCloseCmd" ,"orderId:" + orderId    ,"  从缓存中获取关阀指令为空，获取上次设备响应重新向服务器请求关阀指令：" + orderId );
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
            writeLogFile("onPay" ,"prepay:  " + prepay   +",bonusId :" +bonusId  ,"" );
            return;
        }

        PayReqDTO reqDTO = new PayReqDTO();
        reqDTO.setMacAddress(deviceNo);
        reqDTO.setBonusId(bonusId);
        reqDTO.setPrepay(prepay);
        writeLogFile("pay" ,"prepay:  " + prepay   +",bonusId :" +bonusId  +",deviceNo:"  + deviceNo,"" );
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
                    Log.i(TAG, "开始下发开阀指令。command>>>>>>>>" + openCmd);



                    writeLogFile("pay" ,"prepay:  " + prepay   +",bonusId :" +bonusId  +",deviceNo:"  + deviceNo,"支付创建订单成功。orderId：" + orderId  +",\"开始下发开阀指令。command：\" + openCmd");
                    onWrite(openCmd);
                } else {
                    Log.wtf(TAG, "支付创建订单失败。");
                    if (getMvpView() != null) {
                        getMvpView().post(() -> getMvpView().onError(TradeError.DEVICE_BROKEN_2));
                    }
                    writeLogFile("pay" ,"prepay:  " + prepay   +",bonusId :" +bonusId  +",deviceNo:"  + deviceNo,"支付创建订单失败：" + result.getError().getDebugMessage());
//                    uploadLog();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (getMvpView() != null) {
                    getMvpView().post(() -> getMvpView().onError(TradeError.CONNECT_ERROR_3));
                }

                writeLogFile("pay" ,"prepay:  " + prepay   +",bonusId :" +bonusId  +",deviceNo:"  + deviceNo,"支付创建订单失败：" + e.getMessage());
//                uploadLog();
            }
        }, Schedulers.io());
    }

    @Override
    public void onClose() {
        Log.i(TAG, "开始关阀");
        // 校验网络
        if (getMvpView() != null && !getMvpView().isNetworkAvailable()) {
            Log.wtf(TAG, "网络不可用");
            getMvpView().onError(TradeError.CONNECT_ERROR_3);
            writeLogFile("onClose" ,"","网络不可用");
            return;
        }

        if (reconnect) {
            // 重连状态下指令有两种 1 - 关阀 2 - 预结账
            Log.i(TAG, "重连状态，开始关阀" + reconnectNextCmd);
            if (reconnectNextCmd != null) {
                writeLogFile("onClose" ,"","重连状态，开始关阀 .reconnectNextCmd: " + reconnectNextCmd);
                onWrite(reconnectNextCmd);
            } else if (closeCmd != null) {
                writeLogFile("onClose" ,"","重连状态，开始关阀 .closeCmd: " + closeCmd);
                onWrite(closeCmd);
            } else if (precheckCmd != null) {
                writeLogFile("onClose" ,"","重连状态，开始关阀 .precheckCmd: " + precheckCmd);
                onWrite(precheckCmd);
            }
        } else {
            Log.i(TAG, "非重连状态，开始关阀");
            if (purelyCheckoutFlag) { // 直接跳转至第二步结算
                if (null == reopenNextCmd) { // 用户卸载掉app时取回的关阀指令为空
                    writeLogFile("onClose" ,"","用户卸载掉app时取回的关阀指令为空 .reopenNextCmd: " + reopenNextCmd);
                    reopenNextCmd = getCloseCmd(orderId);
                    writeLogFile("onClose" ,"","getCloseCmd .reopenNextCmd: " + reopenNextCmd);
                    if (!TextUtils.isEmpty(reopenNextCmd)) {
                        writeLogFile("onClose" ,"","write  reopenNextCmd " + reopenNextCmd);
                        onWrite(reopenNextCmd);
                    } else {
                        // 获取缓存中的设备响应，去重新获取关阀指令
                        String saveDeviceResult = getDeviceResult(orderId);

                        writeLogFile("onClose" ,"","获取缓存中的设备响应，去重新获取关阀指令 . saveDeviceResult： " + saveDeviceResult +"， orderId：" + orderId);
                        if (TextUtils.isEmpty(saveDeviceResult)) {
                            Log.wtf(TAG, "从缓存中获取上一次的设备响应失败");
                            closeBleConnection();
                            if (getMvpView() != null) {
                                getMvpView().onError(TradeError.CONNECT_ERROR_2);
                            }

                            writeLogFile("onClose" ,"","从缓存中获取上一次的设备响应失败 . saveDeviceResult " + saveDeviceResult);
                        } else {
                            Log.i(TAG, "关阀指令获取失败，重新使用设备响应请求指令");
                            processCommandResult(saveDeviceResult);
                            writeLogFile("onClose" ,"","关阀指令获取失败，重新使用设备响应请求指令 . saveDeviceResult " + saveDeviceResult);
                        }
                    }
                } else {
                    writeLogFile("onClose" ,"","正常下发 . reopenNextCmd " + reopenNextCmd);
                    onWrite(reopenNextCmd); // 正常下发
                }
            } else {
                // 向设备下发关阀指令
                if (checkCloseCmd()) {
                    writeLogFile("onClose" ,"","向设备下发关阀指令 . closeCmd " + closeCmd);
                    onWrite(closeCmd);
                }
            }
        }
    }

    @Override
    public void closeBleConnection() {

        // 清空连接观察者
        clearObservers();
        bleDataManager.unregisterConnectStatusListener(currentMacAddress);
        Log.d(TAG, "关闭蓝牙连接");
        bleDataManager.disconnect(currentMacAddress);
        Log.d(TAG, "停止蓝牙扫描");
        bleDataManager.stopScan();
        writeLogFile("closeBleConnection" ,"","关闭蓝牙连接 " );
    }

    @Override
    public void resetBleConnection() {
        // 清空旧连接
        clearObservers();

        // 此步骤非常重要，不加会造成重连请求掉进黑洞的现象
        resetSubscriptions();
        writeLogFile("resetBleConnection" ,"","清空旧连接 " );
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


    @Override
    public void onDetach() {
        super.onDetach();
        closeTriggerSubject.onNext(null);
    }


    protected boolean isBleOpen(){
        // 确保蓝牙适配器处于打开状态
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return null != adapter && adapter.isEnabled();

    }


    protected boolean isOpenLocation(){
       return  RxPermissions.getInstance(MvpApp.getContext()).isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)||
               RxPermissions.getInstance(MvpApp.getContext()).isGranted(Manifest.permission.ACCESS_FINE_LOCATION) ;
    }
    
    private void writeLogFile(String method ,String params , String result ) {

        StringBuffer  content = new StringBuffer();

        String time = TimeUtils.date2String(new Date());

        content.append(time );
        content.append('\t');
        content.append("蓝牙是否开启：" + isBleOpen() +'\t' + "定位是否开启:" + isOpenLocation() +'\n');
        content.append(method);
        content.append('\t');
        content.append(params);
        content.append('\t');
        content.append(result);
        content.append('\n');
        rx.Observable.just(content)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<StringBuffer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.wtf(TAG ,e.getMessage());
                    }

                    @Override
                    public void onNext(StringBuffer stringBuffer) {
                        writeLogFile(stringBuffer.toString());
                    }
                });
    }

    /**
     * 写文件
     * @param content   写入的内容
     * @param name    文件名
     */
    private void writeFile(String name ,String content){
        rx.Observable.just(1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(integer -> {
                    String timeStampsFileDirName = Environment.getExternalStorageDirectory().getAbsolutePath() +"/xiaolian/"+COUNT_NAME;
                    Log.e(TAG ,"fileName >>>>>>>>" + timeStampsFileDirName);
                    File timeStampsFileDir = new File(timeStampsFileDirName);
                    if (!timeStampsFileDir.exists() && !timeStampsFileDir.mkdirs()){
                        android.util.Log.e(TAG, "writeFile: >>>>>  创建文件失败"  );
                    }

                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(content)) return ;
                    File file = new File(timeStampsFileDirName ,name);
                    Log.e(TAG ,  "文件名>>>>>>>>>>" + file.getAbsolutePath());
                    try{
                        if (!file.exists()){
                            file.createNewFile();
                        }
                        FileIOUtils.writeFileFromString(file, content , true);
                    }catch (IOException e){
                        Log.e(TAG ,e.getMessage());
                    }
                });

    }



    /**
     * 写文件
     * @param content
     */
    private void writeLogFile(String content ){
        rx.Observable.just(1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(integer -> {
                    String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiaolian/" + deviceDataManager.getUser().getId()+"/";
                    File path = new File(filePath);
                    if (!path.exists() && !path.mkdirs()) {
//            onError(R.string.no_sd_card_premission);
                        return ;
                    }

                    File outputImage = new File(filePath, DeviceLogFileName );
                    try {
                        if (!outputImage.exists()) {
                            outputImage.createNewFile();
                            deviceDataManager.saveDeleteFileTime(System.currentTimeMillis());
                        }
                        FileIOUtils.writeFileFromString(outputImage, content , true);
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                });
    }


    /**
     * 记录用户使用次数
     * @param type    类型：  比如 SCAN  、  CONNECT 等
     * @param target  交互对象：  蓝牙设备 、  服务器
     * @param result  结果：   成功 、 失败
     * @param time    耗时：
     *  String 格式为 type:SCAN、target:SERVER、result:success、time:120;
     */
    private void recordUseNumber(Type type , Target target , Result result , long time){
        StringBuffer recordString = new StringBuffer();
        recordString.append("type")
                .append(Constant.TRADE_STATISTIC_CONTENT_SEPARATOR)
                .append(type.getContent())
                .append(Constant.TRADE_STATISTIC_PARAM_SEPARATOR)
                .append("target")
                .append(Constant.TRADE_STATISTIC_CONTENT_SEPARATOR)
                .append(target.getContent())
                .append(Constant.TRADE_STATISTIC_PARAM_SEPARATOR)
                .append("result")
                .append(Constant.TRADE_STATISTIC_CONTENT_SEPARATOR)
                .append(result.getContent())
                .append(Constant.TRADE_STATISTIC_PARAM_SEPARATOR)
                .append("time")
                .append(Constant.TRADE_STATISTIC_CONTENT_SEPARATOR)
                .append(time)
                .append(Constant.TRADE_STATISTIC_PARAM_SEPARATOR)
                .append("macAddress")
                .append(Constant.TRADE_STATISTIC_CONTENT_SEPARATOR)
                .append(deviceNo)
                .append(Constant.TRADE_STATISTIC_PARAM_SEPARATOR)
                .append("supplierId")
                .append(Constant.TRADE_STATISTIC_CONTENT_SEPARATOR)
                .append(supplier.getId())
                .append(Constant.TRADE_STATISTIC_PARAM_SEPARATOR)
                .append("deviceType")
                .append(Constant.TRADE_STATISTIC_CONTENT_SEPARATOR)
                .append(deviceType)
                .append(Constant.TRADE_STATISTIC_PARAM_SEPARATOR)
                .append("residenceId")
                .append(Constant.TRADE_STATISTIC_CONTENT_SEPARATOR)
                .append(residenceId)
                .append(Constant.TRADE_STATISTIC_ITEM_SEPARATOR);

        String fileName = "" + TimeUtils.getCountTimeStamp() + deviceDataManager.getUser().getId()+".txt" ;
        writeFile(fileName ,recordString.toString());
    }
}
