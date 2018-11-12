package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathPasswordUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.RecordResidenceReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.common.EmptyRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCategoryBO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckReqDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckRespDTO;
import com.xiaolian.amigo.data.network.model.file.OssModel;
import com.xiaolian.amigo.data.network.model.login.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeCheckReqDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeGetReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.NoticeCountDTO;
import com.xiaolian.amigo.data.network.model.residence.QueryResidenceListReqDTO;
import com.xiaolian.amigo.data.network.model.residence.ResidenceListRespDTO;
import com.xiaolian.amigo.data.network.model.school.QueryBriefSchoolListRespDTO;
import com.xiaolian.amigo.data.network.model.school.QuerySchoolBizListRespDTO;
import com.xiaolian.amigo.data.network.model.school.QuerySchoolListReqDTO;
import com.xiaolian.amigo.data.network.model.user.BindResidenceReq;
import com.xiaolian.amigo.data.network.model.user.DeleteResidenceRespDTO;
import com.xiaolian.amigo.data.network.model.user.MobileUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.user.PasswordCheckReqDTO;
import com.xiaolian.amigo.data.network.model.user.PasswordUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.user.PersonalExtraInfoDTO;
import com.xiaolian.amigo.data.network.model.user.PersonalUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.user.QueryAvatarDTO;
import com.xiaolian.amigo.data.network.model.user.QueryUserResidenceListRespDTO;
import com.xiaolian.amigo.data.network.model.user.SchoolNameListRespDTO;
import com.xiaolian.amigo.data.network.model.user.UserAuthCertifyReqDTO;
import com.xiaolian.amigo.data.network.model.user.UserCertifyInfoRespDTO;
import com.xiaolian.amigo.data.network.model.user.UserGradeInfoRespDTO;
import com.xiaolian.amigo.data.network.model.user.UserResidenceDTO;
import com.xiaolian.amigo.data.network.model.user.UserResidenceInListDTO;
import com.xiaolian.amigo.data.vo.User;

import java.util.List;

import dagger.BindsOptionalOf;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * 个人信息模块DataManager
 *
 * @author zcd
 * @date 17/9/15
 */

public interface IUserDataManager {
    Observable<ApiResult<String>> uploadFile(@Part("filename=\"image.jpg\"") RequestBody images);

    User getUser();

    void setUser(User user);

    void logout();

    /**
     * 用户默认头像列表
     */
    Observable<ApiResult<QueryAvatarDTO>> getAvatarList();

    /**
     * 获取用户个人信息
     */
    Observable<ApiResult<EntireUserDTO>> getUserInfo();

    /**
     * 用户个人中心额外信息
     */
    Observable<ApiResult<PersonalExtraInfoDTO>> getUserExtraInfo();

    /**
     * 更新用户个人信息
     */
    Observable<ApiResult<EntireUserDTO>> updateUserInfo(@Body PersonalUpdateReqDTO body);

    /**
     * 更新用户手机号
     */
    Observable<ApiResult<EntireUserDTO>> updateMobile(@Body MobileUpdateReqDTO body);

    /**
     * 更新用户密码
     */
    Observable<ApiResult<SimpleRespDTO>> updatePassword(@Body PasswordUpdateReqDTO body);

    /**
     * 获取学校列表
     */
    Observable<ApiResult<QueryBriefSchoolListRespDTO>> getSchoolList(@Body QuerySchoolListReqDTO body);

    /**
     * 获取学校业务列表
     */
    Observable<ApiResult<QuerySchoolBizListRespDTO>> getSchoolBizList();

    /**
     * 获取验证码
     */
    Observable<ApiResult<BooleanRespDTO>> getVerifyCode(@Body VerificationCodeGetReqDTO body);

    /**
     * 用户绑定的寝室列表
     */
    @Deprecated
    Observable<ApiResult<QueryUserResidenceListRespDTO>> queryUserResidenceList();

    /**
     * 用户删除绑定寝室
     */
    Observable<ApiResult<DeleteResidenceRespDTO>> deleteResidence(@Body SimpleReqDTO body);

    /**
     * 获取建筑列表
     * residenceLevel 1幢 2楼层 3宿舍 具体位置 buildingType 1宿舍楼 parentId上一层事物Id
     */
    Observable<ApiResult<ResidenceListRespDTO>> queryResidenceList(@Body QueryResidenceListReqDTO body);

    /**
     * 用户绑定编辑寝室
     */
    @Deprecated
    Observable<ApiResult<UserResidenceInListDTO>> bindResidence(@Body BindResidenceReq body);

    /**
     * 用户密码校验
     */
    Observable<ApiResult<BooleanRespDTO>> checkPasswordValid(@Body PasswordCheckReqDTO reqDTO);

    /**
     * 用户密码校验
     */
    Observable<ApiResult<BooleanRespDTO>> changeSchoolCheck();

    /**
     * 用户绑定宿舍详情
     */
    Observable<ApiResult<UserResidenceDTO>> queryResidenceDetail(@Body SimpleReqDTO reqDTO);

    /**
     * oss
     */
    Observable<ApiResult<OssModel>> getOssModel();

    /**
     * 是否存在公共浴室业务
     */
    boolean isExistBathroomBiz();

    /**
     * 获取验证码
     */
    Observable<ApiResult<BooleanRespDTO>> getVerification(@Body VerificationCodeGetReqDTO body);

    /**
     * 校验验证码
     */
    Observable<ApiResult<BooleanRespDTO>> checkVerifyCode(@Body VerificationCodeCheckReqDTO reqDTO);

    /**
     * 更新浴室密码
     *
     * @param reqDTO
     * @return
     */
    Observable<ApiResult<SimpleRespDTO>> updateBathroomPassword(@Body BathPasswordUpdateReqDTO reqDTO);

    /**
     * 获取浴室密码说明
     *
     * @return
     */
    List<String> getBathroomPasswordDesc();

    /**
     * 获取公共浴室地址列表
     *
     * @return
     */
    Observable<ApiResult<QueryUserResidenceListRespDTO>> bathList(@Body EmptyRespDTO dto);


    /**
     * 获取洗澡地址
     *
     * @param body
     * @return
     */
    Observable<ApiResult<ResidenceListRespDTO>> queryBathResidenceList(@Body QueryResidenceListReqDTO body);

    /**
     * 记录洗澡地址
     *
     * @param reqDTO
     * @return
     */
    Observable<ApiResult<UserResidenceInListDTO>> recordBath(@Body RecordResidenceReqDTO reqDTO);

    /**
     * 删除洗澡地址记录
     *
     * @param dto
     * @return
     */
    Observable<ApiResult<DeleteResidenceRespDTO>> deleteBathRecord(@Body SimpleReqDTO dto);


    /**
     * 更新默认洗澡地址
     *
     * @param dto
     * @return
     */
    Observable<ApiResult<BooleanRespDTO>> updateNormalBathroom(@Body SimpleReqDTO dto);


    /**
     * 通知数量
     */
    Observable<ApiResult<NoticeCountDTO>> noticeCount();


    void saveDeviceCategory(List<DeviceCategoryBO> devices);


    /**
     * 首页设备用水校验
     */
    Observable<ApiResult<DeviceCheckRespDTO>> checkDeviceUseage(@Body DeviceCheckReqDTO reqDTO);
    void setBathroomPassword();

    void setRoomId(Long residenceId);

    Long getRoomId();

    public void setBathroomPassword(String password);

    public String getBathroomPassword();


    /**
     * 获取学校列表
     * @return
     */
    Observable<ApiResult<SchoolNameListRespDTO>> getSchoolNameList ();



//    Observable<ApiResult<BooleanRespDTO>> certify(@Body UserAuthCertifyReqDTO dto);

    Observable<ApiResult<BooleanRespDTO>> certify(@Body RequestBody body);


    Observable<ApiResult<UserGradeInfoRespDTO>> gradeInfo();

    /**
     * 查询认证信息
     * @return
     */
    Observable<ApiResult<UserCertifyInfoRespDTO>> certifyInfo();


    void setCertifyStatus(int certifyStatus);

    long getLastDeleteTime();

    void setDeleteFileTime(long l);
}
