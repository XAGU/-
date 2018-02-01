package com.xiaolian.amigo.ui.order.intf;

import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 账单详情
 *
 * @author zcd
 * @date 18/1/17
 */

public interface INormalOrderView extends IBaseView {
    /**
     * 显示账单
     *
     * @param data 账单详情
     */
    void renderView(OrderDetailRespDTO data);

    /**
     * 投诉
     */
    void toComplaint();
}
