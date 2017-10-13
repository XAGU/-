package com.xiaolian.amigo.ui.device.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * Created by caidong on 2017/10/11.
 */
public interface IDeviceOrderPresenter<V extends IDeviceOrderView> extends IBasePresenter<V> {

    // 页面初次加载
    void onLoad(long orderId);

}