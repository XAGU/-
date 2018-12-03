package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.alipay.AlipayAuthInfoRespDTO;
import com.xiaolian.amigo.data.network.model.alipay.AlipayBindReq;
import com.xiaolian.amigo.data.network.model.login.CancelThirdBindReqDTO;
import com.xiaolian.amigo.data.network.model.login.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.login.LoginReqDTO;
import com.xiaolian.amigo.data.network.model.login.PasswordResetReqDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeCheckReqDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeGetReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.login.LoginRespDTO;
import com.xiaolian.amigo.data.network.model.login.RegisterReqDTO;
import com.xiaolian.amigo.data.network.model.login.WeChatBindPhoneReqDTO;
import com.xiaolian.amigo.data.network.model.login.WeChatResiterReqDTO;
import com.xiaolian.amigo.data.network.model.login.WechatLoginReqDTO;
import com.xiaolian.amigo.data.network.model.user.PasswordCheckReqDTO;
import com.xiaolian.amigo.data.network.model.user.PasswordVerifyRespDTO;


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
     * 支付宝登录
     */
    @POST("user/alipay/bind/login")
    Observable<ApiResult<LoginRespDTO>> apayLogin(@Body AlipayBindReq body);

    @POST("user/alipay/bind/mobile/check")
    Observable<ApiResult<LoginRespDTO>> alipayCheckPhoneBind(@Body AlipayBindReq body);


    @POST("user/alipay/bind/register")
    Observable<ApiResult<LoginRespDTO>> registerAlipay(@Body AlipayBindReq body);

    @POST("user/alipay/bind/cancel")
    Observable<ApiResult<CancelThirdBindReqDTO>> aplipayUnbind();

    @POST("wechat/user/account/unbind")
    Observable<ApiResult<CancelThirdBindReqDTO>> weChatUnbind();

    /**
     * 获取支付宝authinfo
     */
    @POST("user/alipay/bind/authInfo")
    Observable<ApiResult<AlipayAuthInfoRespDTO>> getApayAuth();

    //wechat login
    @POST("wechat/user/account/public/login")
    Observable<ApiResult<LoginRespDTO>> weChatLogin(@Body WechatLoginReqDTO body);

    @POST("wechat/user/account/login/check")
    Observable<ApiResult<LoginRespDTO>> weChatCheckPhoneBind(@Body WeChatBindPhoneReqDTO body);

    @POST("wechat/user/account/register")
    Observable<ApiResult<LoginRespDTO>> registerWeChat(@Body WeChatResiterReqDTO body);
    //wechat login end

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
     * 修改电话号码
     */
    @POST("user/mobile/update")
    Observable<ApiResult<EntireUserDTO>> checkChangePhoneVerification(@Body VerificationCodeCheckReqDTO body);

    /**
     *登录后在app中的操作的密码验证
     */
    @POST("user/password/check")
    Observable<ApiResult<PasswordVerifyRespDTO>> verifyPassword(@Body PasswordCheckReqDTO body);

    /**
     * 校验重置密码验证码
     */
    @POST("login/password/reset/check")
    Observable<ApiResult<BooleanRespDTO>> verificationResetCheck(@Body VerificationCodeCheckReqDTO body);
}
