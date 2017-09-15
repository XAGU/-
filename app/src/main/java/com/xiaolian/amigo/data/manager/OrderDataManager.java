package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.data.network.IOrderApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.OrderReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.OrderRespDTO;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
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

}
