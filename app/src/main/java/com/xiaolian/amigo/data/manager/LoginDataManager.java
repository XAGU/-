package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.ILoginDataManager;
import com.xiaolian.amigo.data.network.ILoginApi;
import com.xiaolian.amigo.data.network.ISystemApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.alipay.AlipayAuthInfoRespDTO;
import com.xiaolian.amigo.data.network.model.alipay.AlipayBindReq;
import com.xiaolian.amigo.data.network.model.login.LoginReqDTO;
import com.xiaolian.amigo.data.network.model.login.PasswordResetReqDTO;
import com.xiaolian.amigo.data.network.model.login.RegisterReqDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeCheckReqDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeGetReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.login.LoginRespDTO;
import com.xiaolian.amigo.data.network.model.login.WeChatBindPhoneReqDTO;
import com.xiaolian.amigo.data.network.model.login.WeChatResiterReqDTO;
import com.xiaolian.amigo.data.network.model.login.WechatLoginReqDTO;
import com.xiaolian.amigo.data.network.model.version.CheckVersionUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.version.CheckVersionUpdateRespDTO;
import com.xiaolian.amigo.data.network.model.version.VersionDialogTime;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.di.UserServer;

import javax.inject.Inject;

import rx.Observable;
import retrofit2.Retrofit;
import retrofit2.http.Body;

import static com.xiaolian.amigo.util.StringUtils.appendToken;

/**
 * LoginDataManager实现类
 *
 * @author zcd
 * @date 17/9/14
 */

public class LoginDataManager implements ILoginDataManager {
    @SuppressWarnings("unused")
    private static final String TAG = LoginDataManager.class.getSimpleName();

    private ILoginApi loginApi;

    private ISystemApi systemApi ;

    private ISharedPreferencesHelp sharedPreferencesHelp;

    @Inject
    public LoginDataManager(@UserServer Retrofit retrofit, ISharedPreferencesHelp sharedPreferencesHelp) {
        loginApi = retrofit.create(ILoginApi.class);
        systemApi = retrofit.create(ISystemApi.class);
        this.sharedPreferencesHelp = sharedPreferencesHelp;
    }

    @Override
    public Observable<ApiResult<LoginRespDTO>> register(@Body RegisterReqDTO body) {
        return loginApi.register(body);
    }

    @Override
    public Observable<ApiResult<LoginRespDTO>> login(@Body LoginReqDTO body) {
        return loginApi.login(body);
    }

    @Override
    public Observable<ApiResult<AlipayAuthInfoRespDTO>> getApayAuth() {
        return loginApi.getApayAuth();
    }
    // alipay登录接口
    @Override
    public Observable<ApiResult<LoginRespDTO>> apayLogin(@Body AlipayBindReq body) {
        return loginApi.apayLogin(body);
    }

    @Override
    public Observable<ApiResult<LoginRespDTO>> alipayCheckPhoneBind(@Body AlipayBindReq body) {
        return loginApi.alipayCheckPhoneBind(body);
    }


    @Override
    public VersionDialogTime getLastUpdateRemindTime() {
        return sharedPreferencesHelp.getLastUpdateRemindTime();
    }

    @Override
    public void setLastUpdateRemindTime(String mobile) {
        sharedPreferencesHelp.setLastUpdateRemindTime(mobile);
    }

    @Override
    public void clearUpdateRemindTime() {
        sharedPreferencesHelp.clearUpdateRemindTime();
    }

    @Override
    public void setToken(String accessToken, String refreshToken) {
//        sharedPreferencesHelp.setAppendToken(appendToken(accessToken , refreshToken));
    }

    @Override
    public Observable<ApiResult<LoginRespDTO>> registerAlipay(@Body AlipayBindReq body) {
        return loginApi.registerAlipay(body);
    }
    //alipay登录接口end

    //wechat login begin
    @Override
    public Observable<ApiResult<LoginRespDTO>> weChatLogin(@Body WechatLoginReqDTO body) {
        return loginApi.weChatLogin(body);
    }

    @Override
    public Observable<ApiResult<LoginRespDTO>> weChatCheckPhoneBind(@Body WeChatBindPhoneReqDTO body){
        return loginApi.weChatCheckPhoneBind(body);
    }

    @Override
    public Observable<ApiResult<LoginRespDTO>> registerWeChat(WeChatResiterReqDTO body){
        return loginApi.registerWeChat(body);
    }



    //wechat login end

    @Override
    public Observable<ApiResult<BooleanRespDTO>> passwordReset(@Body PasswordResetReqDTO body) {
        return loginApi.passwordReset(body);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> verificationCheck(@Body VerificationCodeCheckReqDTO body) {
        return loginApi.verificationCheck(body);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> getVerification(@Body VerificationCodeGetReqDTO body) {
        return loginApi.getVerification(body);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> verificationResetCheck(@Body VerificationCodeCheckReqDTO body) {
        return loginApi.verificationResetCheck(body);
    }

    @Override
    public void deletePushToken() {
        sharedPreferencesHelp.setPushToken("");
    }

    @Override
    public Long getSchoolId() {
        return sharedPreferencesHelp.getUserInfo().getSchoolId();
    }

    @Override
    public String getPushTag() {
        return sharedPreferencesHelp.getPushTag();
    }

    @Override
    public void setPushTag(String pushTag) {
        sharedPreferencesHelp.setPushToken(pushTag);
    }

    @Override
    public void setIsFirstAfterLogin(boolean b) {
        sharedPreferencesHelp.setIsFirstAfterLogin(b);
    }

    @Override
    public void setAccessToken(String accessToken) {
        sharedPreferencesHelp.setAccessToken(accessToken);
    }

    @Override
    public void setRefreshToken(String refreshToken) {
        sharedPreferencesHelp.setReferToken(refreshToken);
    }

    @Override
    public Observable<ApiResult<CheckVersionUpdateRespDTO>> checkUpdate(CheckVersionUpdateReqDTO reqDTO) {
        return systemApi.checkUpdate(reqDTO);
    }


    @Override
    public User getUserInfo() {
        return sharedPreferencesHelp.getUserInfo();
    }

    @Override
    public void setUserInfo(User user) {
        sharedPreferencesHelp.setUserInfo(user);
    }

    @Override
    public void logout() {
        sharedPreferencesHelp.logout();
    }

    @Override
    public String getRememberMobile() {
        return sharedPreferencesHelp.getRememberMobile();
    }

    @Override
    public void setRememberMobile(String mobile) {
        sharedPreferencesHelp.setRememberMobile(mobile);
    }
}
