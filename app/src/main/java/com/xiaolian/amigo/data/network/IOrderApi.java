package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.order.LatestOrderReqDTO;
import com.xiaolian.amigo.data.network.model.order.OrderDetailReqDTO;
import com.xiaolian.amigo.data.network.model.order.OrderReqDTO;
import com.xiaolian.amigo.data.network.model.order.QueryPrepayOptionReqDTO;
import com.xiaolian.amigo.data.network.model.order.UnsettledOrderStatusCheckReqDTO;
import com.xiaolian.amigo.data.network.model.order.LatestOrderRespDTO;
import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.data.network.model.order.OrderPreInfoDTO;
import com.xiaolian.amigo.data.network.model.order.OrderRespDTO;
import com.xiaolian.amigo.data.network.model.order.UnsettledOrderStatusCheckRespDTO;

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
    @POST("order/personal/list")
    Observable<ApiResult<OrderRespDTO>> queryOrders(@Body OrderReqDTO reqDTO);

    // 查询订单详情
    @POST("order/one")
    Observable<ApiResult<OrderDetailRespDTO>> queryOrderDetail(@Body OrderDetailReqDTO reqDTO);

    // 校验订单状态
    @POST("order/status/check")
    Observable<ApiResult<UnsettledOrderStatusCheckRespDTO>> checkOrderStatus(@Body UnsettledOrderStatusCheckReqDTO reqDTO);

    // 校验订单状态
    @POST("order/latest")
    Observable<ApiResult<LatestOrderRespDTO>> queryLatestOrder(@Body LatestOrderReqDTO reqDTO);

    // 订单预备信息：options是预付金额选项，bonus是可用代金券数量
    @POST("order/pre")
    Observable<ApiResult<OrderPreInfoDTO>> queryPrepayOption(@Body QueryPrepayOptionReqDTO reqDTO);

    // 查看预付订单
    @POST("order/personal/list/prepay")
    Observable<ApiResult<OrderRespDTO>> queryPrepay(@Body OrderReqDTO reqDTO);
}
