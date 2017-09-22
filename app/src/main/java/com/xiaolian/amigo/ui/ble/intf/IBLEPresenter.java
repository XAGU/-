package com.xiaolian.amigo.ui.ble.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;
import com.xiaolian.amigo.ui.order.intf.IOrderView;

/**
 * Created by caidong on 2017/9/22.
 */
public interface IBLEPresenter <V extends IBLEView> extends IBasePresenter<V> {

    // 扫描设备
    void scan();
}
