package com.xiaolian.amigo.ui.device.intf;

import com.xiaolian.amigo.data.network.model.order.OrderPreInfoDTO;

/**
 * 用水设备 热水澡 饮水机
 *
 * @author zcd
 * @date 17/10/13
 */

public interface IWaterDeviceBaseView extends IDeviceView {

    /**
     * 显示提示充值dialog
     *
     * @param amount 充值金额
     */
    void showRechargeDialog(double amount);

    /**
     * 开始使用
     */
    void startUse();


    /**
     * 设置预付信息
     *
     * @param data 预付信息
     */
    void setPrepayOption(OrderPreInfoDTO data);

    /**
     * 打电话给客服
     *
     * @param tel 客服电话
     */
    void showCsCallDialog(String tel);

    /**
     * 显示引导
     */
    void showGuide();
}
