package com.xiaolian.amigo.ui.repair.intf;

import com.xiaolian.amigo.data.network.model.dto.response.RepairDetailRespDTO;
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

    /**
     * 渲染报修详情
     *
     * @param detail 报修详情
     */
    void render(RepairDetailRespDTO detail);
}
