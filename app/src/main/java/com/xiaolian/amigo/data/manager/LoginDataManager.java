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

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.http.Body;

/**
 * LoginDataManager实现类
 * @author zcd
 */

public class LoginDataManager implements ILoginDataManager {
    private static final String TAG = LoginDataManager.class.getSimpleName();

    private ILoginApi loginApi;

    @Inject
    public LoginDataManager(Retrofit retrofit) {
        loginApi = retrofit.create(ILoginApi.class);
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
}
