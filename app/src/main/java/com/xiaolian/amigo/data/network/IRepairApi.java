package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.repair.RatingRepairReqDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairApplyReqDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairDetailReqDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairProblemReqDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairApplyRespDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairDetailRespDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairProblemRespDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairRespDTO;

import rx.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 报修相关api
 */
public interface IRepairApi {

    // 查询个人报修列表
    @POST("repair/personal/list")
    Observable<ApiResult<RepairRespDTO>> queryRepairs(@Body RepairReqDTO reqDTO);

    // 查询报修单详情
    @POST("repair/one")
    Observable<ApiResult<RepairDetailRespDTO>> queryRepairDetail(@Body RepairDetailReqDTO reqDTO);

    // 申请报修
    @POST("repair/save")
    Observable<ApiResult<RepairApplyRespDTO>> applyRepair(@Body RepairApplyReqDTO reqDTO);

    // 获取报修问题列表
    @POST("repair/cause/list")
    Observable<ApiResult<RepairProblemRespDTO>> queryRepairProblems(@Body RepairProblemReqDTO reqDTO);

    // 维修评价
    @POST("repair/rating")
    Observable<ApiResult<BooleanRespDTO>> rateRapair(@Body RatingRepairReqDTO reqDTO);

    // 取消报修
    @POST("repair/cancel")
    Observable<ApiResult<BooleanRespDTO>> cancelRepair(@Body SimpleReqDTO reqDTO);
}
