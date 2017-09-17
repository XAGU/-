package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.ILoginDataManager;
import com.xiaolian.amigo.data.network.ILoginApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.response.LoginRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.RegisterReqDTO;

import javax.inject.Inject;

import io.reactivex.Flowable;
import retrofit2.Retrofit;

/**
 * LoginDataManager实现类
 * @author zcd
 */

public class LoginDataManager implements ILoginDataManager {
    private static final String TAG = LoginDataManager.class.getSimpleName();

    private ILoginApi mLoginService;

    @Inject
    public LoginDataManager(Retrofit retrofit) {
        mLoginService = retrofit.create(ILoginApi.class);
    }

    @Override
    public Flowable<ApiResult<LoginRespDTO>> register(RegisterReqDTO registerReqDTO) {
        return mLoginService.register(registerReqDTO);
    }
}
