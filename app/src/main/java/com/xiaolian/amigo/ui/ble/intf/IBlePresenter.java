package com.xiaolian.amigo.ui.ble.intf;

import android.support.annotation.NonNull;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * Created by caidong on 2017/9/22.
 */
public interface IBlePresenter<V extends IBleView> extends IBasePresenter<V> {

    // 扫描设备
    void onScan();

}
