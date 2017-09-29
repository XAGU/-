package com.xiaolian.amigo.ui.device.intf.heator;

import com.xiaolian.amigo.ui.device.intf.IDeviceView;

/**
 * Created by caidong on 2017/9/28.
 */
public interface IHeaterView extends IDeviceView {

    // 显示提示充值dialog
    void showRechargeDialog(int amount);

    // 开始使用
    void startUse();
}
