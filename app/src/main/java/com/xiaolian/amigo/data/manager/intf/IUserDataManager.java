package com.xiaolian.amigo.data.manager.intf;

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
import com.xiaolian.amigo.data.vo.User;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Part;
import rx.Observable;

/**
 * 个人信息模块DataManager
 *
 * @author zcd
 * @date 17/9/15
 */

public interface IUserDataManager {

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
     *解除支付宝绑定
     */
    Observable<ApiResult<CancelThirdBindReqDTO>> aplipayUnbind();

    /**
     *解除微信绑定
     */
    Observable<ApiResult<CancelThirdBindReqDTO>> weChatUnbind();

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
     * 检查是否有申请过更换学校
     */
    Observable<ApiResult<ApplySchoolCheckRespDTO>> applySchoolCheck();
    /**
     *在app登录后绑定支付宝
     */
    Observable<ApiResult<User.AlipayBindBean>> bindAlipayInApp(AliPayBindInAppReq reqDTO);
    /**
     * 在app登录后绑定微信
     */
    Observable<ApiResult<WeChatBindRespDTO>> bindWechatInApp(WechatLoginReqDTO reqDTO);
    /**
     *获取支付宝auth info
     */
    Observable<ApiResult<AlipayAuthInfoRespDTO>> getApayAuth();
    /**
     *检查是否满足切换学校条件
     */

    Observable<ApiResult<CheckSchoolRespDTO>> changeSchoolCheck();

    /**
     *取消申请学校
     */

    Observable<ApiResult<CheckSchoolRespDTO>> cancelApplyChangeSchool(SimpleReqDTO reqDTO);

    /**
     * 提交审核前的预处理
     */
    Observable<ApiResult<CheckSchoolRespDTO>> preApplySchoolCheck();

    /**
     * 提交审核
     */
    Observable<ApiResult<ChangeSchoolResDTO>> applyChangeSchool(CommitSchoolReqDTO reqDTO);

    /**
     *清空token
     */
    Observable<ApiResult<BooleanRespDTO>> clearToken(@Body ClearTokenReqDTO body);

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
    Observable<ApiResult<ResidenceUpdateRespDTO>> updateNormalBathroom(@Body SimpleReqDTO dto);


    /**
     * 通知数量
     */
    Observable<ApiResult<NoticeCountDTO>> noticeCount();



    /**
     * 首页设备用水校验
     */
    Observable<ApiResult<DeviceCheckRespDTO>> checkDeviceUseage(@Body DeviceCheckReqDTO reqDTO);
    void setBathroomPassword(boolean isHadSetPassword);



    /**
     * 获取学校列表
     * @return
     */
    Observable<ApiResult<SchoolNameListRespDTO>> getSchoolNameList ();



    Observable<ApiResult<BooleanRespDTO>> certify(@Body RequestBody body);


    Observable<ApiResult<UserGradeInfoRespDTO>> gradeInfo();

    /**
     * 查询认证信息
     * @return
     */
    Observable<ApiResult<UserCertifyInfoRespDTO>> certifyInfo();

    /**
     *修改电话号码
     */
    Observable<ApiResult<EntireUserDTO>> checkChangePhoneVerification(VerificationCodeCheckReqDTO body);

    /**
     * 验证登录密码
     */
    Observable<ApiResult<PasswordVerifyRespDTO>> verifyPassword(PasswordCheckReqDTO body);

    void setCertifyStatus(int certifyStatus);

    long getLastDeleteTime();

    void setDeleteFileTime(long l);

    int getCertifyStatus();

    /**
     * 滚动公告
     * @return
     */
    Observable<ApiResult<RollingNotifyRespDTO>> rollingNotify();

    /**
     * 获取学校论坛开关接口
     * @return
     */
    Observable<ApiResult<SchoolForumStatusDTO>> getSchoolForumStatus();

    String getRemindMobile();

    void saveDeviceCategory(List<DeviceCategoryBO> devices);
}
