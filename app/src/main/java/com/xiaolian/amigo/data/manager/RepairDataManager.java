package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IRepairDataManager;
import com.xiaolian.amigo.data.network.IRepairApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.RatingRepairReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.RepairApplyReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.RepairDetailReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.RepairProblemReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.RepairReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.RepairApplyRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.RepairDetailRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.RepairProblemRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.RepairRespDTO;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.http.Body;

/**
 * 报修管理
 * <p>
 * Created by caidong on 2017/9/18.
 */
public class RepairDataManager implements IRepairDataManager{

    private static final String TAG = RepairDataManager.class.getSimpleName();

    private IRepairApi repairApi;

    @Inject
    public RepairDataManager(Retrofit retrofit) {
        repairApi = retrofit.create(IRepairApi.class);
    }

    @Override
    public Observable<ApiResult<RepairRespDTO>> queryRepairs(@Body RepairReqDTO reqDTO) {
        return repairApi.queryRepairs(reqDTO);
    }

    @Override
    public Observable<ApiResult<RepairDetailRespDTO>> queryRepairDetail(@Body RepairDetailReqDTO reqDTO) {
        return repairApi.queryRepairDetail(reqDTO);
    }

    @Override
    public Observable<ApiResult<RepairApplyRespDTO>> applyRepair(@Body RepairApplyReqDTO reqDTO) {
        return repairApi.applyRepair(reqDTO);
    }

    @Override
    public Observable<ApiResult<RepairProblemRespDTO>> queryRepairProblems(@Body RepairProblemReqDTO reqDTO) {
        return repairApi.queryRepairProblems(reqDTO);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> rateRapair(@Body RatingRepairReqDTO reqDTO) {
        return repairApi.rateRapair(reqDTO);
    }
}
