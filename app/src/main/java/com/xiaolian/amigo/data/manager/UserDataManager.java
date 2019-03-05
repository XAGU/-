package com.xiaolian.amigo.data.manager;

import android.support.v4.util.ObjectsCompat;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.IBathroomApi;
import com.xiaolian.amigo.data.network.IDeviceApi;
import com.xiaolian.amigo.data.network.IFileApi;
import com.xiaolian.amigo.data.network.ILoginApi;
import com.xiaolian.amigo.data.network.ILostAndFoundApi;
import com.xiaolian.amigo.data.network.INotifyApi;
import com.xiaolian.amigo.data.network.IOssApi;
import com.xiaolian.amigo.data.network.IResidenceApi;
import com.xiaolian.amigo.data.network.ISchoolApi;
import com.xiaolian.amigo.data.network.IUserApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.alipay.AliPayBindInAppReq;
import com.xiaolian.amigo.data.network.model.alipay.AliPayBindQueryReq;
import com.xiaolian.amigo.data.network.model.alipay.AlipayAuthInfoRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathPasswordUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.RecordResidenceReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.common.ChangeSchoolResDTO;
import com.xiaolian.amigo.data.network.model.common.CheckSchoolRespDTO;
import com.xiaolian.amigo.data.network.model.common.CommitSchoolReqDTO;
import com.xiaolian.amigo.data.network.model.common.EmptyRespDTO;
import com.xiaolian.amigo.data.network.model.common.ApplySchoolCheckRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCategoryBO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckReqDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckRespDTO;
import com.xiaolian.amigo.data.network.model.file.OssModel;
import com.xiaolian.amigo.data.network.model.login.CancelBindReqDTO;
import com.xiaolian.amigo.data.network.model.login.CancelThirdBindReqDTO;
import com.xiaolian.amigo.data.network.model.login.ClearTokenReqDTO;
import com.xiaolian.amigo.data.network.model.login.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeCheckReqDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeGetReqDTO;
import com.xiaolian.amigo.data.network.model.login.WeChatBindRespDTO;
import com.xiaolian.amigo.data.network.model.login.WechatLoginReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.NoticeCountDTO;
import com.xiaolian.amigo.data.network.model.notify.RollingNotifyRespDTO;
import com.xiaolian.amigo.data.network.model.residence.QueryResidenceListReqDTO;
import com.xiaolian.amigo.data.network.model.residence.ResidenceListRespDTO;
import com.xiaolian.amigo.data.network.model.school.QueryBriefSchoolListRespDTO;
import com.xiaolian.amigo.data.network.model.school.QuerySchoolBizListRespDTO;
import com.xiaolian.amigo.data.network.model.school.QuerySchoolListReqDTO;
import com.xiaolian.amigo.data.network.model.school.SchoolForumStatusDTO;
import com.xiaolian.amigo.data.network.model.user.BindResidenceReq;
import com.xiaolian.amigo.data.network.model.user.BriefSchoolBusiness;
import com.xiaolian.amigo.data.network.model.user.DeleteResidenceRespDTO;
import com.xiaolian.amigo.data.network.model.user.MobileUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.user.PasswordCheckReqDTO;
import com.xiaolian.amigo.data.network.model.user.PasswordUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.user.PasswordVerifyRespDTO;
import com.xiaolian.amigo.data.network.model.user.PersonalExtraInfoDTO;
import com.xiaolian.amigo.data.network.model.user.PersonalUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.user.QueryAvatarDTO;
import com.xiaolian.amigo.data.network.model.user.QueryUserResidenceListRespDTO;
import com.xiaolian.amigo.data.network.model.user.ResidenceUpdateRespDTO;
import com.xiaolian.amigo.data.network.model.user.SchoolNameListRespDTO;
import com.xiaolian.amigo.data.network.model.user.UserCertifyInfoRespDTO;
import com.xiaolian.amigo.data.network.model.user.UserGradeInfoRespDTO;
import com.xiaolian.amigo.data.network.model.user.UserResidenceDTO;
import com.xiaolian.amigo.data.network.model.user.UserResidenceInListDTO;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.data.vo.DeviceCategory;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.di.BathroomServer;
import com.xiaolian.amigo.di.UserServer;

import java.util.ArrayList;
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
    private IDeviceApi deviceApi ;
    private ILostAndFoundApi lostAndFoundApi ;
    private ISharedPreferencesHelp sharedPreferencesHelp;
    private INotifyApi notifyApi ;

    @Inject
    public UserDataManager(@BathroomServer Retrofit bathroomRetrofit, @UserServer Retrofit retrofit, ISharedPreferencesHelp sharedPreferencesHelp) {
        residenceApi = retrofit.create(IResidenceApi.class);
        schoolApi = retrofit.create(ISchoolApi.class);
        userApi = retrofit.create(IUserApi.class);
        fileApi = retrofit.create(IFileApi.class);
        loginApi = retrofit.create(ILoginApi.class);
        ossApi = retrofit.create(IOssApi.class);
        deviceApi = retrofit.create(IDeviceApi.class);
        lostAndFoundApi = retrofit.create(ILostAndFoundApi.class);
        notifyApi = retrofit.create(INotifyApi.class);
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
    public Observable<ApiResult<EntireUserDTO>> updateUserInfo(@Body PersonalUpdateReqDTO body) {
        return userApi.updateUserInfo(body);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> clearToken(@Body ClearTokenReqDTO body) {
        return userApi.clearToken(body);
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
    public Observable<ApiResult<BooleanRespDTO>> getVerifyCode(VerificationCodeGetReqDTO body) {
        return loginApi.getVerification(body);
    }
    @Deprecated
    @Override
    public Observable<ApiResult<QueryUserResidenceListRespDTO>> queryUserResidenceList() {
        return userApi.queryUserResidenceList(new SimpleQueryReqDTO());
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
    public Observable<ApiResult<CheckSchoolRespDTO>> preApplySchoolCheck() {
        return userApi.PreApplySchoolCheck();
    }

    @Override
    public Observable<ApiResult<ApplySchoolCheckRespDTO>> applySchoolCheck() {
        return userApi.applySchoolCheck();
    }

    @Override
    public Observable<ApiResult<ChangeSchoolResDTO>> applyChangeSchool(CommitSchoolReqDTO reqDTO) {
        return userApi.applyChangeSchool(reqDTO);
    }

    @Override
    public Observable<ApiResult<User.AlipayBindBean>> bindAlipayInApp(AliPayBindInAppReq reqDTO) {
        return userApi.bindAlipayInApp(reqDTO);
    }

    @Override
    public Observable<ApiResult<WeChatBindRespDTO>> bindWechatInApp(WechatLoginReqDTO reqDTO) {
        return userApi.bindWechatApp(reqDTO);
    }

    @Override
    public Observable<ApiResult<AlipayAuthInfoRespDTO>> getApayAuth() {
        return loginApi.getApayAuth();
    }

    @Override
    public Observable<ApiResult<CheckSchoolRespDTO>> changeSchoolCheck() {
        return userApi.changeSchoolCheck();
    }

    @Override
    public Observable<ApiResult<CheckSchoolRespDTO>> cancelApplyChangeSchool(SimpleReqDTO reqDTO) {
        return userApi.cancelApplyChangeSchool(reqDTO);
    }


    @Override
    public Observable<ApiResult<OssModel>> getOssModel() {
        return ossApi.getOssModel();
    }

    @Override
    public boolean isExistBathroomBiz() {
        if (sharedPreferencesHelp.getSchoolBiz() !=null && sharedPreferencesHelp.getSchoolBiz().size() > 0) {
            for (BriefSchoolBusiness biz : sharedPreferencesHelp.getSchoolBiz()) {
                if (ObjectsCompat.equals(biz.getBusinessId(), 1L) && biz.getPublicBath() != null && biz.getPublicBath()) {
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
    public Observable<ApiResult<PasswordVerifyRespDTO>> verifyPassword(PasswordCheckReqDTO body) {
        return loginApi.verifyPassword(body);
    }

    @Override
    public Observable<ApiResult<EntireUserDTO>> checkChangePhoneVerification(VerificationCodeCheckReqDTO body) {
        return loginApi.checkChangePhoneVerification(body);
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
    public Observable<ApiResult<ResidenceUpdateRespDTO>> updateNormalBathroom(SimpleReqDTO dto) {
        return userApi.updateNormalBathroom(dto);
    }

    @Override
    public Observable<ApiResult<NoticeCountDTO>> noticeCount() {
        return lostAndFoundApi.noticeCount();
    }

    @Override
    public Observable<ApiResult<DeviceCheckRespDTO>> checkDeviceUseage(DeviceCheckReqDTO reqDTO) {
        return deviceApi.checkDeviceUseage(reqDTO);
    }

    @Override
    public void setBathroomPassword(boolean isHadSetBathPassword) {
        sharedPreferencesHelp.setHadSetBathPassword(isHadSetBathPassword);
    }


    @Override
    public Observable<ApiResult<SchoolNameListRespDTO>> getSchoolNameList() {
        return schoolApi.getSchoolNameList();
    }


    @Override
    public Observable<ApiResult<BooleanRespDTO>> certify(RequestBody dto) {
        return userApi.certify(dto);
    }

    @Override
    public Observable<ApiResult<UserGradeInfoRespDTO>> gradeInfo() {
        return userApi.gradeInfo();
    }

    @Override
    public Observable<ApiResult<UserCertifyInfoRespDTO>> certifyInfo() {
        return userApi.certifyInfo();
    }

    @Override
    public void setCertifyStatus(int certifyStatus) {
        sharedPreferencesHelp.setCertifyStauts(certifyStatus);
    }

    @Override
    public long getLastDeleteTime() {
        return sharedPreferencesHelp.getDeleteFileTime();
    }

    @Override
    public void setDeleteFileTime(long l) {
        sharedPreferencesHelp.saveDeleteFileTime(l);
    }

    @Override
    public int getCertifyStatus() {
        return sharedPreferencesHelp.getCertifyStatus();
    }

    @Override
    public Observable<ApiResult<RollingNotifyRespDTO>> rollingNotify() {
        return notifyApi.rollingList();
    }

    @Override
    public Observable<ApiResult<SchoolForumStatusDTO>> getSchoolForumStatus() {
        return schoolApi.getSchoolSwitch();
    }

    @Override
    public String getRemindMobile() {
        return sharedPreferencesHelp.getRememberMobile();
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

    @Override
    public Observable<ApiResult<CancelThirdBindReqDTO>> aplipayUnbind() {
        return loginApi.aplipayUnbind();
    }

    @Override
    public  Observable<ApiResult<CancelThirdBindReqDTO>> weChatUnbind(){
        return loginApi.weChatUnbind();
    }
}
