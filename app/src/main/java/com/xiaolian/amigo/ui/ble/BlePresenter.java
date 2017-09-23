package com.xiaolian.amigo.ui.ble;

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
import com.xiaolian.amigo.ui.ble.intf.IBlePresenter;
import com.xiaolian.amigo.ui.ble.intf.IBleView;

import java.nio.charset.Charset;
import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

import static com.trello.rxlifecycle.android.ActivityEvent.PAUSE;

/**
 * Created by caidong on 2017/9/22.
 */
public class BlePresenter<V extends IBleView> extends BasePresenter<V>
        implements IBlePresenter<V> {

    private static final String TAG = BlePresenter.class.getSimpleName();
    private IBLEDataManager manager;

    @Inject
    public BlePresenter(IBLEDataManager manager) {
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

}
