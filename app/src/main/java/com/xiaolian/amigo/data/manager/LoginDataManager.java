package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.ILoginDataManager;
import com.xiaolian.amigo.data.network.ILoginApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.login.LoginReqDTO;
import com.xiaolian.amigo.data.network.model.login.PasswordResetReqDTO;
import com.xiaolian.amigo.data.network.model.login.RegisterReqDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeCheckReqDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeGetReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.login.LoginRespDTO;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.di.UserServer;

import javax.inject.Inject;

import rx.Observable;
import retrofit2.Retrofit;
import retrofit2.http.Body;

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

    private ISharedPreferencesHelp sharedPreferencesHelp;

    @Inject
    public LoginDataManager(@UserServer Retrofit retrofit, ISharedPreferencesHelp sharedPreferencesHelp) {
        loginApi = retrofit.create(ILoginApi.class);
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
