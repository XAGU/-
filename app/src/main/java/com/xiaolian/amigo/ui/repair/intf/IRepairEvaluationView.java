package com.xiaolian.amigo.ui.repair.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 设备报修评价
 *
 * @author zcd
 * @date 17/9/21
 */

public interface IRepairEvaluationView extends IBaseView {
    /**
     * 结束页面
     */
    void finishView();

    /**
     * 评价成功
     */
    void repairSuccess();
    }
