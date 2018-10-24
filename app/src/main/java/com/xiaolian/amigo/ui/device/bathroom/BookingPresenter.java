package com.xiaolian.amigo.ui.device.bathroom;

import android.text.TextUtils;
import android.util.Log;

import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingStatusReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderCurrentRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathRoomReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BookingQueueProgressDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingView;
import com.xiaolian.amigo.util.RxHelper;
import com.xiaolian.amigo.util.TimeUtils;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

import static com.xiaolian.amigo.util.Constant.ACCEPTED;
import static com.xiaolian.amigo.util.Constant.CANCELED;
import static com.xiaolian.amigo.util.Constant.EXPIRED;
import static com.xiaolian.amigo.util.Constant.FINISHED;
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

    private static int queryCancle = 0 ;  //  查询取消状态  ， 最多5次

    boolean isOnPause = false  ;

    boolean timeOut = false ;

    private long diffTime ;  // 服务器时间与系统时间的差值

    private ISharedPreferencesHelp iSharedPreferencesHelp ;

    private int notifyTime = 0 ;

    private String bookId  ;
    @Inject
    public BookingPresenter(IBathroomDataManager bathroomDataManager , ISharedPreferencesHelp sharedPreferencesHelp) {
        this.bathroomDataManager = bathroomDataManager;
        this.iSharedPreferencesHelp = sharedPreferencesHelp ;
    }



        
    @Override
    public void cancel(long id) {
        SimpleReqDTO reqDTO = new SimpleReqDTO();
        reqDTO.setId(id);
        addObserver(bathroomDataManager.cancel(reqDTO),
                new NetworkObserver<ApiResult<BooleanRespDTO>>() {
                    @Override
                    public void onStart() {
                        getMvpView().showLoading("正在取消预约，T~T");
                    }

                    @Override
                    public void onReady(ApiResult<BooleanRespDTO> result) {
                        if (null == result.getError()) {
                            if (result.getData().isResult()) {
                                getMvpView().hideWaitLoading(true);
                                getMvpView().bookingCancel();
                            }else{
                                queryCancle = 1 ;
                                query(id);
                            }
                        } else {
                            getMvpView().hideWaitLoading(false);
                            getMvpView().onError(result.getError().getDisplayMessage());

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getMvpView().hideWaitLoading(false);
                    }
                });


    }

    @Override
    public void onPause() {
        this.isOnPause = true ;
    }

    @Override
    public void onResume() {
        this.isOnPause = false ;
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
    public void query(String bathOrderId , boolean isToUsing , int time , boolean isShowDialog) {
        if (isOnPause  || timeOut)  return ;
        BathBookingStatusReqDTO reqDTO = new BathBookingStatusReqDTO();
        reqDTO.setId(bathOrderId);
        this.bookId = bathOrderId ;
        addObserver(bathroomDataManager.query(reqDTO) , new NetworkObserver<ApiResult<BathBookingRespDTO>>(){


            @Override
            public void onStart() {
                if (isShowDialog) {
                    super.onStart();
                }else{

                }
            }

            @Override
            public void onReady(ApiResult<BathBookingRespDTO> result) {
                if (result.getError() == null) {
                    if (isToUsing) {
                        if (result.getData().getStatus() == OPENED) {
                            getMvpView().gotoUsing(result.getData().getBathOrderId());
                        } else if (result.getData().getStatus() == ACCEPTED) {
                            delay(time, aLong -> {
                                Log.e(TAG, "queryBookign:  >>>> delay"   );
                                query(bathOrderId, true , time , false);
                            });

                        } else if (result.getData().getStatus() == EXPIRED) {
                            getMvpView().appointMentTimeOut(true);
                        }else if (result.getData().getStatus() == FINISHED){
                            queryTradeOrder(result.getData().getBathOrderId());
                        }
                    } else {
                          if (result.getData().getStatus() == ACCEPTED){  // 成功
                                getMvpView().bookingSuccess(result.getData());
//                                query(bathOrderId ,true ,10 , false);
                          } else if (result.getData().getStatus() == OPENED){  // 使用中
                              Log.e(TAG, "queryBookign: >>>>> using"   );
                              getMvpView().gotoUsing(result.getData().getBathOrderId());
                          }else if (result.getData().getStatus() == EXPIRED){   //  超时
                              getMvpView().appointMentTimeOut(result.getData());
                          }else if (result.getData().getStatus() ==FINISHED){
                              queryTradeOrder(result.getData().getBathOrderId());
                          }
                    }
                }
                }
        });
    }



    /**
     * 预约时查询订单状态
     * @param
     */
    public void query(long bookingId ) {
        if (isOnPause   )  return ;
        BathBookingStatusReqDTO reqDTO = new BathBookingStatusReqDTO();
        reqDTO.setId(bookingId+"");
        addObserver(bathroomDataManager.query(reqDTO) , new NetworkObserver<ApiResult<BathBookingRespDTO>>() {


            @Override
            public void onStart() {
            }

            @Override
            public void onReady(ApiResult<BathBookingRespDTO> result) {
                if (result.getError() == null) {
                    if (result.getData().getStatus() == ACCEPTED){
                        if (queryCancle < 4) {
                            delay(3, aLong -> {
                                queryCancle++;
                                query(bookingId);
                            });
                        }else{
                            queryCancle = 0 ;
                            getMvpView().onError("取消预约失败");
                            getMvpView().hideWaitLoading(false);
                        }
                    }else if (result.getData().getStatus() ==CANCELED){
                        queryCancle = 0 ;
                        getMvpView().bookingCancel();
                        getMvpView().hideWaitLoading(true);
                    }
                }else{
                    queryCancle = 0 ;
                    getMvpView().onError(result.getError().getDisplayMessage());
                    getMvpView().hideWaitLoading(false);
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
    public void countDownexpiredTime(long expiredTime ) {

        int countTime = TimeUtils.intervalTime(expiredTime , getDiffTime());
        if (countTime > 0) {


                subscription = RxHelper.countDown(countTime)
                        .doOnSubscribe(() -> {
                            getMvpView().countTimeLeft(TimeUtils.orderBathroomLastTime(expiredTime, "" ,getDiffTime()));
                        })
                        .subscribe(new Subscriber<Integer>() {
                            @Override
                            public void onCompleted() {
                                if (!isOnPause) {
                                    getMvpView().countTimeLeft("0:00");
                                    notifyExpired();

                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Integer integer) {
                                Log.e(TAG, "onNext: " + integer);
                                getMvpView().countTimeLeft(TimeUtils.orderBathroomLastTime(expiredTime, ""  , getDiffTime()));
                            }
                        });
            }else{
            if (!isOnPause) {
                getMvpView().countTimeLeft("0:00");
                notifyExpired();
            }
        }
            if (this.subscriptions != null && !subscriptions.isUnsubscribed() && subscription != null ) {
                this.subscriptions.add(subscription);
            }
        }



    public long getDiffTime() {
        return iSharedPreferencesHelp.getDiffTime();
    }


    @Override
    public void cancleDownExpiredTime() {
        if (subscriptions != null) {
            this.subscriptions.remove(subscription);
        }
    }


    @Override
    public void queryQueueId(long id , boolean isShowDialog) {
        if (isOnPause )  return ;
        SimpleReqDTO simpleReqDTO = new SimpleReqDTO();
        simpleReqDTO.setId(id);
        addObserver(bathroomDataManager.queueQuery(simpleReqDTO) , new NetworkObserver<ApiResult<BookingQueueProgressDTO>>(){

            @Override
            public void onStart() {
                if (isShowDialog){
                    super.onStart();
                }else{

                }
            }

            @Override
            public void onReady(ApiResult<BookingQueueProgressDTO> result) {
                    if (result.getError() == null){
                        if (result.getData().getBathBookingId() == 0){
                            getMvpView().showQueue(result.getData());
                        }else {
                            query(result.getData().getBathBookingId()+"" ,false ,10 , false);
                        }
                    }else{
                        getMvpView().onError(result.getError().getDisplayMessage());
                    }
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                delay(5, new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        queryQueueId(id , false);
                    }
                });
            }
        });
    }


    @Override
    public void cancelQueue(long id) {
        if (isOnPause )  return ;
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

    @Override
    public void notifyExpired() {
        addObserver(bathroomDataManager.notyfyExpired() ,new NetworkObserver<ApiResult<BooleanRespDTO>>(){

            @Override
            public void onStart() {

            }

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (result.getError() == null){
                    getMvpView().appointMentTimeOut(false);
                    notifyTime = 0 ;
                }else {
                    if (result.getError().getCode() == 1005 ) {
                        getMvpView().appointMentTimeOut(false);
                        notifyTime = 0;
                    }else if (result.getError().getCode() == 10013){
                        if (!TextUtils.isEmpty(bookId)){
                           query(bookId , true , 0 , false);
                        }
                        notifyTime = 0 ;
                    } else  if (result.getError().getCode() == 30113){
                        if (notifyTime < 5) {
                            RxHelper.delay(1, aLong -> notifyExpired());
                            notifyTime++;
                        }else{
                            getMvpView().appointMentTimeOut(false);
                            notifyTime = 0;
                        }
                    }

                }
            }
        });
    }




    @Override
    public void queryTradeOrder(long id) {

        SimpleReqDTO simpleReqDTO  = new SimpleReqDTO();
        simpleReqDTO.setId(id);
        addObserver(bathroomDataManager.orderQuery(simpleReqDTO) , new NetworkObserver<ApiResult<BathOrderCurrentRespDTO>>(){

            @Override
            public void onStart() {

            }


            @Override
            public void onReady(ApiResult<BathOrderCurrentRespDTO> result) {
                if (result.getError() == null) {
                    getMvpView().startOrderInfo(result.getData());
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public int getBookingMethod() {
        return bathroomDataManager.getBookMethod();
    }

    @Override
    public void setAppointmentTimeOut() {
        timeOut = true ;
    }



    private void delay(int time ,  Action1<Long> action0 ){
        this.subscriptions.add(RxHelper.delay(time , action0));
    }
}
