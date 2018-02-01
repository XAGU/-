package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.login.LoginReqDTO;
import com.xiaolian.amigo.data.network.model.login.PasswordResetReqDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeCheckReqDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeGetReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.login.LoginRespDTO;
import com.xiaolian.amigo.data.network.model.login.RegisterReqDTO;


import rx.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Api接口
 *
 * @author zcd
 * @date 17/9/14
 */

public interface ILoginApi {

    /**
     * 注册
     */
    @POST("login/register")
    Observable<ApiResult<LoginRespDTO>> register(@Body RegisterReqDTO body);

    /**
     * 登录
     */
    @POST("login")
    Observable<ApiResult<LoginRespDTO>> login(@Body LoginReqDTO body);

    /**
     * 密码重置
     */
    @POST("login/password/reset")
    Observable<ApiResult<BooleanRespDTO>> passwordReset(@Body PasswordResetReqDTO body);

    /**
     * 校验注册验证码
     */
    @POST("login/register/check")
    Observable<ApiResult<BooleanRespDTO>> verificationCheck(@Body VerificationCodeCheckReqDTO body);

    /**
     * 获取验证码
     */
    @POST("login/verification/one")
    Observable<ApiResult<BooleanRespDTO>> getVerification(@Body VerificationCodeGetReqDTO body);

    /**
     * 校验重置密码验证码
     */
    @POST("login/password/reset/check")
    Observable<ApiResult<BooleanRespDTO>> verificationResetCheck(@Body VerificationCodeCheckReqDTO body);
}
