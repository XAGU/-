package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.LoginRespDTO;

import io.reactivex.Flowable;
import retrofit2.http.POST;

/**
 * Api接口
 * @author zcd
 */

public interface ILoginService {
    @POST("/login/register")
    Flowable<ApiResult<LoginRespDTO>> register(String code, String mobile, String password, String schoolld);
}
