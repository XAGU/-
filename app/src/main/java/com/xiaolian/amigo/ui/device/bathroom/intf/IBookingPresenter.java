package com.xiaolian.amigo.ui.device.bathroom.intf;

import com.xiaolian.amigo.data.network.model.bathroom.BathOrderRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

import java.util.Observable;

/**
 * @author zcd
 * @date 18/6/29
 */
public interface IBookingPresenter<V extends IBookingView> extends IBasePresenter<V> {


    /**
     * 取消预约
     * @param id
     */
    void cancel(long id);

    /**
     * 预约倒计时
     */
    void bathroomBookingCountDown( );


    /**
     * 取消倒计时
     */
    void cancelCountDown();

    /**
     * 查询是否已存在订单
     * @param deviceNo
     */
    void preBooking(String deviceNo);

    /**
     * 查询特定订单的状态
     * @param bathOrderId
     */
    void query(String bathOrderId);

    /**
     * 解除绑定设备
     * @param deviceNo
     */
    void unLock(String deviceNo);

    /**
     * 预约
     * @param device
     */
    void booking(String device);

    /**
     * 根据过期时间倒计时
     */
    void countDownexpiredTime(long expiredTime );
}
