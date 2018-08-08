package com.xiaolian.amigo.ui.device.bathroom.intf;

import com.xiaolian.amigo.data.network.model.bathroom.BathOrderCurrentRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

public interface IBathroomHeartView extends IBaseView{

    /**
     * 跳转账单详情
     */
    void goToOrderInfo();

    void getOrderInfo(BathOrderCurrentRespDTO dto);

    void reset();
}
