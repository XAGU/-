package com.xiaolian.amigo.ui.order;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.CheckComplaintReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.order.intf.IOrderDetailPresenter;
import com.xiaolian.amigo.ui.order.intf.IOrderDetailView;

import javax.inject.Inject;

/**
 * <p>
 * Created by zcd on 17/11/10.
 */

public class OrderDetailPresenter<V extends IOrderDetailView> extends BasePresenter<V>
    implements IOrderDetailPresenter<V> {
    private IOrderDataManager orderDataManager;

    @Inject
    public OrderDetailPresenter(IOrderDataManager orderDataManager) {
        this.orderDataManager = orderDataManager;
    }

    @Override
    public void checkComplaint(Long orderId, Integer orderType) {
        CheckComplaintReqDTO reqDTO = new CheckComplaintReqDTO();
        reqDTO.setOrderId(orderId);
        reqDTO.setOrderType(orderType);
        addObserver(orderDataManager.checkComplaint(reqDTO), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isResult()) {
                        getMvpView().onError(R.string.complaint_error);
                    } else {
                        getMvpView().toComplaint();
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
