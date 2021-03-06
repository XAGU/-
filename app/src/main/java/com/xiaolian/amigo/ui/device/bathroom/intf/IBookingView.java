package com.xiaolian.amigo.ui.device.bathroom.intf;

import com.xiaolian.amigo.data.network.model.bathroom.BathBookingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderCurrentRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathPreBookingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BookingQueueProgressDTO;
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
    void bookingSuccess(BathBookingRespDTO data);

    /**
     * 预约取消
     */
    void  bookingCancel() ;


    /**
     * 退出activity
     */
    void finishActivity();


    /**
     * 预约超时
     */
    void appointMentTimeOut(BathBookingRespDTO respDTO);

    /**
     * 支付成功等待下发订单给设备
     */
    void showWaitLoading();


    void showLoading(String text );

    /**
     * 取消等待动画
     */
    void hideWaitLoading(boolean isSuccess);

    /**
     * 等待服务器下发设备超时
     */
    void SendDeviceTimeOut();


    /**
     * 剩余时间倒计时
     * @param text
     */
    void countTimeLeft(String text);

    /**
     * 去洗浴中界面
     * @param dto
     */
    void gotoUsing(long  dto);



    /**
     * 无参预约倒计时
     * @param
     */
    void appointMentTimeOut(boolean isServer);

    /**
     * 显示排队信息
     * @param data
     */
    void showQueue(BookingQueueProgressDTO data);

    void startOrderInfo(BathOrderCurrentRespDTO dto);
}
