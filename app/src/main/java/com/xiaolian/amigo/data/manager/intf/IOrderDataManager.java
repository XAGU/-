package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.ILoginService;
import com.xiaolian.amigo.data.network.IOrderApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.response.OrderRespDTO;

import io.reactivex.observers.DisposableObserver;

public interface IOrderDataManager {

    // 查询个人账单
    void queryOrders(DisposableObserver<ApiResult<OrderRespDTO>> observer);
}
