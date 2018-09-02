package com.xiaolian.amigo.ui.device.bathroom;

import android.util.Log;

import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderCurrentRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBathroomHeartPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBathroomHeartView;
import com.xiaolian.amigo.util.RxHelper;

import javax.inject.Inject;

import rx.functions.Action1;

import static com.xiaolian.amigo.util.Constant.ORDER_SETTLE;
import static com.xiaolian.amigo.util.Constant.ORDER_USING;

public class BathroomHeartPresenter<V extends IBathroomHeartView> extends BasePresenter<V>
        implements IBathroomHeartPresenter<V> {

    private static final String TAG = BathroomHeartPresenter.class.getSimpleName();
    private IBathroomDataManager bathroomDataManager ;
    private boolean isPause = false ;

    @Inject
    public BathroomHeartPresenter(IBathroomDataManager bathroomDataManager) {
        this.bathroomDataManager = bathroomDataManager;
    }

    @Override
    public void askSettle(Long id) {
        SimpleReqDTO simpleReqDTO  = new SimpleReqDTO();
        simpleReqDTO.setId(id);
        addObserver(bathroomDataManager.askSettle(simpleReqDTO) , new NetworkObserver<ApiResult<BooleanRespDTO>>(){

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (result.getError() == null){
                    if (result.getData().isResult()){
                        getMvpView().onSuccess("结算成功");
                        Log.e(TAG, "onReady: >>>> askSettle" );
//                        getMvpView().goToOrderInfo();
                        delay(3, new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                queryBathroomOrder(id ,false , 3);
                            }
                        });
                    }else{
                        getMvpView().onError(result.getData().getFailReason());
                        getMvpView().reset();
                    }

                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                    getMvpView().reset();
                }
            }
        });
    }


    @Override
    public void onResume() {
        isPause = false ;
    }


    @Override
    public void onPause() {
        isPause = true ;
    }

    @Override
    public void queryBathroomOrder(Long id , boolean isShowDialog , int  time) {

        if (isPause) return ;
        SimpleReqDTO simpleReqDTO  = new SimpleReqDTO();
        simpleReqDTO.setId(id);
        addObserver(bathroomDataManager.orderQuery(simpleReqDTO) , new NetworkObserver<ApiResult<BathOrderCurrentRespDTO>>(){

            @Override
            public void onStart() {
                if (isShowDialog) {
                    super.onStart();
                }
            }


            @Override
            public void onReady(ApiResult<BathOrderCurrentRespDTO> result) {

                Log.e(TAG, "onReady: " );
                if (result.getError() == null){
                    if (result.getData().getStatus() == ORDER_USING) {
                        getMvpView().getOrderInfo(result.getData());
                        delay(time, new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                Log.e(TAG, "call:>>>>> delay " );
                                queryBathroomOrder(id , false ,time);
                            }
                        });
                    }else if (result.getData().getStatus() == ORDER_SETTLE){
                        getMvpView().goToOrderInfo(result.getData());
                    }

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
