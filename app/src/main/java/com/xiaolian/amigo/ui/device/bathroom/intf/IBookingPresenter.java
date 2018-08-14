package com.xiaolian.amigo.ui.device.bathroom.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

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
     * 取消倒计时
     */
    void cancelCountDown();


    /**
     * 查询特定订单的状态
     * @param bathOrderId
     */
    void query(String bathOrderId , boolean isToUsing , int time , boolean isShowDialog);



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


    /**
     * 查询排队状态
     * @param id
     */
    void queryQueueId(long id  , boolean isShowDialog);

    /**
     * 取消预约排队
     * @param id
     */
    void cancelQueue(long id);

    /**
     * 提醒服务器预约超时
     */
    void notifyExpired();

}
