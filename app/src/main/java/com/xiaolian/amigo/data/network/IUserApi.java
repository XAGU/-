package com.xiaolian.amigo.data.network;

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


import java.util.List;

import rx.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 个人信息模块Api接口
 * @author zcd
 */
public interface IUserApi {
    // 用户默认头像列表
    @POST("/user/avatar/list")
    Observable<ApiResult<QueryAvatarDTO>> getAvatarList();
    // 获取用户个人信息
    @POST("/user/one")
    Observable<ApiResult<EntireUserDTO>> getUserInfo();

    // 用户个人中心额外信息
    @POST("/user/extraInfo/one")
    Observable<ApiResult<PersonalExtraInfoDTO>> getUserExtraInfo();

    // 更新用户个人信息
    @POST("/user/update")
    Observable<ApiResult<EntireUserDTO>> updateUserInfo(@Body PersonalUpdateReqDTO body);

    // 更新用户手机号
    @POST("/user/mobile/update")
    Observable<ApiResult<EntireUserDTO>> updateMobile(@Body MobileUpdateReqDTO body);

    // 更新用户密码
    @POST("/user/password/update")
    Observable<ApiResult<SimpleRespDTO>> updatePassword(@Body PasswordUpdateReqDTO body);

    // 获取学校列表
    @POST("/school/brief/list")
    Observable<ApiResult<QueryBriefSchoolListRespDTO>> getSchoolList(@Body SimpleQueryReqDTO body);

    // 获取学校业务列表
    @POST("/school/business/list")
    Observable<ApiResult<QuerySchoolBizListRespDTO>> getSchoolBizList();

    // 获取验证码
    @POST("/login/verification/one")
    Observable<ApiResult<BooleanRespDTO>> getVerifyCode(@Body VerificationCodeGetReqDTO body);

    // 用户绑定的寝室列表
    @POST("/user/residence/list")
    Observable<ApiResult<QueryUserResidenceListRespDTO>> queryUserResidenceList(@Body SimpleQueryReqDTO body);

    // 用户删除绑定寝室
    @POST("/user/residence/delete")
    Observable<ApiResult<DeleteResidenceRespDTO>> deleteResidence(@Body SimpleReqDTO body);

    // 获取建筑列表
    // residenceLevel 1幢 2楼层 3宿舍 具体位置 buildingType 1宿舍楼 parentId上一层事物Id
    @POST("/residence/list")
    Observable<ApiResult<ResidenceListRespDTO>> queryResidenceList(@Body QueryResidenceListReqDTO body);

    // 用户绑定编辑寝室
    @POST("/user/residence/bind")
    Observable<ApiResult<UserResidenceInListDTO>> bindResidence(@Body BindResidenceReq body);

    // 用户密码校验
    @POST("/user/password/check")
    Observable<ApiResult<BooleanRespDTO>> checkPasswordValid(@Body PasswordCheckReqDTO reqDTO);

    // 用户密码校验
    @POST("/user/changeSchool/check")
    Observable<ApiResult<BooleanRespDTO>> changeSchoolCheck();
}
