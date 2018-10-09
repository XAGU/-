package com.xiaolian.amigo.ui.device.bathroom.intf;

import com.xiaolian.amigo.data.network.model.bathroom.BathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathPreBookingRespDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * @author zcd
 * @date 18/6/29
 */
public interface IBuyCodeView extends IBaseView {
    /**
     * 购买前获取的信息
     * @param data
     */
    void preBuy(BathPreBookingRespDTO data);

    /**
     * 购买洗澡卷成功
     * @param respDTO
     */
    void bookingSuccess(BathOrderRespDTO respDTO);

    /**
     * 取消已购买洗澡卷
     * @param respDTO
     */
    void bookingCancel(BooleanRespDTO respDTO);

    /**
     * 显示倒计时时间
     * @param time
     */
    void setBookingCountDownTime(String time);
}
