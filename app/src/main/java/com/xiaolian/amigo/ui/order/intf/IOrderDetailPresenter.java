package com.xiaolian.amigo.ui.order.intf;

import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * <p>
 * Created by zcd on 17/11/10.
 */

public interface IOrderDetailPresenter<V extends IOrderDetailView> extends IBasePresenter<V> {
    void checkComplaint();

    void getOrder(Long orderId);

    String getToken();

    OrderDetailRespDTO getOrder();
}
