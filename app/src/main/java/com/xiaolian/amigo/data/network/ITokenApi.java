package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;

import retrofit2.http.POST;
import rx.Observable;
import rx.Observer;

public interface ITokenApi {

    /**
     * 移除token
     * @return
     */
    @POST("/token/revoke")
    Observable<ApiResult<Void>>  revokeToken();
}
