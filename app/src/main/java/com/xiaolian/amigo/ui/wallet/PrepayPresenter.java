package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.order.OrderInListDTO;
import com.xiaolian.amigo.data.network.model.order.OrderReqDTO;
import com.xiaolian.amigo.data.network.model.order.OrderRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.wallet.adaptor.PrepayAdaptor;
import com.xiaolian.amigo.ui.wallet.intf.IPrepayPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IPrepayView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 预付列表
 *
 * @author zcd
 * @date 17/10/10
 */

public class PrepayPresenter<V extends IPrepayView> extends BasePresenter<V>
        implements IPrepayPresenter<V> {
    @SuppressWarnings("unused")
    private static final String TAG = PrepayPresenter.class.getSimpleName();
    private IOrderDataManager orderDataManager;

    @Inject
    PrepayPresenter(IOrderDataManager manager) {
        super();
        this.orderDataManager = manager;
    }

    @Override
    public void requestPrepay(int page) {
        OrderReqDTO reqDTO = new OrderReqDTO();
        reqDTO.setPage(page);
        reqDTO.setSize(Constant.PAGE_SIZE);
        // 查看未结束账单
        reqDTO.setOrderStatus(1);
        addObserver(orderDataManager.queryPrepay(reqDTO), new NetworkObserver<ApiResult<OrderRespDTO>>(false, true) {
            @Override
            public void onReady(ApiResult<OrderRespDTO> result) {
                getMvpView().setRefreshComplete();
                getMvpView().setLoadMoreComplete();
                getMvpView().hideEmptyView();
                getMvpView().hideErrorView();
                if (null == result.getError()) {
                    if (null != result.getData().getOrders() && result.getData().getOrders().size() > 0) {
                        List<PrepayAdaptor.OrderWrapper> wrappers = new ArrayList<>();
                        for (OrderInListDTO order : result.getData().getOrders()) {
                            wrappers.add(new PrepayAdaptor.OrderWrapper(order.transform()));
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
