package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.complaint.CheckComplaintReqDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 投诉api
 * <p>
 * Created by zcd on 17/12/14.
 */

public interface IComplaintApi {
    // 投诉查重
    @POST("complaint/check")
    Observable<ApiResult<BooleanRespDTO>> checkComplaint(@Body CheckComplaintReqDTO reqDTO);
}
