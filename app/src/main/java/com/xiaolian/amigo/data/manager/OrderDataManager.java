package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.data.network.ILoginService;
import com.xiaolian.amigo.data.network.IOrderApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.LoginRespDTO;
import com.xiaolian.amigo.data.network.model.NetworkObserver;
import com.xiaolian.amigo.data.network.model.RegisterReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.OrderReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.OrderRespDTO;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Retrofit;
import retrofit2.http.Body;

/**
 * 订单管理
 * <p>
 * Created by caidong on 2017/9/15.
 */
public class OrderDataManager extends BaseManager implements IOrderDataManager {

    private static final String TAG = LoginDataManager.class.getSimpleName();

    private IOrderApi orderApi;

    @Inject
    public OrderDataManager(Retrofit retrofit) {
        orderApi = retrofit.create(IOrderApi.class);
    }

    // 查询个人账单
    @Override
    public void queryOrders(DisposableObserver<ApiResult<OrderRespDTO>> observer) {
        addObservable(orderApi.queryOrders(new OrderReqDTO()), observer);
    }

}
