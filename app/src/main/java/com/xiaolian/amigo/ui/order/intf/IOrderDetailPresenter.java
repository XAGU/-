package com.xiaolian.amigo.ui.order.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * <p>
 * Created by zcd on 17/11/10.
 */

public interface IOrderDetailPresenter<V extends IOrderDetailView> extends IBasePresenter<V> {
    void checkComplaint(Long orderId, Integer orderType);
}
