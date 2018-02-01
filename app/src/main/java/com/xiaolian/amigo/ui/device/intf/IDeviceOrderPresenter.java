package com.xiaolian.amigo.ui.device.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 设备账单
 *
 * @author caidong
 * @date 2017/10/11
 */
public interface IDeviceOrderPresenter<V extends IDeviceOrderView> extends IBasePresenter<V> {

    /**
     * 页面初次加载
     * @param orderId 订单id
     */
    void onLoad(long orderId);

    /**
     * 获取token
     * @return token
     */
    String getToken();

    /**
     * 剪裁是否已经投诉过了
     * @param orderId 订单id
     * @param orderType 订单类型
     */
    void checkComplaint(Long orderId, Integer orderType);

}
