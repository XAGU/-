package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.credits.CreditsExchangeReqDTO;
import com.xiaolian.amigo.data.network.model.credits.CreditsRuleRespDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 积分模块
 * @author zcd
 * @date 18/2/26
 */

public interface ICreditsApi {
    /**
     * 获取积分兑换规则
     */
    @POST("credits/rules")
    Observable<ApiResult<CreditsRuleRespDTO>> getRules();

    /**
     * 兑换积分
     */
    @POST("credits/exchange")
    Observable<ApiResult<BooleanRespDTO>> exchange(@Body CreditsExchangeReqDTO reqDTO);
}
