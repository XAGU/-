package com.xiaolian.amigo.ui.device.bathroom;

import android.text.TextUtils;
import android.util.Log;

import com.xiaolian.amigo.data.enumeration.BathTradeType;
import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingStatusReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathPreBookingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathRoomReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingView;
import com.xiaolian.amigo.util.RxHelper;
import com.xiaolian.amigo.util.TimeUtils;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;

import static com.xiaolian.amigo.util.Constant.ACCEPTED;


/**
 * 预约使用
 *
 * @author zcd
 * @date 18/6/29
 */
public class BookingPresenter<V extends IBookingView> extends BasePresenter<V>
        implements IBookingPresenter<V> {
    private static final String TAG = BookingPresenter.class.getSimpleName();
    private IBathroomDataManager bathroomDataManager;
    private Long bathOrderId;  //  设备订单
    private Subscription subscription;
    private static int queryNum = 0 ;  //  查询订单状态次数，最多只能五次
    @Inject
    public BookingPresenter(IBathroomDataManager bathroomDataManager) {
        this.bathroomDataManager = bathroomDataManager;
    }


    @Override
    public void cancel(long id) {
        SimpleReqDTO reqDTO = new SimpleReqDTO();
        reqDTO.setId(id);
        addObserver(bathroomDataManager.cancel(reqDTO),
                new NetworkObserver<ApiResult<BathOrderRespDTO>>() {

                    @Override
                    public void onReady(ApiResult<BathOrderRespDTO> result) {
                        if (null == result.getError()) {
                            getMvpView().bookingCancel(result.getData());
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }
                });
    }

    @Override
    public void bathroomBookingCountDown() {
        subscription = RxHelper.countDown(120)
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
                        if (integer == 120) {
                            getMvpView().setBookingCountDownTime("02:00");
                        } else if (integer >= 60) {
                            getMvpView().setBookingCountDownTime("0" + integer / 60 + ":" + integer % 60);
                        } else if (integer > 0) {
                            getMvpView().setBookingCountDownTime("0" + ":" + integer % 60);
                        } else {
                            getMvpView().finishActivity();
                        }
                    }
                });
        this.subscriptions.add(subscription);
    }

    @Override
    public void cancelCountDown() {
        if (subscription != null && !subscription.isUnsubscribed()){
            Log.e(TAG, "cancelCountDown: " );
            subscription.unsubscribe();
            this.subscriptions.remove(subscription);
        }else{
            Log.e(TAG, "cancelCountDown: "  + (subscriptions== null) + (subscriptions.isUnsubscribed()) );
        }
    }

    @Override
    public void preBooking(String deviceNo) {
        BathBookingReqDTO reqDTO = new BathBookingReqDTO();
        if (TextUtils.isEmpty(deviceNo)|| "null".equals(deviceNo)){
            reqDTO.setDeviceNo(null);
            reqDTO.setType(BathTradeType.BUY_CODE.getCode());
        }else{
            reqDTO.setDeviceNo(Integer.parseInt(deviceNo));
            reqDTO.setType(BathTradeType.BOOKING.getCode());
        }

        addObserver(bathroomDataManager.preBooking(reqDTO), new NetworkObserver<ApiResult<BathPreBookingRespDTO>>() {

            @Override
            public void onReady(ApiResult<BathPreBookingRespDTO> result) {
                if (null == result.getError()) {
//                    if (result.getData().getBonus() != null) {
                        getMvpView().reschedule(result.getData());
//                    }else{
//                        getMvpView().gotoBookingView(result.getData().getBalance(),
//                                null, null,
//                                null, result.getData().getExpiredTime(),
//                                result.getData().getLocation(), result.getData().getMaxMissAbleTimes(),
//                                result.getData().getMinPrepay(), result.getData().getMissedTimes(),
//                                result.getData().getPrepay() ,result.getData().getReservedTime());
//                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void query(String bathOrderId) {
        BathBookingStatusReqDTO reqDTO = new BathBookingStatusReqDTO();
        reqDTO.setId(bathOrderId);
        addObserver(bathroomDataManager.query(reqDTO) , new NetworkObserver<ApiResult<BathOrderRespDTO>>(){

            @Override
            public void onReady(ApiResult<BathOrderRespDTO> result) {
                if (result.getError() == null){
//                    if (result.getData().getStatus() == ACCEPTED.getCode()){
//                        getMvpView().bookingSuccess(result.getData());
//                    }else{
//                        if (queryNum < 5) {
//                            queryNum++ ;
//                            delay(3, new Subscriber<Long>() {
//                                @Override
//                                public void onCompleted() {
//
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//
//                                }
//
//                                @Override
//                                public void onNext(Long aLong) {
//                                    query(bathOrderId);
//                                }
//                            });
//                        }else{
//
//                        }
//                    }
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void unLock(String deviceNo) {
        BathRoomReqDTO reqDTO = new BathRoomReqDTO();
        reqDTO.setDeviceNo(deviceNo);
        addObserver(bathroomDataManager.unlock(reqDTO) , new NetworkObserver<ApiResult<BooleanRespDTO>>(){

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (result.getError() == null){

                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void booking(String device) {
        BathBookingReqDTO reqDTO = new BathBookingReqDTO();
        if (TextUtils.isEmpty(device)){
            reqDTO.setDeviceNo(null);
        }else{
            reqDTO.setDeviceNo(Integer.parseInt(device));
        }
        reqDTO.setType(BathTradeType.BUY_CODE.getCode());
        addObserver(bathroomDataManager.booking(reqDTO) , new NetworkObserver<ApiResult<BathOrderRespDTO>>(){

            @Override
            public void onStart() {
                getMvpView().showWaitLoading();
            }

            @Override
            public void onReady(ApiResult<BathOrderRespDTO> result) {
                if (null == result.getError()) {
                    bathOrderId = result.getData().getBathOrderId();
                    if (result.getData().getStatus() ==ACCEPTED ) {
                              getMvpView().bookingSuccess(result.getData());
                        }else{

                              query(result.getData().getBathOrderId()+"");
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
                getMvpView().hideWaitLoading();
            }
        });
    }

    @Override
    public void countDownexpiredTime(long expiredTime) {
             int countTime = TimeUtils.intervalTime(expiredTime);
        subscription = RxHelper.countDown(countTime)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        getMvpView().countTimeLeft(TimeUtils.orderBathroomLastTime(expiredTime ,""));
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
                        getMvpView().countTimeLeft(TimeUtils.orderBathroomLastTime(expiredTime ,""));
                    }
                });
        this.subscriptions.add(subscription);
    }


    private void delay(int time ,Subscriber<Long> subscription){
        this.subscriptions.add(RxHelper.delay(time ,subscription));
    }
}
