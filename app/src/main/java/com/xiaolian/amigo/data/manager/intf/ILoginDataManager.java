package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.login.LoginReqDTO;
import com.xiaolian.amigo.data.network.model.login.LoginRespDTO;
import com.xiaolian.amigo.data.network.model.login.PasswordResetReqDTO;
import com.xiaolian.amigo.data.network.model.login.RegisterReqDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeCheckReqDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeGetReqDTO;
import com.xiaolian.amigo.data.vo.User;

import retrofit2.http.Body;
import rx.Observable;

/**
 * LoginDataManager接口
 *
 * @author zcd
 * @date 17/9/14
 */
public interface ILoginDataManager {
    String getToken();

    void setToken(String token);

    User getUserInfo();

    void setUserInfo(User user);

    void logout();

    String getRememberMobile();

    void setRememberMobile(String mobile);

    // 注册
    Observable<ApiResult<LoginRespDTO>> register(@Body RegisterReqDTO body);

    // 登录
    Observable<ApiResult<LoginRespDTO>> login(@Body LoginReqDTO body);

    // 密码重置
    Observable<ApiResult<BooleanRespDTO>> passwordReset(@Body PasswordResetReqDTO body);

    // 校验注册验证码
    Observable<ApiResult<BooleanRespDTO>> verificationCheck(@Body VerificationCodeCheckReqDTO body);

    // 获取验证码
    Observable<ApiResult<BooleanRespDTO>> getVerification(@Body VerificationCodeGetReqDTO body);

    // 校验重置密码验证码
    Observable<ApiResult<BooleanRespDTO>> verificationResetCheck(@Body VerificationCodeCheckReqDTO body);
}
