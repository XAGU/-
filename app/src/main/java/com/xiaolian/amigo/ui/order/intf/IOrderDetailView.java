package com.xiaolian.amigo.ui.order.intf;

import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 账单详情
 *
 * @author caidong
 * @date 17/9/18
 */
public interface IOrderDetailView extends IBaseView {

    /**
     * 投诉
     */
    void toComplaint();

    /**
     * 显示页面
     *
     * @param data 账单数据
     */
    void renderView(OrderDetailRespDTO data);

    /**
     * 显示未使用提示
     */
    void showNoUseTip();

    /**
     * 异常账单提示
     */
    void showOnErrorTip(OrderDetailRespDTO dto);
}
