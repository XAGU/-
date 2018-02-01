package com.xiaolian.amigo.ui.order.intf;

import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 账单详情
 *
 * @author zcd
 * @date 17/11/10
 */

public interface IOrderDetailPresenter<V extends IOrderDetailView> extends IBasePresenter<V> {
    /**
     * 检查是否已经投诉过
     */
    void checkComplaint();

    /**
     * 获取账单
     *
     * @param orderId 账单id
     */
    void getOrder(Long orderId);

    /**
     * 获取token
     *
     * @return token
     */
    String getToken();

    /**
     * 获取账单
     *
     * @return 账单详情
     */
    OrderDetailRespDTO getOrder();
}
