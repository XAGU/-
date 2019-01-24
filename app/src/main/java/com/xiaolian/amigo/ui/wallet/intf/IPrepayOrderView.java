package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 待找零账单
 *
 * @author zcd
 * @date 17/10/12
 */

public interface IPrepayOrderView extends IBaseView {
     void render(OrderDetailRespDTO orderDetailRespDTO);
}
