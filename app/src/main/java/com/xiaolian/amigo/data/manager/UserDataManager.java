package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.IUserApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.MobileUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.PasswordUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.PersonalUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.VerificationCodeGetReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryBriefSchoolListRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QuerySchoolBizListRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.SimpleRespDTO;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.http.Body;

/**
 * User模块DataManager
 * @author zcd
 */

public class UserDataManager implements IUserDataManager {
    private static final String TAG = UserDataManager.class.getSimpleName();

    private IUserApi userApi;

    @Inject
    public UserDataManager(Retrofit retrofit) {
        userApi = retrofit.create(IUserApi.class);
    }

    @Override
    public Observable<ApiResult<EntireUserDTO>> getUserInfo() {
        return userApi.getUserInfo();
    }

    @Override
    public Observable<ApiResult<EntireUserDTO>> updateUserInfo(@Body PersonalUpdateReqDTO body) {
        return userApi.updateUserInfo(body);
    }

    @Override
    public Observable<ApiResult<EntireUserDTO>> updateMobile(@Body MobileUpdateReqDTO body) {
        return userApi.updateMobile(body);
    }

    @Override
    public Observable<ApiResult<SimpleRespDTO>> updatePassword(@Body PasswordUpdateReqDTO body) {
        return userApi.updatePassword(body);
    }

    @Override
    public Observable<ApiResult<QueryBriefSchoolListRespDTO>> getSchoolList(@Body SimpleQueryReqDTO body) {
        return userApi.getSchoolList(body);
    }

    @Override
    public Observable<ApiResult<QuerySchoolBizListRespDTO>> getSchoolBizList() {
        return userApi.getSchoolBizList();
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> getVerifyCode(VerificationCodeGetReqDTO body) {
        return userApi.getVerifyCode(body);
    }
}
