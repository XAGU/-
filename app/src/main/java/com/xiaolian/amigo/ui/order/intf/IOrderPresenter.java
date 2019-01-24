package com.xiaolian.amigo.ui.order.intf;


import com.xiaolian.amigo.di.LoginActivityContext;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 账单
 *
 * @author caidong
 * @date 17/9/15
 */
@LoginActivityContext
public interface IOrderPresenter<V extends IOrderView> extends IBasePresenter<V> {

    /**
     * 刷新个人消费账单
     *
     * @param page 页数
     */
    void requestOrders(int page, Integer deviceType,
                       Integer year, Integer month, Integer action, Integer orderStatus);
}
