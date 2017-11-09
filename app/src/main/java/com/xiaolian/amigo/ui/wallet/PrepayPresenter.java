package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Payment;
import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.OrderReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.OrderRespDTO;
import com.xiaolian.amigo.data.network.model.order.Order;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.order.adaptor.OrderAdaptor;
import com.xiaolian.amigo.ui.wallet.adaptor.PrepayAdaptor;
import com.xiaolian.amigo.ui.wallet.intf.IPrepayPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IPrepayView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * <p>
 * Created by zcd on 10/10/17.
 */

public class PrepayPresenter<V extends IPrepayView> extends BasePresenter<V>
        implements IPrepayPresenter<V> {
    private static final String TAG = PrepayPresenter.class.getSimpleName();
    private IOrderDataManager manager;

    @Inject
    public PrepayPresenter(IOrderDataManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void requestPrepay(int page) {
        OrderReqDTO reqDTO = new OrderReqDTO();
        reqDTO.setPage(page);
        reqDTO.setSize(Constant.PAGE_SIZE);
        // 查看未结束账单
        reqDTO.setOrderStatus(1);
        addObserver(manager.queryOrders(reqDTO), new NetworkObserver<ApiResult<OrderRespDTO>>(false, true) {
            @Override
            public void onReady(ApiResult<OrderRespDTO> result) {
                getMvpView().setRefreshComplete();
                getMvpView().setLoadMoreComplete();
                getMvpView().hideEmptyView();
                getMvpView().hideErrorView();
                if (null == result.getError()) {
                    if (null != result.getData().getOrders() && result.getData().getOrders().size() > 0) {
                        List<PrepayAdaptor.OrderWrapper> wrappers = new ArrayList<PrepayAdaptor.OrderWrapper>();
                        for (Order order : result.getData().getOrders()) {
                            wrappers.add(new PrepayAdaptor.OrderWrapper(order));
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
}
