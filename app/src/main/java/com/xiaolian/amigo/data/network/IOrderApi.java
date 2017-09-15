package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.OrderReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.OrderRespDTO;

import io.reactivex.Observable;
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
}
