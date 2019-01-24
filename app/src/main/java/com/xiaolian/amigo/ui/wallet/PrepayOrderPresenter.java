package com.xiaolian.amigo.ui.wallet;

import android.text.TextUtils;

import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.order.Order;
import com.xiaolian.amigo.data.network.model.order.OrderDetailReqDTO;
import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.wallet.intf.IPrepayOrderPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IPrepayOrderView;

import javax.inject.Inject;

/**
 * 待找零账单
 *
 * @author zcd
 * @date 17/10/12
 */

public class PrepayOrderPresenter<V extends IPrepayOrderView> extends BasePresenter<V>
        implements IPrepayOrderPresenter<V> {
    @SuppressWarnings("unused")
    private static final String TAG = PrepayOrderPresenter.class.getSimpleName();
    private IWalletDataManager walletDataManager;
    private IOrderDataManager orderDataManager;

    @Inject
    PrepayOrderPresenter(IWalletDataManager walletDataManager, IOrderDataManager orderDataManager) {
        super();
        this.walletDataManager = walletDataManager;
        this.orderDataManager = orderDataManager;
    }

    @Override
    public void getOrder(Long orderId) {
        OrderDetailReqDTO reqDTO = new OrderDetailReqDTO();
        reqDTO.setId(orderId);

        addObserver(orderDataManager.queryOrderDetail(reqDTO), new NetworkObserver<ApiResult<OrderDetailRespDTO>>() {
            @Override
            public void onReady(ApiResult<OrderDetailRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().render(result.getData());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
