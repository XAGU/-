package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.response.PersonalExtraInfoDTO;

import retrofit2.http.POST;
import rx.Observable;

/**
 * 主页
 * <p>
 * Created by zcd on 9/22/17.
 */

public interface IMainApi {
    // 用户个人中心额外信息：包括我的钱包余额、我的红包数量、是否用设备报修中、目前在进行中的订单
    @POST("/user/extraInfo/one")
    Observable<ApiResult<PersonalExtraInfoDTO>> getExtraInfo();
}
