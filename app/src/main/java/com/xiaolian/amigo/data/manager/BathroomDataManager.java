package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.data.network.IBathroomApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingStatusReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBuildingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderCurrentRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderPreconditionRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathPreBookingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathRoomReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathRouteRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.QueryBathOrderListReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.QueryBathOrderListRespDTO;
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
    public Observable<ApiResult<BathBuildingRespDTO>> tree(SimpleReqDTO reqDTO) {
        return bathroomApi.tree(reqDTO);
    }

    @Override
    public Observable<ApiResult<BathRouteRespDTO>> route() {
        return bathroomApi.route();
    }

    @Override
    public Observable<ApiResult<BathPreBookingRespDTO>> preBooking(BathBookingReqDTO reqDTO) {
        return bathroomApi.preBooking(reqDTO);
    }


    @Override
    public Observable<ApiResult<BathOrderRespDTO>> cancel(SimpleReqDTO reqDTO) {
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

    @Override
    public Observable<ApiResult<BathOrderRespDTO>> query(BathBookingStatusReqDTO reqDTO) {
        return bathroomApi.query(reqDTO);
    }

    @Override
    public Observable<ApiResult<BathOrderCurrentRespDTO>> orderQuery(SimpleReqDTO reqDTO) {
        return bathroomApi.orderQuery(reqDTO);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> unlock(BathRoomReqDTO reqDTO) {
        return bathroomApi.unlock(reqDTO);
    }

    @Override
    public Observable<ApiResult<BathPreBookingRespDTO>> preBuyVoucher() {
        return bathroomApi.preBuyVoucher();
    }

    @Override
    public Observable<ApiResult<BathOrderRespDTO>> booking(BathBookingReqDTO reqDTO) {
        return bathroomApi.booking(reqDTO);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> askSettle(SimpleReqDTO reqDTO) {
        return bathroomApi.askSettle(reqDTO);
    }

    @Override
    public boolean getBathroomPassword() {
        return iSharedPreferencesHelp.getUserInfo().isHadSetBathPassword();
    }


}
