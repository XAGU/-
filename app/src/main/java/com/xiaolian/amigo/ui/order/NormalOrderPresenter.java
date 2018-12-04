package com.xiaolian.amigo.ui.order;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.ComplaintType;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.complaint.CheckComplaintReqDTO;
import com.xiaolian.amigo.data.network.model.order.OrderDetailReqDTO;
import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.order.intf.INormalOrderPresenter;
import com.xiaolian.amigo.ui.order.intf.INormalOrderView;
import com.xiaolian.amigo.util.Constant;

import javax.inject.Inject;

/**
 * 订单presenter
 *
 * @author zcd
 * @date 18/1/18
 */

public class NormalOrderPresenter<V extends INormalOrderView> extends BasePresenter<V>
        implements INormalOrderPresenter<V> {
    private IOrderDataManager orderDataManager;
    private Long orderId;
    private OrderDetailRespDTO order;

    @Inject
    NormalOrderPresenter(IOrderDataManager orderDataManager) {
        this.orderDataManager = orderDataManager;
    }

    @Override
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public Long getOrderId() {
        return orderId;
    }

    @Override
    public void requestOrder() {
        OrderDetailReqDTO reqDTO = new OrderDetailReqDTO();
        reqDTO.setId(orderId);

        addObserver(orderDataManager.queryOrderDetail(reqDTO), new NetworkObserver<ApiResult<OrderDetailRespDTO>>() {
            @Override
            public void onReady(ApiResult<OrderDetailRespDTO> result) {
                if (null == result.getError()) {
//                    if (result.getData().getLowest() != null
//                            && result.getData().getLowest()
//                            && TextUtils.isEmpty(result.getData().getBonus())) {
//                        getMvpView().showNoUseTip();
//                    }
                    order = result.getData();
                    getMvpView().renderView(result.getData());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void checkComplaint() {
        if (order == null) {
            return;
        }
        CheckComplaintReqDTO reqDTO = new CheckComplaintReqDTO();
        reqDTO.setOrderId(order.getId());
        reqDTO.setOrderType(ComplaintType.getComplaintTypeByDeviceType(
                Device.getDevice(order.getDeviceType())).getType());
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

    @Override
    public String getComplaintParam() {
        return Constant.H5_COMPLAINT
                + "?accessToken=" + orderDataManager.getAccessToken()
                + "&refreshToken=" + orderDataManager.getRefreshToken()
                + "&orderId=" + order.getId()
                + "&orderNo=" + order.getOrderNo()
                + "&orderType="
                + ComplaintType.getComplaintTypeByDeviceType(
                Device.getDevice(order.getDeviceType())).getType();
    }

    @Override
    public OrderDetailRespDTO getOrder() {
        return order;
    }
}
