package com.xiaolian.amigo.ui.device.bathroom;

import com.xiaolian.amigo.data.enumeration.BathTradeType;
import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingView;

import javax.inject.Inject;

/**
 * 预约使用
 *
 * @author zcd
 * @date 18/6/29
 */
public class BookingPresenter<V extends IBookingView> extends BasePresenter<V>
        implements IBookingPresenter<V> {
    private IBathroomDataManager bathroomDataManager;
    private Long bathOrderId;

    @Inject
    public BookingPresenter(IBathroomDataManager bathroomDataManager) {
        this.bathroomDataManager = bathroomDataManager;
    }

    @Override
    public void pay(Double prepayAmount, Long bonusId) {
        BathOrderReqDTO reqDTO = new BathOrderReqDTO();
        reqDTO.setPrepayAmount(prepayAmount);
        reqDTO.setType(BathTradeType.BOOKING.getCode());
        reqDTO.setUserBonusId(bonusId);
        addObserver(bathroomDataManager.pay(reqDTO),
                new NetworkObserver<ApiResult<BathOrderRespDTO>>() {

                    @Override
                    public void onReady(ApiResult<BathOrderRespDTO> result) {
                        if (null == result.getError()) {
                            bathOrderId = result.getData().getBathOrderId();
                            getMvpView().bookingSuccess(result.getData());
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }
                });
    }

    @Override
    public void cancel() {
        SimpleReqDTO reqDTO = new SimpleReqDTO();
        reqDTO.setId(bathOrderId);
        addObserver(bathroomDataManager.cancel(reqDTO),
                new NetworkObserver<ApiResult<BooleanRespDTO>>() {

                    @Override
                    public void onReady(ApiResult<BooleanRespDTO> result) {
                        if (null == result.getError()) {
                            getMvpView().bookingCancel();
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }
                });
    }
}
