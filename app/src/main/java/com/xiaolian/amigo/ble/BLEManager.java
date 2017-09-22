package com.xiaolian.amigo.ble;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xiaolian.amigo.ble.intf.IBLEManager;
import com.xiaolian.amigo.ui.base.BaseActivity;

import io.reactivex.functions.Consumer;

/**
 * Created by caidong on 2017/9/22.
 */
public class BLEManager implements IBLEManager {

    private static final String TAG = BLEManager.class.getSimpleName();
    private BaseActivity activity;

    public BLEManager(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public void check(Consumer<IBLEManager> consumer) {
        Log.i(TAG, "确认设备是否已打开蓝牙设备......");
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                               @Override
                               public void accept(Boolean open) throws Exception {
                                   if (open) {
                                       Log.i(TAG, "设备蓝牙已打开，可以继续！");
                                       consumer.accept(BLEManager.this);
                                   } else {
                                       Log.i(TAG, "设备蓝牙未打开，操作无法继续！");
                                       activity.onBLENotOpen();
                                   }
                               }
                           }
                );
    }
}
