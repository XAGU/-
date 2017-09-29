package com.xiaolian.amigo.ui.device;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.util.Log;

import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleCustomOperation;
import com.polidea.rxandroidble.internal.connection.RxBleGattCallback;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.ble.util.HexBytesUtils;
import com.xiaolian.amigo.ui.device.intf.IDevicePresenter;
import com.xiaolian.amigo.ui.device.intf.IDeviceView;
import com.xiaolian.amigo.util.ble.Agreement;

import java.math.BigDecimal;
import java.util.UUID;

import rx.Observable;
import rx.Scheduler;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

import static com.trello.rxlifecycle.android.ActivityEvent.PAUSE;

/**
 * Created by caidong on 2017/9/22.
 */
public class DeviceBasePresenter<V extends IDeviceView> extends BasePresenter<V>
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
    // 设备连接状态, 只会在UI线程中更新该变量，不需要volatile
    private boolean connected = false;
    // 蓝牙已关闭
    private boolean bluetoothDisabled = false;
    // 回调任务（存放结束用水的回调）
    private Callback callback;

    public DeviceBasePresenter(IBleDataManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void onConnect(@NonNull String macAddress) {
        // 1、创建共享连接
        connectionObservable = manager
                .prepareConnectionObservable(macAddress, false, disconnectTriggerSubject)
                .compose(bindUntilEvent(PAUSE));

        // 2、连接设备
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
                        // 设备连接上存储mac地址供后续读写数据使用
                        currentMacAddress = macAddress;
                        // 蓝牙已启用
                        bluetoothDisabled = false;

                        // 取第一个匹配到的特征值
                        if (null == notifyCharacteristic) {
                            notifyCharacteristic = characteristic;

                            // 3、开启notify，向蓝牙设备写notify特征值描述，为后续接受蓝牙设备notify通知做铺垫
                            if (manager.getStatus(macAddress) == RxBleConnection.RxBleConnectionState.CONNECTED) {
                                enableNotify();
                            } else {
                                getMvpView().onStatusError();
                            }
                        }
                    }
                });
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
                        writeNotifyCharacteristicDesc();
                    }
                });
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
                        Log.wtf(TAG, "写特征值描述失败！", e);
                        getMvpView().onConnectError();
                    }

                    @Override
                    public void onNext(byte[] descriptor) {
                        Log.i(TAG, "写特征值描述成功！");
                        // 至此认为设备已真正连接成功
                        connected = true;

                        // 注册notify通道接收数据
                        registerNotify();
                    }
                });
    }

    @Override
    public void onWrite(@NonNull String command) {
        if (manager.getStatus(currentMacAddress) != RxBleConnection.RxBleConnectionState.CONNECTED) {
            getMvpView().onStatusError();

            clearObservers();

            // reconnect
            this.setCallback(new Callback() {
                @Override
                public void execute() {
                    onWrite(command);
                }
            });
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
                });
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
                            handleResult(result);
                        }
                    }
                });

        // 开始握手连接
        onWrite(Agreement.getInstance().createConnection());
    }

    // 统一处理设备连接异常
    private void handleDisConnectError() {
        Log.wtf(TAG, "蓝牙连接已断开！");
        connected = false;
        this.onDisConnect();
        getMvpView().onConnectError();

        addObserver(connectionObservable, new BLEObserver<RxBleConnection>() {

            @Override
            public void onNext(RxBleConnection connection) {
                connection.queue(new RxBleCustomOperation() {
                    @NonNull
                    @Override
                    public Observable asObservable(BluetoothGatt bluetoothGatt, RxBleGattCallback rxBleGattCallback, Scheduler scheduler) throws Throwable {
                        return Observable.fromCallable(() -> {
                            try {
                                bluetoothGatt.disconnect();
                                bluetoothGatt.close();
                            } catch (Exception e) {
                                // ignore
                                Log.wtf(TAG, "关闭连接失败", e);
                            }
                            return null;
                        });
                    }
                });
            }

            @Override
            public void onConnectError() {

            }

            @Override
            public void onExecuteError(Throwable e) {

            }
        });

        // 判断蓝牙模块是否打开,如果没有提示打开
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (null == adapter || !adapter.isEnabled()) {
            // 避免重复提示
            if (!bluetoothDisabled) {
                getMvpView().getBLEPermission();
                bluetoothDisabled = true;
            }
        }
    }

    @Override
    public void onDisConnect() {
        disconnectTriggerSubject.onNext(null);
    }

    @NonNull
    @CheckResult
    private <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifeCycleSubject, event);
    }

    public static interface Callback {
        void execute();
    }

    @Override
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    /*************************** 以下为测试用 *****************************/
    @Override
    public void handleResult(String data) {
        String prefix = data.substring(0, 4);
        switch (prefix) {
            case "a801":
                if (data.length() != 28) {
                    Log.e(TAG, "握手失败！data:" + data);
                } else {
                    Log.e(TAG, "握手成功！data:" + data);
                    Agreement.getInstance().InitKey(data, "AAABCDDEEFADABBB");

                    // 回调下发预结账指令
                    if (null != callback) {
                        callback.execute();
                    }
                }
                break;
            case "a802":
                if (!"a80200aa".equals(data)) {
                    Log.e(TAG, "开阀失败！data:" + data);
                } else {
                    Log.e(TAG, "开阀成功！data:" + data);
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
                    Log.e(TAG, "预结账成功收到的数据：" + data);
                    BigDecimal bal = Agreement.getInstance().getYE(data);
                    Log.e(TAG, "预结账金额：" + bal);
                    onWrite(Agreement.getInstance().Checkout("12345678"));
                } else {
                    Log.e(TAG, "预结账失败收到的数据：" + data);
                }
                break;
        }
    }

}
