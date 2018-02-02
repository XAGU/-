package com.xiaolian.amigo.ui.ble.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * @author caidong
 * @date 17/9/22
 */
public interface IBlePresenter<V extends IBleView> extends IBasePresenter<V> {

    // 扫描设备
    void onScan();

}
