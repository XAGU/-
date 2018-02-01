package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.timerange.QueryTimeValidReqDTO;
import com.xiaolian.amigo.data.network.model.timerange.QueryTimeValidRespDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 时间段相关api
 *
 * @author zcd
 * @date 17/12/14
 */

public interface ITimeRangeApi {
    /**
     * 查询提现时间段
     */
    @POST("time/range/withdraw")
    Observable<ApiResult<QueryTimeValidRespDTO>> queryWithDrawTimeValid();

    /**
     * 查询热水澡热水供应时间段
     */
    @POST("time/range/water")
    Observable<ApiResult<QueryTimeValidRespDTO>> queryWaterTimeValid(@Body QueryTimeValidReqDTO reqDTO);
}
