/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.xiaolian.amigo.ui.order;


import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.order.OrderInListDTO;
import com.xiaolian.amigo.data.network.model.order.OrderReqDTO;
import com.xiaolian.amigo.data.network.model.order.OrderRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.order.adaptor.OrderAdaptor;
import com.xiaolian.amigo.ui.order.intf.IOrderPresenter;
import com.xiaolian.amigo.ui.order.intf.IOrderView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 订单列表
 *
 * @author caidong
 * @date 17/9/15
 */
public class OrderPresenter<V extends IOrderView> extends BasePresenter<V>
        implements IOrderPresenter<V> {
    @SuppressWarnings("unused")
    private static final String TAG = OrderPresenter.class.getSimpleName();
    private IOrderDataManager orderDataManager;

    @Inject
    OrderPresenter(IOrderDataManager orderDataManager) {
        super();
        this.orderDataManager = orderDataManager;
    }


    @Override
    public void requestOrders(int page, Integer deviceType,
                              Integer year, Integer month) {
        OrderReqDTO reqDTO = new OrderReqDTO();
        if (deviceType != null
                && deviceType != OrderActivity.INVALID_INT) {
            reqDTO.setDeviceType(deviceType);
        }
        if (year != null
                && year != OrderActivity.INVALID_INT) {
            reqDTO.setYear(year);
        }
        if (month != null
                && month != OrderActivity.INVALID_INT) {
            reqDTO.setMonth(month);
        }
        reqDTO.setPage(page);
        reqDTO.setSize(Constant.PAGE_SIZE);
        // 查看已结束账单
        reqDTO.setOrderStatus(2);
        addObserver(orderDataManager.queryOrders(reqDTO), new NetworkObserver<ApiResult<OrderRespDTO>>(false, true) {
            @Override
            public void onReady(ApiResult<OrderRespDTO> result) {
                getMvpView().setRefreshComplete();
                getMvpView().setLoadMoreComplete();
                getMvpView().hideEmptyView();
                getMvpView().hideErrorView();
                if (null == result.getError()) {
                    if (null != result.getData().getOrders() && result.getData().getOrders().size() > 0) {
                        List<OrderAdaptor.OrderWrapper> wrappers = new ArrayList<>();
                        for (OrderInListDTO order : result.getData().getOrders()) {
                            wrappers.add(new OrderAdaptor.OrderWrapper(order.transform()));
                        }
                        getMvpView().addMore(wrappers);
                        getMvpView().addPage();
                    } else {
                        getMvpView().showEmptyView(R.string.empty_tip_1);
                    }
                } else {
                    getMvpView().showErrorView();
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().setRefreshComplete();
                getMvpView().setLoadMoreComplete();
                getMvpView().showErrorView();
            }
        });
    }

    @Override
    public String getToken() {
        return orderDataManager.getToken();
    }

}
