package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.data.network.model.device.DeviceCheckRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

public interface ICompleteInfoView  extends IBaseView {
    /**
     * 刷新页面信息
     */
    void refreshCompleteInfoView();

    /**
     * 选择洗澡地址
     */
    void startChooseBathroom();

    void choseDormitory();

    void showDeviceUsageDialog(DeviceCheckRespDTO deviceCheckRespDTO);
}
