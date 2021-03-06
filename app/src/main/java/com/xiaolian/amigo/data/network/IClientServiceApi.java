package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.cs.CsMobileRespDTO;

import retrofit2.http.POST;
import rx.Observable;

/**
 * @author caidong
 * @date 17/11/2
 */
public interface IClientServiceApi {

    /**
     * 获取客服人员电话
     */
    @POST("cs/mobile/one")
    Observable<ApiResult<CsMobileRespDTO>> queryCsInfo();

}
