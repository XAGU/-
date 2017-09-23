package com.xiaolian.amigo.ui.ble;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.util.Log;

import com.polidea.rxandroidble.RxBleConnection;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.xiaolian.amigo.data.manager.intf.IBLEDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.ble.intf.IBleInteractivePresenter;
import com.xiaolian.amigo.ui.ble.intf.IBleInteractiveView;

import java.nio.charset.Charset;
import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

import static com.trello.rxlifecycle.android.ActivityEvent.PAUSE;

/**
 * Created by caidong on 2017/9/22.
 */
public class BleInteractivePresenter<V extends IBleInteractiveView> extends BasePresenter<V>
        implements IBleInteractivePresenter<V> {

    private static final String TAG = BleInteractivePresenter.class.getSimpleName();
    private static final String DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805f9b34fb";
    private IBLEDataManager manager;
    private Observable<RxBleConnection> connectionObservable;
    private BluetoothGattCharacteristic writeCharacteristic;
    private BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();
    // 当前连接的设备
    private String currentMacAddress;


    @Inject
    public BleInteractivePresenter(IBLEDataManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void onConnect(@NonNull String macAddress) {
        // 1、创建共享连接
        connectionObservable = manager.prepareConnectionObservable(macAddress, false).compose(bindUntilEvent(PAUSE));

        // 2、连接设备并获取写特征值
        addObserver(manager.connect(connectionObservable),
                new BLEObserver<BluetoothGattCharacteristic>() {
                    @Override
                    public void onError(Throwable e) {
                        Log.wtf(TAG, "获取特征值失败！", e);
                        getMvpView().onConnectError();
                    }

                    @Override
                    public void onNext(BluetoothGattCharacteristic characteristic) {
                        // 设备连接上存储mac地址供后续读写数据使用
                        currentMacAddress = macAddress;

                        // 取第一个匹配到的特征值
                        if (null == writeCharacteristic && characteristic.getProperties() == 16) {
                            writeCharacteristic = characteristic;

                            // 3、向蓝牙设备写特征值描述，为后续接受蓝牙设备notify通知做铺垫
                            if (manager.getStatus(macAddress) == RxBleConnection.RxBleConnectionState.CONNECTED) {
                                writeCharacteristicDesc(writeCharacteristic);
                            } else {
                                getMvpView().onStatusError();
                            }
                        }
                    }
                });
    }

    // 写特征值描述
    private void writeCharacteristicDesc(BluetoothGattCharacteristic characteristic) {
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(DESCRIPTOR_UUID));

        addObserver(manager.writeDescriptor(connectionObservable, descriptor),
                new BLEObserver<byte[]>() {
                    @Override
                    public void onError(Throwable e) {
                        Log.wtf(TAG, "写特征值描述失败！", e);
                        getMvpView().onConnectError();
                    }

                    @Override
                    public void onNext(byte[] descriptor) {
                        Log.i(TAG, "写特征值描述成功！");
                    }
                });
    }

    @Override
    public void onWrite(@NonNull String command) {
        if (manager.getStatus(currentMacAddress) == RxBleConnection.RxBleConnectionState.CONNECTED) {
            getMvpView().onStatusError();
        }

        addObserver(manager.write(connectionObservable, command.getBytes(Charset.defaultCharset())),
                new BLEObserver<byte[]>() {
                    @Override
                    public void onError(Throwable e) {
                        Log.wtf(TAG, "发送指令失败！", e);
                        getMvpView().onWriteError();
                    }

                    @Override
                    public void onNext(byte[] data) {
                        Log.i(TAG, "发送指令成功！");
                    }
                });
    }

    @Override
    public void registerNotify() {
        if (manager.getStatus(currentMacAddress) == RxBleConnection.RxBleConnectionState.CONNECTED) {
            getMvpView().onStatusError();
        }

        addObserver(manager.notify(connectionObservable),
                new BLEObserver<byte[]>() {
                    @Override
                    public void onError(Throwable e) {
                        Log.wtf(TAG, "接收数据失败！", e);
                        getMvpView().onNotifyError();
                    }

                    @Override
                    public void onNext(byte[] data) {
                        Log.i(TAG, "接收数据成功！");

                        // TODO 对data对应的业务数据做处理，此处需要有状态机控制
                    }
                });
    }

    @NonNull
    @CheckResult
    private <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }
}
