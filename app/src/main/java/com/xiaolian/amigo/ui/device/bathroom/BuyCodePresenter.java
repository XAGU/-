package com.xiaolian.amigo.ui.device.bathroom;

import android.util.Log;

import com.xiaolian.amigo.data.enumeration.BathTradeType;
import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathPreBookingRespDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBuyCodeView;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBuyCodePresenter;
import com.xiaolian.amigo.util.RxHelper;
import com.xiaolian.amigo.util.TimeUtils;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;


/**
 * 购买编码
 *
 * @author zcd
 * @date 18/6/29
 */
public class BuyCodePresenter<V extends IBuyCodeView> extends BasePresenter<V>
        implements IBuyCodePresenter<V> {
    private static final String TAG = BuyCodePresenter.class.getSimpleName();
    private IBathroomDataManager bathroomDataManager;
    private Long bathOrderId ;
    private Subscription subscription;
    @Inject
    public BuyCodePresenter(IBathroomDataManager bathroomDataManager) {
        this.bathroomDataManager = bathroomDataManager;
    }

    @Override
    public void preBuyVoucher() {
        addObserver(bathroomDataManager.preBuyVoucher() ,new NetworkObserver<ApiResult<BathPreBookingRespDTO>>(){

            @Override
            public void onReady(ApiResult<BathPreBookingRespDTO> result) {
                if (result.getError() == null){
                    getMvpView().preBuy(result.getData());
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void pay(Double prepayAmount ,Long bonusId) {
//        BathOrderReqDTO reqDTO = new BathOrderReqDTO();
//        reqDTO.setPrepayAmount(prepayAmount);
//        reqDTO.setType(BathTradeType.BUY_CODE.getCode());
//        reqDTO.setUserBonusId(bonusId);
//        addObserver(bathroomDataManager.pay(reqDTO),
//                new NetworkObserver<ApiResult<BathOrderRespDTO>>() {
//
//                    @Override
//                    public void onReady(ApiResult<BathOrderRespDTO> result) {
//                        if (null == result.getError()) {
//                            bathOrderId = result.getData().getBathOrderId();
//                            getMvpView().bookingSuccess(result.getData());
//                        } else {
//                            getMvpView().onError(result.getError().getDisplayMessage());
//                        }
//                    }
//                });
    }

    @Override
    public void cancel(Long bathOrderId) {
        SimpleReqDTO reqDTO = new SimpleReqDTO();
        reqDTO.setId(bathOrderId);
        addObserver(bathroomDataManager.cancel(reqDTO),
                new NetworkObserver<ApiResult<BooleanRespDTO>>() {

                    @Override
                    public void onReady(ApiResult<BooleanRespDTO> result) {
                        if (null == result.getError()) {
                            getMvpView().bookingCancel(result.getData());
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }
                });
    }

    @Override
    public void BathroomCountDown(int time) {
        int currTime = (int) TimeUtils.intervalTime(time);
        subscription = RxHelper.countDown(currTime)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        getMvpView().setBookingCountDownTime(TimeUtils.orderBathroomLastTime(currTime,""));
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
                        Log.e(TAG, "onNext: " + integer );
                       if (integer >=0){
                           getMvpView().setBookingCountDownTime(TimeUtils.orderBathroomLastTime(currTime - integer,""));
                       }else{

                       }
                    }
                });
        this.subscriptions.add(subscription);
    }

}
