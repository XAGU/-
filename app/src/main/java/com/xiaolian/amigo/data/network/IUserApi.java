package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.user.BindResidenceReq;
import com.xiaolian.amigo.data.network.model.user.MobileUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.user.PasswordCheckReqDTO;
import com.xiaolian.amigo.data.network.model.user.PasswordUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.user.PersonalUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.user.DeleteResidenceRespDTO;
import com.xiaolian.amigo.data.network.model.login.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.user.PersonalExtraInfoDTO;
import com.xiaolian.amigo.data.network.model.user.QueryAvatarDTO;
import com.xiaolian.amigo.data.network.model.user.QueryUserResidenceListRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.user.UserResidenceDTO;
import com.xiaolian.amigo.data.network.model.user.UserResidenceInListDTO;
import com.xiaolian.amigo.data.network.model.user.UploadUserDeviceInfoReqDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 个人信息模块Api接口
 * @author zcd
 */
public interface IUserApi {
    // 用户默认头像列表
    @POST("user/avatar/list")
    Observable<ApiResult<QueryAvatarDTO>> getAvatarList();
    // 获取用户个人信息
    @POST("user/one")
    Observable<ApiResult<EntireUserDTO>> getUserInfo();

    // 用户个人中心额外信息
    @POST("user/extraInfo/one")
    Observable<ApiResult<PersonalExtraInfoDTO>> getUserExtraInfo();

    // 更新用户个人信息
    @POST("user/update")
    Observable<ApiResult<EntireUserDTO>> updateUserInfo(@Body PersonalUpdateReqDTO body);

    // 更新用户手机号
    @POST("user/mobile/update")
    Observable<ApiResult<EntireUserDTO>> updateMobile(@Body MobileUpdateReqDTO body);

    // 更新用户密码
    @POST("user/password/update")
    Observable<ApiResult<SimpleRespDTO>> updatePassword(@Body PasswordUpdateReqDTO body);

    // 用户绑定的寝室列表
    @POST("user/residence/list")
    Observable<ApiResult<QueryUserResidenceListRespDTO>> queryUserResidenceList(@Body SimpleQueryReqDTO body);

    // 用户删除绑定寝室
    @POST("user/residence/delete")
    Observable<ApiResult<DeleteResidenceRespDTO>> deleteResidence(@Body SimpleReqDTO body);

    // 用户绑定编辑寝室
    @POST("user/residence/bind")
    Observable<ApiResult<UserResidenceInListDTO>> bindResidence(@Body BindResidenceReq body);

    // 用户密码校验
    @POST("user/password/check")
    Observable<ApiResult<BooleanRespDTO>> checkPasswordValid(@Body PasswordCheckReqDTO reqDTO);

    // 用户密码校验
    @POST("user/changeSchool/check")
    Observable<ApiResult<BooleanRespDTO>> changeSchoolCheck();

    // 用户绑定宿舍详情
    @POST("user/residence/one")
    Observable<ApiResult<UserResidenceDTO>> queryResidenceDetail(@Body SimpleReqDTO reqDTO);

    // 提交设备信息
    @POST("user/deviceInfo/upload")
    Observable<ApiResult<BooleanRespDTO>> uploadDeviceInfo(@Body UploadUserDeviceInfoReqDTO reqDTO);
}
