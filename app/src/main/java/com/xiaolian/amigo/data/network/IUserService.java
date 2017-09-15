package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.MobileUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.PasswordUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.PersonalUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.dto.response.SimpleRespDTO;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 个人信息模块Api接口
 * @author zcd
 */
public interface IUserService {
    // 获取用户个人信息
    @POST("/user/one")
    Flowable<ApiResult<EntireUserDTO>> getUserInfo();

    // 更新用户个人信息
    @POST("/user/update")
    Flowable<ApiResult<EntireUserDTO>> updateUserInfo(@Body PersonalUpdateReqDTO body);

    // 更新用户手机号
    @POST("/user/mobile/update")
    Flowable<ApiResult<EntireUserDTO>> updateMobile(@Body MobileUpdateReqDTO body);

    // 更新用户密码
    @POST("/user/password/update")
    Flowable<ApiResult<SimpleRespDTO>> updatePassword(@Body PasswordUpdateReqDTO body);

}
