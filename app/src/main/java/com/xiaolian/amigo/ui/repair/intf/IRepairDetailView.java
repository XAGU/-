package com.xiaolian.amigo.ui.repair.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.repair.adaptor.RepairAdaptor;
import com.xiaolian.amigo.ui.repair.adaptor.RepairProgressAdaptor;

import java.util.List;

/**
 * Created by caidong on 2017/9/18.
 */
public interface IRepairDetailView extends IBaseView {

    /**
     * 刷新报修进度列表
     *
     * @param progresses 待添加的报修进度
     */
    void addMoreProgresses(List<RepairProgressAdaptor.ProgressWrapper> progresses);
}
