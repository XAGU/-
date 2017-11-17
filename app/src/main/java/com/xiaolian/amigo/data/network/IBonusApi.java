package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.QueryUserBonusReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.RedeemBonusReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryUserBonusListRespDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 代金券相关接口
 *
 * @author zcd
 */
public interface IBonusApi {
    // 获取代金券列表
    @POST("/bonus/personal/list")
    Observable<ApiResult<QueryUserBonusListRespDTO>> queryOrders(@Body QueryUserBonusReqDTO reqDTO);

    // 兑换代金券
    @POST("/bonus/redeem")
    Observable<ApiResult<BooleanRespDTO>> redeem(@Body RedeemBonusReqDTO reqDTO);
}
