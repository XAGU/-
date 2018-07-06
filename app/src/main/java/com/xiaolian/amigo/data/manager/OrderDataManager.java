package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.data.network.IComplaintApi;
import com.xiaolian.amigo.data.network.IOrderApi;
import com.xiaolian.amigo.data.network.IUserBillApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.complaint.CheckComplaintReqDTO;
import com.xiaolian.amigo.data.network.model.order.LatestOrderReqDTO;
import com.xiaolian.amigo.data.network.model.order.OrderDetailReqDTO;
import com.xiaolian.amigo.data.network.model.order.OrderReqDTO;
import com.xiaolian.amigo.data.network.model.order.QueryPrepayOptionReqDTO;
import com.xiaolian.amigo.data.network.model.order.UnsettledOrderStatusCheckReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.order.LatestOrderRespDTO;
import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.data.network.model.order.OrderPreInfoDTO;
import com.xiaolian.amigo.data.network.model.order.OrderRespDTO;
import com.xiaolian.amigo.data.network.model.order.UnsettledOrderStatusCheckRespDTO;
import com.xiaolian.amigo.data.network.model.userbill.QueryPersonalMaxConsumeOrderListReqDTO;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.di.UserServer;

import javax.inject.Inject;

import retrofit2.http.Body;
import rx.Observable;

import retrofit2.Retrofit;

/**
 * 订单管理
 *
 * @author caidong
 * @date 17/9/15
 */
public class OrderDataManager implements IOrderDataManager {

    private static final String TAG = LoginDataManager.class.getSimpleName();

    private IComplaintApi complaintApi;
    private IOrderApi orderApi;
    private IUserBillApi userBillApi;
    private ISharedPreferencesHelp sharedPreferencesHelp;

    @Inject
    public OrderDataManager(@UserServer Retrofit retrofit, ISharedPreferencesHelp sharedPreferencesHelp) {
        orderApi = retrofit.create(IOrderApi.class);
        complaintApi = retrofit.create(IComplaintApi.class);
        userBillApi = retrofit.create(IUserBillApi.class);
        this.sharedPreferencesHelp = sharedPreferencesHelp;
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
        return complaintApi.checkComplaint(reqDTO);
    }

    @Override
    public String getToken() {
        return sharedPreferencesHelp.getToken();
    }

    @Override
    public Observable<ApiResult<OrderRespDTO>> getMonthlyMaxBill(QueryPersonalMaxConsumeOrderListReqDTO reqDTO) {
        return userBillApi.getMonthlyMaxBill(reqDTO);
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
