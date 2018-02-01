package com.xiaolian.amigo.ui.order.intf;

import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 账单详情
 *
 * @author zcd
 * @author 18/1/17
 */

public interface INormalOrderPresenter<V extends INormalOrderView> extends IBasePresenter<V> {
    /**
     * 设置账单id
     *
     * @param orderId 账单id
     */
    void setOrderId(Long orderId);

    /**
     * 获取账单id
     *
     * @return 账单id
     */
    Long getOrderId();

    /**
     * 请求账单详情
     */
    void requestOrder();

    /**
     * 检查是否已经投诉过
     */
    void checkComplaint();

    /**
     * 获取投诉参数
     *
     * @return 投诉参数
     */
    String getComplaintParam();

    /**
     * 获取账单
     *
     * @return 账单详情
     */
    OrderDetailRespDTO getOrder();
}
