package com.xiaolian.amigo.ui.repair.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.order.adaptor.OrderAdaptor;
import com.xiaolian.amigo.ui.repair.adaptor.RepairAdaptor;

import java.util.List;

/**
 * Created by caidong on 2017/9/18.
 */
public interface IRepairView extends IBaseView {

    /**
     * 刷新报修记录
     *
     * @param repairs 待添加的报修记录
     */
    void addMore(List<RepairAdaptor.RepairWrapper> repairs);
}
