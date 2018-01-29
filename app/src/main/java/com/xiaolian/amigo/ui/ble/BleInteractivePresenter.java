package com.xiaolian.amigo.ui.ble;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.xiaolian.amigo.data.manager.BleDataManager;
import com.xiaolian.amigo.util.Log;

import com.polidea.rxandroidble.RxBleConnection;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.ble.intf.IBleInteractivePresenter;
import com.xiaolian.amigo.ui.ble.intf.IBleInteractiveView;
import com.xiaolian.amigo.ui.ble.util.HexBytesUtils;

import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

import static com.trello.rxlifecycle.android.ActivityEvent.PAUSE;

/**
 *
 * Created by caidong on 2017/9/22.
 */
public class BleInteractivePresenter<V extends IBleInteractiveView> extends BasePresenter<V>
        implements IBleInteractivePresenter<V> {

    private static final String TAG = BleInteractivePresenter.class.getSimpleName();
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


    @Inject
    BleInteractivePresenter(IBleDataManager manager) {
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
                new BleObserver<BluetoothGattCharacteristic>() {
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
                new BleObserver<Observable<byte[]>>() {
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
                new BleObserver<byte[]>() {
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
        if (!connected || manager.getStatus(currentMacAddress) != RxBleConnection.RxBleConnectionState.CONNECTED) {
            getMvpView().onStatusError();
            return;
        }

        byte[] commandBytes = HexBytesUtils.hexStr2Bytes(command);
        addObserver(manager.write(connectionObservable, commandBytes, BleDataManager.WRITE_CHARACTERISTIC_UUID),
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
                });
    }

    @Override
    public void registerNotify() {
        if (!connected || manager.getStatus(currentMacAddress) != RxBleConnection.RxBleConnectionState.CONNECTED) {
            getMvpView().onStatusError();
            return;
        }

        addObserver(manager.notify(connectionObservable, notifyCharacteristic),
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
                            getMvpView().onShow(result);
                        }
                    }
                });
    }

    // 统一处理设备连接异常
    private void handleDisConnectError() {
        Log.wtf(TAG, "蓝牙连接已断开！");
        connected = false;
        this.onDisConnect();
        getMvpView().onConnectError();
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
}
