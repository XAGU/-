package com.xiaolian.amigo.ui.device.bathroom.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * @author zcd
 * @date 18/7/3
 */
public interface IChooseBathroomPresenter<V extends IChooseBathroomView>
        extends IBasePresenter<V> {


    void getBathroomList(long buildingId);


    void precondition(boolean isShowDialog);

    boolean getBathroomPassword();

    void setIsResume(boolean isResume);
    /**
     * 获取楼栋流量
     */
    void buildingTraffic(long id);


    /**
     * 预约楼栋
     * @param   deviceNo  type 为1
     * @param floorId   type  为2
     */
    void  booking(long deviceNo , long floorId);

    /**
     * 清除查询次数
     */
    void clearTime() ;

    /**
     * 查询预约状态
     * @param bookingId
     */
    void  queryBooking(long bookingId) ;

    /**
     * 查询订单状态，是正在用水之后的状态
     * @param bathOrder
     */
    void queryBathorder(long bathOrder);

}
