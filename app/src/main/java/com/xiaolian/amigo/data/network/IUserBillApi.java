package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.userbill.QueryMonthlyBillReqDTO;
import com.xiaolian.amigo.data.network.model.userbill.UserMonthlyBillRespDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author zcd
 * @date 18/6/4
 */
public interface IUserBillApi {
    /**
     * 用户个人账单查询
     */
    @POST("user/bill/month/one")
    Observable<ApiResult<UserMonthlyBillRespDTO>> uploadDeviceInfo(@Body QueryMonthlyBillReqDTO reqDTO);
}
