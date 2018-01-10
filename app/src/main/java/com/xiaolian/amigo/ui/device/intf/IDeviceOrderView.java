package com.xiaolian.amigo.ui.device.intf;

import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 设备账单
 * Created by caidong on 2017/10/11.
 */
public interface IDeviceOrderView extends IBaseView {

    void setRefreshComplete(OrderDetailRespDTO respDTO);

    void toComplaint();

    void showNoUseTip();
}
