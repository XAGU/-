package com.xiaolian.amigo.ui.device.bathroom.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * @author zcd
 * @date 18/7/3
 */
public interface IChooseBathroomPresenter<V extends IChooseBathroomView>
        extends IBasePresenter<V> {
    void getBathroomList(long buildingId);

    void preBooking(String deviceNo);

    void precondition();

    boolean getBathroomPassword();


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

}
