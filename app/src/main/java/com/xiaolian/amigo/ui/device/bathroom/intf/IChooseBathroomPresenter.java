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


}
