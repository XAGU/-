package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.data.network.IOrderApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.CheckComplaintReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.LatestOrderReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.OrderDetailReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.OrderReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.QueryPrepayOptionReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.UnsettledOrderStatusCheckReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.LatestOrderRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.OrderDetailRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.OrderPreInfoDTO;
import com.xiaolian.amigo.data.network.model.dto.response.OrderRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.UnsettledOrderStatusCheckRespDTO;

import javax.inject.Inject;

import retrofit2.http.Body;
import rx.Observable;

import retrofit2.Retrofit;

/**
 * 订单管理
 * <p>
 * Created by caidong on 2017/9/15.
 */
public class OrderDataManager implements IOrderDataManager {

    private static final String TAG = LoginDataManager.class.getSimpleName();

    private IOrderApi orderApi;

    @Inject
    public OrderDataManager(Retrofit retrofit) {
        orderApi = retrofit.create(IOrderApi.class);
    }

    // 查询个人订单
    @Override
    public Observable<ApiResult<OrderRespDTO>> queryOrders(OrderReqDTO reqDTO) {
        return orderApi.queryOrders(reqDTO);
    }

    @Override
    public Observable<ApiResult<OrderDetailRespDTO>> queryOrderDetail(@Body OrderDetailReqDTO reqDTO) {
        return orderApi.queryOrderDetail(reqDTO);
    }

    @Override
    public Observable<ApiResult<UnsettledOrderStatusCheckRespDTO>> checkOrderStatus(@Body UnsettledOrderStatusCheckReqDTO reqDTO) {
        return orderApi.checkOrderStatus(reqDTO);
    }

    @Override
    public Observable<ApiResult<OrderPreInfoDTO>> queryPrepayOption(@Body QueryPrepayOptionReqDTO reqDTO) {
        return orderApi.queryPrepayOption(reqDTO);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> checkComplaint(CheckComplaintReqDTO reqDTO) {
        return orderApi.checkComplaint(reqDTO);
    }

    @Override
    public Observable<ApiResult<OrderRespDTO>> queryPrepay(OrderReqDTO reqDTO) {
        return orderApi.queryPrepay(reqDTO);
    }

    @Override
    public Observable<ApiResult<LatestOrderRespDTO>> queryLatestOrder(@Body LatestOrderReqDTO reqDTO) {
        return orderApi.queryLatestOrder(reqDTO);
    }
}
