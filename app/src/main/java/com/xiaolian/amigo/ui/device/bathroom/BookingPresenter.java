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
import com.xiaolian.amigo.util.RxHelper;

import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;

import rx.Subscriber;
import rx.functions.Action0;

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

    @Override
    public void bathroomBookingCountDown() {
        this.subscriptions.add(RxHelper.countDown(120)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        getMvpView().setBookingCountDownTime("02:00");
                    }
                })
        .subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                getMvpView().setBookingCountDownTime("0:00");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                if (integer == 120){
                    getMvpView().setBookingCountDownTime("02:00");
                }
                else if (integer >=60){
                    getMvpView().setBookingCountDownTime("0"+integer / 60 +":" + integer % 60);
                }else if (integer >0){
                    getMvpView().setBookingCountDownTime("0"+":"+integer % 60);
                }else{
                    getMvpView().finishActivity();
                }
            }
        }));
    }


}
