package com.xiaolian.amigo.ui.repair.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

import java.util.List;

/**
 * 设备报修评价
 * <p>
 * Created by zcd on 9/21/17.
 */

public interface IRepairEvaluationPresenter<V extends IRepairEvaluationView> extends IBasePresenter<V> {
    void rateRepair(String content, Integer rating, List<Integer> ratingOptions, Long repairId);
}
