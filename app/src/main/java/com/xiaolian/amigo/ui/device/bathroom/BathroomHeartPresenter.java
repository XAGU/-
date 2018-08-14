package com.xiaolian.amigo.ui.device.bathroom;

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

    private IBathroomDataManager bathroomDataManager ;

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
                        getMvpView().goToOrderInfo();
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
    public void queryBathroomOrder(Long id) {
        SimpleReqDTO simpleReqDTO  = new SimpleReqDTO();
        simpleReqDTO.setId(id);
        addObserver(bathroomDataManager.orderQuery(simpleReqDTO) , new NetworkObserver<ApiResult<BathOrderCurrentRespDTO>>(){

            @Override
            public void onReady(ApiResult<BathOrderCurrentRespDTO> result) {
                if (result.getError() == null){
                    if (result.getData().getStatus() == ORDER_USING) {
                        getMvpView().getOrderInfo(result.getData());
                        delay(3, new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                queryBathroomOrder(id);
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
