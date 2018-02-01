package com.xiaolian.amigo.ui.device.intf;

import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 设备账单
 *
 * @author caidong
 * @date 17/10/11
 */
public interface IDeviceOrderView extends IBaseView {

    /**
     * 刷新完成
     * @param respDTO 订单数据
     */
    void setRefreshComplete(OrderDetailRespDTO respDTO);

    /**
     * 投诉
     */
    void toComplaint();

    /**
     * 显示未使用提示
     */
    void showNoUseTip();
}
