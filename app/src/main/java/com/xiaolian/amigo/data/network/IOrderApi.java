package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.OrderDetailReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.OrderReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.UnsettledOrderStatusCheckReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.OrderDetailRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.OrderRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.UnsettledOrderStatusCheckRespDTO;

import rx.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 订单相关接口
 * <p>
 * Created by caidong on 2017/9/15.
 */
public interface IOrderApi {

    // 查询个人订单列表
    @POST("/order/personal/list")
    Observable<ApiResult<OrderRespDTO>> queryOrders(@Body OrderReqDTO reqDTO);

    // 查询订单详情
    @POST("/order/one")
    Observable<ApiResult<OrderDetailRespDTO>> queryOrderDetail(@Body OrderDetailReqDTO reqDTO);

    // 校验订单状态
    @POST("/order/status/check")
    Observable<ApiResult<UnsettledOrderStatusCheckRespDTO>> checkOrderStatus(@Body UnsettledOrderStatusCheckReqDTO reqDTO);
}
