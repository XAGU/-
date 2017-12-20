package com.xiaolian.amigo.ui.order.intf;

import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * Created by caidong on 2017/9/18.
 */
public interface IOrderDetailView extends IBaseView {

    void toComplaint();

    void renderView(OrderDetailRespDTO data);

    void showNoUseTip();
}
