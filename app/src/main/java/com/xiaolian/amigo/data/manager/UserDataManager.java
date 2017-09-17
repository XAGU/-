package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.IUserApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.MobileUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.PasswordUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.PersonalUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleQueryReqDTO;
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

    private IUserApi mUserService;

    @Inject
    public UserDataManager(Retrofit retrofit) {
        mUserService = retrofit.create(IUserApi.class);
    }

    @Override
    public Observable<ApiResult<EntireUserDTO>> getUserInfo() {
        return mUserService.getUserInfo();
    }

    @Override
    public Observable<ApiResult<EntireUserDTO>> updateUserInfo(@Body PersonalUpdateReqDTO body) {
        return mUserService.updateUserInfo(body);
    }

    @Override
    public Observable<ApiResult<EntireUserDTO>> updateMobile(@Body MobileUpdateReqDTO body) {
        return mUserService.updateMobile(body);
    }

    @Override
    public Observable<ApiResult<SimpleRespDTO>> updatePassword(@Body PasswordUpdateReqDTO body) {
        return mUserService.updatePassword(body);
    }

    @Override
    public Observable<ApiResult<QueryBriefSchoolListRespDTO>> getSchoolList(@Body SimpleQueryReqDTO body) {
        return mUserService.getSchoolList(body);
    }

    @Override
    public Observable<ApiResult<QuerySchoolBizListRespDTO>> getSchoolBizList() {
        return mUserService.getSchoolBizList();
    }
}
