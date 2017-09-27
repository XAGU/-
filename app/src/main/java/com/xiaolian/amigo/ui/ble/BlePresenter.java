package com.xiaolian.amigo.ui.ble;

import android.util.Log;

import com.polidea.rxandroidble.scan.ScanResult;
import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.ble.intf.IBlePresenter;
import com.xiaolian.amigo.ui.ble.intf.IBleView;

import javax.inject.Inject;

/**
 * Created by caidong on 2017/9/22.
 */
public class BlePresenter<V extends IBleView> extends BasePresenter<V>
        implements IBlePresenter<V> {

    private static final String TAG = BlePresenter.class.getSimpleName();
    private IBleDataManager manager;

    @Inject
    public BlePresenter(IBleDataManager manager) {
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
            public void onConnectError() {
                // ignore 不会执行到此
            }

            @Override
            public void onExecuteError(Throwable e) {
                // ignore 不会执行到此
            }

            @Override
            public void onNext(ScanResult result) {
                getMvpView().addDevice(result);
            }
        });
    }

}
