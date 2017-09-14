package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.LoginRespDTO;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Api接口
 * @author zcd
 */

public interface ILoginService {
    @POST("/login/register")
    @FormUrlEncoded
    Flowable<ApiResult<LoginRespDTO>> register(@Field("code") String code,
                                               @Field("mobile") String mobile,
                                               @Field("password") String password,
                                               @Field("schoolId") String schoolId);
}
