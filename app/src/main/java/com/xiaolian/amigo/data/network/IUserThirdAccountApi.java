package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.userthirdaccount.AddThirdAccountReqDTO;
import com.xiaolian.amigo.data.network.model.userthirdaccount.QueryUserThirdAccountReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.userthirdaccount.QueryUserThirdAccountRespDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 第三方账号相关api
 * <p>
 * Created by zcd on 17/12/14.
 */

public interface IUserThirdAccountApi {
    // 第三方账号列表
    @POST("user/third/account/list")
    Observable<ApiResult<QueryUserThirdAccountRespDTO>> requestThirdAccounts(@Body QueryUserThirdAccountReqDTO reqDTO);

    // 新增第三方账号
    @POST("user/third/account/add")
    Observable<ApiResult<BooleanRespDTO>> addAccount(@Body AddThirdAccountReqDTO reqDTO);

    // 删除第三方账户
    @POST("user/third/account/delete")
    Observable<ApiResult<BooleanRespDTO>> deleteAccount(@Body SimpleReqDTO reqDTO);
}
