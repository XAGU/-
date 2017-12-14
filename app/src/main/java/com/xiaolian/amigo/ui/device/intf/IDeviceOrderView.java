package com.xiaolian.amigo.ui.device.intf;

import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * Created by caidong on 2017/10/11.
 */
public interface IDeviceOrderView extends IBaseView {

    void setRefreshComplete(OrderDetailRespDTO respDTO);

    void toComplaint();
}
