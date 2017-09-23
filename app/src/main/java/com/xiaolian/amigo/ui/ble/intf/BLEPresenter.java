package com.xiaolian.amigo.ui.ble.intf;

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
import com.xiaolian.amigo.data.manager.intf.IBLEDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;

import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

import static com.trello.rxlifecycle.android.ActivityEvent.PAUSE;

/**
 * Created by caidong on 2017/9/22.
 */
public class BLEPresenter<V extends IBLEView> extends BasePresenter<V>
        implements IBLEPresenter<V> {

    private static final String TAG = BLEPresenter.class.getSimpleName();
    private static final String DESCRIPTION_UUID = "00002902-0000-1000-8000-00805f9b34fb";
    private IBLEDataManager manager;
    private Observable<RxBleConnection> connectionObservable;
    private BluetoothGattCharacteristic writeCharacteristic;
    private BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();


    @Inject
    public BLEPresenter(IBLEDataManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void onScan() {
        addObserver(manager.scan(), new BLEObserver<ScanResult>() {
            @Override
            public void onError(Throwable e) {
                Log.wtf(TAG, "扫描设备失败！", e);
                getMvpView().onScanError();
            }

            @Override
            public void onNext(ScanResult result) {
                getMvpView().addDevice(result);
            }
        });
    }

    @Override
    public void onConnect(String macAddress) {
        // 1、创建共享连接
        connectionObservable = manager.prepareConnectionObservable(macAddress, false).compose(bindUntilEvent(PAUSE));

        // 2、连接设备并获取写特征值
        addObserver(manager.connect(connectionObservable),
                new BLEObserver<BluetoothGattCharacteristic>() {
                    @Override
                    public void onError(Throwable e) {
                        Log.wtf(TAG, "获取特征值失败！", e);
                    }

                    @Override
                    public void onNext(BluetoothGattCharacteristic characteristic) {
                        // 取第一个匹配到的特征值
                        if (null == writeCharacteristic && characteristic.getProperties() == 16) {
                            writeCharacteristic = characteristic;

                            // 3、向蓝牙设备写特征值描述，为后续接受蓝牙设备notify通知做铺垫
                            writeCharacteristicDesc(writeCharacteristic);
                        }
                    }
                });
    }

    // 写特征值描述
    private void writeCharacteristicDesc(BluetoothGattCharacteristic characteristic) {
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(DESCRIPTION_UUID));

        addObserver(manager.writeDescriptor(connectionObservable, descriptor),
                new BLEObserver<byte[]>() {
                    @Override
                    public void onError(Throwable e) {
                        Log.wtf(TAG, "写特征值描述失败！", e);
                    }

                    @Override
                    public void onNext(byte[] descriptor) {
                        Log.i(TAG, "写特征值描述成功！");
                    }
                });
    }

    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }
}
