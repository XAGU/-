package com.xiaolian.amigo.ui.device.bathroom.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

public interface IBathroomHeartPresenter<V extends IBathroomHeartView>  extends IBasePresenter<V>{

    /**
     * 客户端请求结账
     * @param id  orderId
     */
    void askSettle(Long id);

    /**
     * 查询id
     * @param id
     */
    void queryBathroomOrder(Long id , boolean isShowDialog , int time);


    void onPause() ;

    void onResume();
}
