package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.order.OrderRespDTO;
import com.xiaolian.amigo.data.network.model.userbill.QueryBillListReqDTO;
import com.xiaolian.amigo.data.network.model.userbill.QueryBillListRespDTO;
import com.xiaolian.amigo.data.network.model.userbill.QueryMonthlyBillReqDTO;
import com.xiaolian.amigo.data.network.model.userbill.QueryPersonalMaxConsumeOrderListReqDTO;
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
     * 用户个人月账单查询
     */
    @POST("user/bill/month/one")
    Observable<ApiResult<UserMonthlyBillRespDTO>> getMonthlyBill(@Body QueryMonthlyBillReqDTO reqDTO);

    /**
     * 用户个人账单列表查询（充值、提现、消费）
     */
    @POST("user/bill/details")
    Observable<ApiResult<QueryBillListRespDTO>> getUserBillList(@Body QueryBillListReqDTO reqDTO);

        /**
         * 用户个人订单(最大消费)记录列表
         */
    @POST("user/bill/month/max")
    Observable<ApiResult<OrderRespDTO>> getMonthlyMaxBill(@Body QueryPersonalMaxConsumeOrderListReqDTO reqDTO);
}
