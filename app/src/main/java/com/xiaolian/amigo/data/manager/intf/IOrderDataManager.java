package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.complaint.CheckComplaintReqDTO;
import com.xiaolian.amigo.data.network.model.order.LatestOrderReqDTO;
import com.xiaolian.amigo.data.network.model.order.LatestOrderRespDTO;
import com.xiaolian.amigo.data.network.model.order.OrderDetailReqDTO;
import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.data.network.model.order.OrderPreInfoDTO;
import com.xiaolian.amigo.data.network.model.order.OrderReqDTO;
import com.xiaolian.amigo.data.network.model.order.OrderRespDTO;
import com.xiaolian.amigo.data.network.model.order.QueryPrepayOptionReqDTO;
import com.xiaolian.amigo.data.network.model.order.UnsettledOrderStatusCheckReqDTO;
import com.xiaolian.amigo.data.network.model.order.UnsettledOrderStatusCheckRespDTO;

import retrofit2.http.Body;
import rx.Observable;

/**
 * 订单
 *
 * @author caidong
 * @date 17/9/15
 */
public interface IOrderDataManager {
    // 查询个人订单列表
    Observable<ApiResult<OrderRespDTO>> queryOrders(@Body OrderReqDTO reqDTO);

    // 查询订单详情
    Observable<ApiResult<OrderDetailRespDTO>> queryOrderDetail(@Body OrderDetailReqDTO reqDTO);

    // 校验订单状态
    Observable<ApiResult<UnsettledOrderStatusCheckRespDTO>> checkOrderStatus(@Body UnsettledOrderStatusCheckReqDTO reqDTO);

    // 校验订单状态
    Observable<ApiResult<LatestOrderRespDTO>> queryLatestOrder(@Body LatestOrderReqDTO reqDTO);

    // 订单预备信息：options是预付金额选项，bonus是可用代金券数量
    Observable<ApiResult<OrderPreInfoDTO>> queryPrepayOption(@Body QueryPrepayOptionReqDTO reqDTO);

    // 查看预付订单
    Observable<ApiResult<OrderRespDTO>> queryPrepay(@Body OrderReqDTO reqDTO);

    // 投诉查重
    Observable<ApiResult<BooleanRespDTO>> checkComplaint(@Body CheckComplaintReqDTO reqDTO);

    String getToken();
}
