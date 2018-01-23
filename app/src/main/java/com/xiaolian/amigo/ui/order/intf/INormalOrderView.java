package com.xiaolian.amigo.ui.order.intf;

import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * <p>
 * Created by zcd on 18/1/17.
 */

public interface INormalOrderView extends IBaseView {
    void renderView(OrderDetailRespDTO data);

    void toComplaint();
}
