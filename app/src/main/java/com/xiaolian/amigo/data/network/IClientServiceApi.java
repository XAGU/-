package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.response.CsMobileRespDTO;

import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by caidong on 2017/11/2.
 */
public interface IClientServiceApi {

    // 获取客服人员电话
    @POST("cs/mobile/one")
    Observable<ApiResult<CsMobileRespDTO>> queryCsInfo();

}
