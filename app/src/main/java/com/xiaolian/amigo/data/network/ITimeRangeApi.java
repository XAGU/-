package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.timerange.QueryTimeValidRespDTO;

import retrofit2.http.POST;
import rx.Observable;

/**
 * 时间段相关api
 * <p>
 * Created by zcd on 17/12/14.
 */

public interface ITimeRangeApi {
    // 查询提现时间段
    @POST("time/range/withdraw")
    Observable<ApiResult<QueryTimeValidRespDTO>> queryWithDrawTimeValid();
}
