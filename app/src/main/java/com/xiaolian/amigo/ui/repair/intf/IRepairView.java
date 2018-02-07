package com.xiaolian.amigo.ui.repair.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseListView;
import com.xiaolian.amigo.ui.repair.adaptor.RepairAdaptor;

import java.util.List;

/**
 * 报修记录
 *
 * @author caidong
 * @date 17/9/18
 */
public interface IRepairView extends IBaseListView {

    /**
     * 刷新报修记录
     *
     * @param repairs 待添加的报修记录
     */
    void addMore(List<RepairAdaptor.RepairWrapper> repairs);

    /**
     * 获取当前页码
     *
     * @return 当前页码
     */
    int getPage();
}
