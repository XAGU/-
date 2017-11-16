package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.ILoginDataManager;
import com.xiaolian.amigo.data.network.ILoginApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.LoginReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.PasswordResetReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.RegisterReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.VerificationCodeCheckReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.VerificationCodeGetReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.LoginRespDTO;
import com.xiaolian.amigo.data.network.model.user.User;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;

import javax.inject.Inject;

import rx.Observable;
import retrofit2.Retrofit;
import retrofit2.http.Body;

/**
 * LoginDataManager实现类
 * @author zcd
 */

public class LoginDataManager implements ILoginDataManager {
    private static final String TAG = LoginDataManager.class.getSimpleName();

    private ILoginApi loginApi;


    private ISharedPreferencesHelp sharedPreferencesHelp;

    @Inject
    public LoginDataManager(Retrofit retrofit, ISharedPreferencesHelp sharedPreferencesHelp) {
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
    public String getToken() {
        return sharedPreferencesHelp.getToken();
    }

    @Override
    public void setToken(String token) {
        sharedPreferencesHelp.setToken(token);
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
