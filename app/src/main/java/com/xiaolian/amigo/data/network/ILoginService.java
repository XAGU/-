package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.response.LoginRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.RegisterReqDTO;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Api接口
 * @author zcd
 */

public interface ILoginService {
    @POST("/login/register")
    Flowable<ApiResult<LoginRespDTO>> register(@Body RegisterReqDTO body);
}
