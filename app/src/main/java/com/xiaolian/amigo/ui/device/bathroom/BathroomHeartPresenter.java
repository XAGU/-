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

import javax.inject.Inject;

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
                    getMvpView().getOrderInfo(result.getData());
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());

                }
            }
        });
    }
}
