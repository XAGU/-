package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.RatingRepairReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.RemindReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.RepairApplyReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.RepairDetailReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.RepairProblemReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.RepairReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.RepairApplyRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.RepairDetailRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.RepairProblemRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.RepairRespDTO;

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

    // 提醒客服
    @POST("cs/remind")
    Observable<ApiResult<BooleanRespDTO>> remind(@Body RemindReqDTO reqDTO);

    // 取消报修
    @POST("repair/cancel")
    Observable<ApiResult<BooleanRespDTO>> cancelRepair(@Body SimpleReqDTO reqDTO);
}
