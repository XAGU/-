package com.xiaolian.amigo.ui.repair.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

import java.util.List;

/**
 * 设备报修评价
 *
 * @author zcd
 * @date 17/9/21
 */

public interface IRepairEvaluationPresenter<V extends IRepairEvaluationView> extends IBasePresenter<V> {
    /**
     * 报修评价
     *
     * @param content       内容
     * @param rating        评价等级
     * @param ratingOptions 评价选项
     * @param repairId      报修id
     */
    void rateRepair(String content, Integer rating, List<Integer> ratingOptions, Long repairId);
}
