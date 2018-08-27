package com.xiaolian.amigo.data.manager;

import android.support.v4.util.ObjectsCompat;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.IBathroomApi;
import com.xiaolian.amigo.data.network.IFileApi;
import com.xiaolian.amigo.data.network.ILoginApi;
import com.xiaolian.amigo.data.network.IOssApi;
import com.xiaolian.amigo.data.network.IResidenceApi;
import com.xiaolian.amigo.data.network.ISchoolApi;
import com.xiaolian.amigo.data.network.IUserApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathPasswordUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.RecordResidenceReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.common.EmptyRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.file.OssModel;
import com.xiaolian.amigo.data.network.model.login.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeCheckReqDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeGetReqDTO;
import com.xiaolian.amigo.data.network.model.residence.QueryResidenceListReqDTO;
import com.xiaolian.amigo.data.network.model.residence.ResidenceListRespDTO;
import com.xiaolian.amigo.data.network.model.school.QueryBriefSchoolListRespDTO;
import com.xiaolian.amigo.data.network.model.school.QuerySchoolBizListRespDTO;
import com.xiaolian.amigo.data.network.model.school.QuerySchoolListReqDTO;
import com.xiaolian.amigo.data.network.model.user.BindResidenceReq;
import com.xiaolian.amigo.data.network.model.user.BriefSchoolBusiness;
import com.xiaolian.amigo.data.network.model.user.DeleteResidenceRespDTO;
import com.xiaolian.amigo.data.network.model.user.MobileUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.user.PasswordCheckReqDTO;
import com.xiaolian.amigo.data.network.model.user.PasswordUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.user.PersonalExtraInfoDTO;
import com.xiaolian.amigo.data.network.model.user.PersonalUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.user.QueryAvatarDTO;
import com.xiaolian.amigo.data.network.model.user.QueryUserResidenceListRespDTO;
import com.xiaolian.amigo.data.network.model.user.UserResidenceDTO;
import com.xiaolian.amigo.data.network.model.user.UserResidenceInListDTO;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.di.BathroomServer;
import com.xiaolian.amigo.di.UserServer;

import java.util.List;

import javax.inject.Inject;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Part;
import rx.Observable;

/**
 * User模块DataManager
 *
 * @author zcd
 * @date 17/9/15
 */

public class UserDataManager implements IUserDataManager {
    @SuppressWarnings("unused")
    private static final String TAG = UserDataManager.class.getSimpleName();
    private IUserApi userApi;
    private IResidenceApi residenceApi;
    private ISchoolApi schoolApi;
    private IFileApi fileApi;
    private ILoginApi loginApi;
    private IOssApi ossApi;
    private IBathroomApi bathroomApi;
    private ISharedPreferencesHelp sharedPreferencesHelp;

    @Inject
    public UserDataManager(@BathroomServer Retrofit bathroomRetrofit, @UserServer Retrofit retrofit, ISharedPreferencesHelp sharedPreferencesHelp) {
        residenceApi = retrofit.create(IResidenceApi.class);
        schoolApi = retrofit.create(ISchoolApi.class);
        userApi = retrofit.create(IUserApi.class);
        fileApi = retrofit.create(IFileApi.class);
        loginApi = retrofit.create(ILoginApi.class);
        ossApi = retrofit.create(IOssApi.class);
        this.sharedPreferencesHelp = sharedPreferencesHelp;
        bathroomApi = bathroomRetrofit.create(IBathroomApi.class);
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
    public Observable<ApiResult<QueryBriefSchoolListRespDTO>> getSchoolList(@Body QuerySchoolListReqDTO body) {
        return schoolApi.getSchoolList(body);
    }

    @Override
    public Observable<ApiResult<QuerySchoolBizListRespDTO>> getSchoolBizList() {
        return schoolApi.getSchoolBizList();
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> getVerifyCode(VerificationCodeGetReqDTO body) {
        return loginApi.getVerification(body);
    }
    @Deprecated
    @Override
    public Observable<ApiResult<QueryUserResidenceListRespDTO>> queryUserResidenceList() {
        return userApi.queryUserResidenceList(new SimpleQueryReqDTO());
    }

//    @Override
    public Observable<ApiResult<QueryUserResidenceListRespDTO>> queryUserResidenceList(@Body SimpleQueryReqDTO body) {
        return userApi.queryUserResidenceList(body);
    }

    @Override
    public Observable<ApiResult<DeleteResidenceRespDTO>> deleteResidence(@Body SimpleReqDTO body) {
        return userApi.deleteResidence(body);
    }

    @Override
    public Observable<ApiResult<ResidenceListRespDTO>> queryResidenceList(@Body QueryResidenceListReqDTO body) {
        return residenceApi.queryResidenceList(body);
    }

    @Deprecated
    @Override
    public Observable<ApiResult<UserResidenceInListDTO>> bindResidence(@Body BindResidenceReq body) {
        return userApi.bindResidence(body);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> checkPasswordValid(@Body PasswordCheckReqDTO reqDTO) {
        return userApi.checkPasswordValid(reqDTO);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> changeSchoolCheck() {
        return userApi.changeSchoolCheck();
    }

    @Override
    public Observable<ApiResult<UserResidenceDTO>> queryResidenceDetail(SimpleReqDTO reqDTO) {
        return userApi.queryResidenceDetail(reqDTO);
    }

    @Override
    public Observable<ApiResult<OssModel>> getOssModel() {
        return ossApi.getOssModel();
    }

    @Override
    public boolean isExistBathroomBiz() {
        if (sharedPreferencesHelp.getSchoolBiz() !=null && sharedPreferencesHelp.getSchoolBiz().size() > 0) {
            for (BriefSchoolBusiness biz : sharedPreferencesHelp.getSchoolBiz()) {
                if (ObjectsCompat.equals(biz.getBusinessId(), 5L)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> getVerification(VerificationCodeGetReqDTO body) {
        return loginApi.getVerification(body);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> checkVerifyCode(VerificationCodeCheckReqDTO reqDTO) {
        return bathroomApi.checkVerifyCode(reqDTO);
    }

    @Override
    public Observable<ApiResult<SimpleRespDTO>> updateBathroomPassword(BathPasswordUpdateReqDTO reqDTO) {
        return bathroomApi.updateBathroomPassword(reqDTO);
    }

    @Override
    public List<String> getBathroomPasswordDesc() {
        return sharedPreferencesHelp.getBathPasswordDescription();
    }


    @Override
    public Observable<ApiResult<QueryUserResidenceListRespDTO>> bathList(EmptyRespDTO dto) {
        return userApi.bathList(dto);
    }

    @Override
    public Observable<ApiResult<ResidenceListRespDTO>> queryBathResidenceList(QueryResidenceListReqDTO body) {
        return residenceApi.queryBathResidenceList(body);
    }

    @Override
    public Observable<ApiResult<UserResidenceInListDTO>> recordBath(RecordResidenceReqDTO reqDTO) {
        return userApi.recordBath(reqDTO);
    }

    @Override
    public Observable<ApiResult<DeleteResidenceRespDTO>> deleteBathRecord(SimpleReqDTO dto) {
        return userApi.deleteBathRecord(dto);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> updateNormalBathroom(SimpleReqDTO dto) {
        return userApi.updateNormalBathroom(dto);
    }

    @Override
    public void setBathroomPassword() {
        sharedPreferencesHelp.getUserInfo().setHadSetBathPassword(true);
    }

    @Override
    public void setRoomId(Long residenceId) {
        sharedPreferencesHelp.setRoomId(residenceId);
    }

    @Override
    public Long getRoomId() {
        return sharedPreferencesHelp.getRoomId();
    }


    public void setBathroomPassword(String password){
        sharedPreferencesHelp.setBathroomPassword(password);
    }


    public String getBathroomPassword(){
       return sharedPreferencesHelp.getBathroomPassword();
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
