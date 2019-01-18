package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.alipay.AlipayAuthInfoRespDTO;
import com.xiaolian.amigo.data.network.model.alipay.AlipayBindReq;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.login.LoginReqDTO;
import com.xiaolian.amigo.data.network.model.login.LoginRespDTO;
import com.xiaolian.amigo.data.network.model.login.PasswordResetReqDTO;
import com.xiaolian.amigo.data.network.model.login.RegisterReqDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeCheckReqDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeGetReqDTO;
import com.xiaolian.amigo.data.network.model.login.WeChatBindPhoneReqDTO;
import com.xiaolian.amigo.data.network.model.login.WeChatResiterReqDTO;
import com.xiaolian.amigo.data.network.model.login.WechatLoginReqDTO;
import com.xiaolian.amigo.data.network.model.version.CheckVersionUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.version.CheckVersionUpdateRespDTO;
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

    User getUserInfo();

    void setUserInfo(User user);

    void logout();

    String getRememberMobile();

    void setRememberMobile(String mobile);

    /**
     * 注册
     */
    Observable<ApiResult<LoginRespDTO>> register(@Body RegisterReqDTO body);

    /**
     * 登录
     */
    Observable<ApiResult<LoginRespDTO>> login(@Body LoginReqDTO body);

    /**
     * 支付宝登录
     */

    Observable<ApiResult<LoginRespDTO>> apayLogin(@Body AlipayBindReq body);

    /**
     *检查手机是否绑定
     */
    Observable<ApiResult<LoginRespDTO>> alipayCheckPhoneBind(@Body AlipayBindReq body);


    /**
     *注册新手机
     */
    Observable<ApiResult<LoginRespDTO>> registerAlipay(@Body AlipayBindReq body);

                                                                       /**
     * 密码重置
     */
    Observable<ApiResult<BooleanRespDTO>> passwordReset(@Body PasswordResetReqDTO body);

    /**
     * 校验注册验证码
     */
    Observable<ApiResult<BooleanRespDTO>> verificationCheck(@Body VerificationCodeCheckReqDTO body);

    /**
     * 获取验证码
     */
    Observable<ApiResult<BooleanRespDTO>> getVerification(@Body VerificationCodeGetReqDTO body);

    /**
     * 校验重置密码验证码
     */
    Observable<ApiResult<BooleanRespDTO>> verificationResetCheck(@Body VerificationCodeCheckReqDTO body);

    /**
     * 获取支付宝authinfo
     */
    Observable<ApiResult<AlipayAuthInfoRespDTO>> getApayAuth();

    //wechat login
    Observable<ApiResult<LoginRespDTO>> weChatLogin(WechatLoginReqDTO body);

    Observable<ApiResult<LoginRespDTO>> weChatCheckPhoneBind(WeChatBindPhoneReqDTO body);

    Observable<ApiResult<LoginRespDTO>>  registerWeChat(WeChatResiterReqDTO body);

    //wechat login end

    void deletePushToken();

    Long getSchoolId();

    String getPushTag();

    void setPushTag(String pushTag);

    void setIsFirstAfterLogin(boolean b);

    void setAccessToken(String accessToken);

    void setRefreshToken(String refreshToken);

    /**
     * 检查是否需要版本更新
     * @param reqDTO
     * @return
     */
    Observable<ApiResult<CheckVersionUpdateRespDTO>> checkUpdate(CheckVersionUpdateReqDTO reqDTO);

    /**
     * 上一次更新时间
     * @return
     */
    Long getLastUpdateRemindTime();

    void setLastUpdateRemindTime();
}
