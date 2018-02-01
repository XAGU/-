package com.xiaolian.amigo.ui.repair;

import com.xiaolian.amigo.data.manager.intf.IRepairDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.repair.RatingRepairReqDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairEvaluationPresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairEvaluationView;

import java.util.List;

import javax.inject.Inject;

/**
 * 设备报修评价
 *
 * @author zcd
 * @date 17/9/21
 */

public class RepairEvaluationPresenter<V extends IRepairEvaluationView> extends BasePresenter<V>
        implements IRepairEvaluationPresenter<V> {
    @SuppressWarnings("unused")
    private static final String TAG = RepairEvaluationPresenter.class.getSimpleName();
    private IRepairDataManager repairDataManager;

    @Inject
    RepairEvaluationPresenter(IRepairDataManager repairDataManager) {
        super();
        this.repairDataManager = repairDataManager;
    }

    @Override
    public void rateRepair(String content, Integer rating, List<Integer> ratingOptions, Long repairId) {
        RatingRepairReqDTO dto = new RatingRepairReqDTO();
        dto.setContent(content);
        dto.setRating(rating);
        dto.setRatingOptions(ratingOptions);
        dto.setRepairId(repairId);
        addObserver(repairDataManager.rateRapair(dto), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isResult()) {
                        getMvpView().onSuccess("评价成功");
                        getMvpView().finishView();
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
