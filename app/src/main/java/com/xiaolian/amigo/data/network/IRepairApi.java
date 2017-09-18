package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.OrderReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.RepairReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.LoginRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.OrderRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.RegisterReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.RepairRespDTO;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 报修相关api
 */
public interface IRepairApi {

    // 查询个人报修列表
    @POST("/repair/personal/list")
    Observable<ApiResult<RepairRespDTO>> queryRepairs(@Body RepairReqDTO reqDTO);

}
