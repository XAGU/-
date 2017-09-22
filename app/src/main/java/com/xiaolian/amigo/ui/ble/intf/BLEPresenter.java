package com.xiaolian.amigo.ui.ble.intf;

import android.util.Log;

import com.polidea.rxandroidble.scan.ScanResult;
import com.xiaolian.amigo.data.manager.intf.IBLEDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by caidong on 2017/9/22.
 */
public class BLEPresenter<V extends IBLEView> extends BasePresenter<V>
        implements IBLEPresenter<V> {

    private static final String TAG = BLEPresenter.class.getSimpleName();
    private IBLEDataManager manager;

    @Inject
    public BLEPresenter(IBLEDataManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void scan() {
        addObserver(manager.scan(), new BLEObserver() {
            @Override
            public void onError(Throwable e) {
                Log.wtf(TAG, "扫描设备失败", e);
            }

            @Override
            public void onNext(Object obj) {
                ScanResult result = (ScanResult)obj ;
                getMvpView().addDevice(result);
            }
        });
    }
}
