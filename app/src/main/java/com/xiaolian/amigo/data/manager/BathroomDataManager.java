package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.data.network.IBathroomApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBuildingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderPreconditionRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathPreBookingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.QueryBathOrderListReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.QueryBathOrderListRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.ShowerRoomRouterRespDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.di.BathroomServer;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * @author zcd
 * @date 18/6/29
 */
public class BathroomDataManager implements IBathroomDataManager {

    private IBathroomApi bathroomApi;

    private ISharedPreferencesHelp iSharedPreferencesHelp ;
    @Inject
    public BathroomDataManager(@BathroomServer Retrofit retrofit ,ISharedPreferencesHelp iSharedPreferencesHelp) {
        this.bathroomApi = retrofit.create(IBathroomApi.class);
        this.iSharedPreferencesHelp = iSharedPreferencesHelp ;
    }

    @Override
    public Observable<ApiResult<BathBuildingRespDTO>> list(SimpleReqDTO reqDTO) {
        return bathroomApi.list(reqDTO);
    }

    @Override
    public Observable<ApiResult<ShowerRoomRouterRespDTO>> route() {
        return bathroomApi.route();
    }

    @Override
    public Observable<ApiResult<BathPreBookingRespDTO>> preBooking(BathBookingReqDTO reqDTO) {
        return bathroomApi.preBooking(reqDTO);
    }

    @Override
    public Observable<ApiResult<BathOrderRespDTO>> pay(BathOrderReqDTO reqDTO) {
        return bathroomApi.pay(reqDTO);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> cancel(SimpleReqDTO reqDTO) {
        return bathroomApi.cancel(reqDTO);
    }

    @Override
    public Observable<ApiResult<QueryBathOrderListRespDTO>> getOrderRecordList(QueryBathOrderListReqDTO reqDTO) {
        return bathroomApi.getOrderRecordList(reqDTO);
    }

    @Override
    public Long getUserId() {
        return iSharedPreferencesHelp.getUserInfo().getId();
    }

    @Override
    public Observable<ApiResult<BathOrderPreconditionRespDTO>> getOrderPrecondition() {
        return bathroomApi.getOrderPrecondition();
    }


}
