package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.network.ILoginService;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.LoginRespDTO;

import javax.inject.Inject;

import io.reactivex.Flowable;
import retrofit2.Retrofit;

/**
 * LoginDataManager实现类
 * @author zcd
 */

public class LoginDataManager implements ILoginDataManager {
    private static final String TAG = LoginDataManager.class.getSimpleName();

    private ILoginService mLoginService;

    @Inject
    public LoginDataManager(Retrofit retrofit) {
        mLoginService = retrofit.create(ILoginService.class);
    }

    @Override
    public Flowable<ApiResult<LoginRespDTO>> register(String code, String mobile, String password, String schoolld) {
        return mLoginService.register(code, mobile, password, schoolld);
    }
}
