package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.credits.CreditsExchangeReqDTO;
import com.xiaolian.amigo.data.network.model.credits.CreditsRuleRespDTO;

import retrofit2.http.Body;
import rx.Observable;

/**
 * 积分兑换模块
 * @author zcd
 * @date 18/2/26
 */

public interface ICreditsDataManager {
    /**
     * 获取积分兑换规则
     */
    Observable<ApiResult<CreditsRuleRespDTO>> getRules();

    /**
     * 兑换积分
     */
    Observable<ApiResult<BooleanRespDTO>> exchange(@Body CreditsExchangeReqDTO reqDTO);

    /**
     * 获取缓存中的积分
     * @return 积分
     */
    int getCredits();

    /**
     * 缓存积分
     * @param credits 积分
     */
    void setCredits(int credits);
}
