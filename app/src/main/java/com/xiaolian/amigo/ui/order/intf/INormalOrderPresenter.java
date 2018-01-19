package com.xiaolian.amigo.ui.order.intf;

import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * <p>
 * Created by zcd on 18/1/17.
 */

public interface INormalOrderPresenter<V extends INormalOrderView> extends IBasePresenter<V> {
    void setOrderId(Long orderId);
    Long getOrderId();

    void requestOrder();

    void checkComplaint();

    String getComplaintParam();
    OrderDetailRespDTO getOrder();
}
