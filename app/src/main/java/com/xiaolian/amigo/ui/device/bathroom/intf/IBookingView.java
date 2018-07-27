package com.xiaolian.amigo.ui.device.bathroom.intf;

import com.xiaolian.amigo.data.network.model.bathroom.BathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathPreBookingRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * @author zcd
 * @date 18/6/29
 */
public interface IBookingView extends IBaseView {

    /**
     * 预约成功，刷新界面
     * @param data
     */
    void bookingSuccess(BathOrderRespDTO data);

    void bookingCancel(BathOrderRespDTO data );

    /**
     * 设置预约倒计时
     */
    void setBookingCountDownTime(String time);

    /**
     * 退出activity
     */
    void finishActivity();

    /**
     * 重新预约
     */
    void reschedule(BathPreBookingRespDTO respDTO);

    /**
     * 预约超时
     */
    void appointMentTimeOut();

    /**
     * 支付成功等待下发订单给设备
     */
    void showWaitLoading();

    /**
     * 取消等待动画
     */
    void hideWaitLoading();

    /**
     * 等待服务器下发设备超时
     */
    void SendDeviceTimeOut();

}
