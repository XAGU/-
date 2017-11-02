package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.IFileApi;
import com.xiaolian.amigo.data.network.IUserApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.BindResidenceReq;
import com.xiaolian.amigo.data.network.model.dto.request.MobileUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.PasswordCheckReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.PasswordUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.PersonalUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.QueryDeviceListReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.QueryResidenceListReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.VerificationCodeGetReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.DeleteResidenceRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.dto.response.PersonalExtraInfoDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryAvatarDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryBriefSchoolListRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryDeviceListRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QuerySchoolBizListRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryUserResidenceListRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.ResidenceListRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.UserResidenceInListDTO;
import com.xiaolian.amigo.data.network.model.user.User;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Part;

/**
 * User模块DataManager
 * @author zcd
 */

public class UserDataManager implements IUserDataManager {
    private static final String TAG = UserDataManager.class.getSimpleName();

    private IUserApi userApi;

    private IFileApi fileApi;

    private ISharedPreferencesHelp sharedPreferencesHelp;

    @Inject
    public UserDataManager(Retrofit retrofit, ISharedPreferencesHelp sharedPreferencesHelp) {
        userApi = retrofit.create(IUserApi.class);
        fileApi = retrofit.create(IFileApi.class);
        this.sharedPreferencesHelp = sharedPreferencesHelp;
    }

    @Override
    public Observable<ApiResult<QueryAvatarDTO>> getAvatarList() {
        return userApi.getAvatarList();
    }

    @Override
    public Observable<ApiResult<EntireUserDTO>> getUserInfo() {
        return userApi.getUserInfo();
    }

    @Override
    public Observable<ApiResult<PersonalExtraInfoDTO>> getUserExtraInfo() {
        return userApi.getUserExtraInfo();
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

    @Override
    public Observable<ApiResult<QueryUserResidenceListRespDTO>> queryUserResidenceList(@Body SimpleQueryReqDTO body) {
        return userApi.queryUserResidenceList(body);
    }

    @Override
    public Observable<ApiResult<DeleteResidenceRespDTO>> deleteResidence(@Body SimpleReqDTO body) {
        return userApi.deleteResidence(body);
    }

    @Override
    public Observable<ApiResult<ResidenceListRespDTO>> queryResidenceList(@Body QueryResidenceListReqDTO body) {
        return userApi.queryResidenceList(body);
    }

    @Override
    public Observable<ApiResult<UserResidenceInListDTO>> bindResidence(@Body BindResidenceReq body) {
        return userApi.bindResidence(body);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> checkPasswordValid(@Body PasswordCheckReqDTO reqDTO) {
        return userApi.checkPasswordValid(reqDTO);
    }

    @Override
    public Observable<ApiResult<String>> uploadFile(@Part("file") RequestBody images) {

        return fileApi.uploadFile(images);
    }

    @Override
    public User getUser() {
        return sharedPreferencesHelp.getUserInfo();
    }

    @Override
    public void setUser(User user) {
        sharedPreferencesHelp.setUserInfo(user);
    }

    @Override
    public void logout() {
        sharedPreferencesHelp.logout();
    }
}
