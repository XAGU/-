package com.xiaolian.amigo.ui.device.bathroom;

import android.text.TextUtils;
import android.util.Log;

import com.xiaolian.amigo.data.enumeration.BathTradeType;
import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingStatusReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathPreBookingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathRoomReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BookingQueueProgressDTO;
import com.xiaolian.amigo.data.network.model.bathroom.TryBookingResultRespDTO;
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
import rx.functions.Action1;

import static com.xiaolian.amigo.util.Constant.ACCEPTED;
import static com.xiaolian.amigo.util.Constant.CANCELED;
import static com.xiaolian.amigo.util.Constant.EXPIRED;
import static com.xiaolian.amigo.util.Constant.FAIL;
import static com.xiaolian.amigo.util.Constant.INIT;
import static com.xiaolian.amigo.util.Constant.OPENED;


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
    public void cancelCountDown() {
        if (subscription != null && !subscription.isUnsubscribed()){
            Log.e(TAG, "cancelCountDown: " );
            subscription.unsubscribe();
            this.subscriptions.remove(subscription);
        }else{
            Log.e(TAG, "cancelCountDown: "  + (subscriptions== null) + (subscriptions.isUnsubscribed()) );
        }
    }

    /**
     * 预约时查询订单状态
     * @param bathOrderId
     * @param isToUsing  是否是已预约成功界面查询订单状态，预约成功时要一直查询订单状态，看是否是预约中， 如果是否， 则表明
     *                   是预约中界面查询， 要最多查询5次是否为预约成功
     */
    @Override
    public void query(String bathOrderId , boolean isToUsing , int time) {
        Log.e(TAG, "query: " + isToUsing  + "     " +time);
        BathBookingStatusReqDTO reqDTO = new BathBookingStatusReqDTO();
        reqDTO.setId(bathOrderId);
        addObserver(bathroomDataManager.query(reqDTO) , new NetworkObserver<ApiResult<BathBookingRespDTO>>(){

            @Override
            public void onReady(ApiResult<BathBookingRespDTO> result) {
                if (result.getError() == null) {
                    if (isToUsing) {
                        if (result.getData().getStatus() == OPENED) {
                            getMvpView().gotoUsing(result.getData().getBathOrderId());
                        } else if (result.getData().getStatus() == ACCEPTED) {
                            delay(time, new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
                                    query(bathOrderId, true , time);
                                }
                            });

                        } else if (result.getData().getStatus() == EXPIRED) {
                            getMvpView().appointMentTimeOut();
                        }
                    } else {
                          if (result.getData().getStatus() == ACCEPTED){  // 成功
                                getMvpView().bookingSuccess(result.getData());
                                query(bathOrderId ,true ,10);
                          } else if (result.getData().getStatus() == OPENED){  // 使用中
                              getMvpView().gotoUsing(result.getData().getBathOrderId());
                          }else if (result.getData().getStatus() == EXPIRED){   //  超时
                              getMvpView().appointMentTimeOut(result.getData());
                          }
                    }
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
    public void booking(String device ) {
        BathBookingReqDTO reqDTO = new BathBookingReqDTO();
        if (TextUtils.isEmpty(device)){
            reqDTO.setDeviceNo(0);
            reqDTO.setType(BathTradeType.BOOKING_WITHOUT_DEVICE.getCode());
        }else{
            reqDTO.setDeviceNo(Integer.parseInt(device));
            reqDTO.setType(BathTradeType.BOOKING_DEVICE.getCode());
        }

        addObserver(bathroomDataManager.booking(reqDTO) , new NetworkObserver<ApiResult<TryBookingResultRespDTO>>(){

            @Override
            public void onStart() {
                getMvpView().showWaitLoading();
            }

            @Override
            public void onReady(ApiResult<TryBookingResultRespDTO> result) {
                if (null == result.getError()) {


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
                        getMvpView().appointMentTimeOut();
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

    @Override
    public void queryQueueId(long id) {
        SimpleReqDTO simpleReqDTO = new SimpleReqDTO();
        simpleReqDTO.setId(id);
        addObserver(bathroomDataManager.queueQuery(simpleReqDTO) , new NetworkObserver<ApiResult<BookingQueueProgressDTO>>(){

            @Override
            public void onStart() {

            }

            @Override
            public void onReady(ApiResult<BookingQueueProgressDTO> result) {
                    if (result.getError() == null){
                        if (result.getData().getBathBookingId() == 0){
                            getMvpView().showQueue(result.getData());
                            delay(5, new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
                                    queryQueueId(id);
                                }
                            });
                        }else {
                            query(result.getData().getBathBookingId()+"" ,false ,10);
                        }
                    }else{
                        getMvpView().onError(result.getError().getDisplayMessage());
                    }
            }
        });
    }

    @Override
    public void cancelQueue(long id) {
        SimpleReqDTO simpleReqDTO = new SimpleReqDTO();
        simpleReqDTO.setId(id);
        addObserver(bathroomDataManager.cancelQueue(simpleReqDTO) , new NetworkObserver<ApiResult<BooleanRespDTO>>(){

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (result.getError() == null){
                    getMvpView().finishActivity();
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }


    private void delay(int time ,  Action1<Long> action0 ){
        this.subscriptions.add(RxHelper.delay(time , action0));
    }
}
