package com.xiaolian.amigo.ui.device.intf;

import com.xiaolian.amigo.data.network.model.order.OrderPreInfoDTO;

/**
 * 用水设备 热水澡 饮水机
 * <p>
 * Created by zcd on 10/13/17.
 */

public interface IWaterDeviceBaseView extends IDeviceView {
    // 显示提示充值dialog
    void showRechargeDialog(double amount);

    // 开始使用
    void startUse();


    void setPrepayOption(OrderPreInfoDTO data);

    // 打电话给客服
    void showCsCallDialog(String tel);

    void showGuide();
}
