package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.data.network.IBathroomApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathBuildingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.ShowerRoomRouterRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
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

    @Inject
    public BathroomDataManager(@BathroomServer Retrofit retrofit) {
        this.bathroomApi = retrofit.create(IBathroomApi.class);
    }

    @Override
    public Observable<ApiResult<BathBuildingRespDTO>> list(SimpleReqDTO reqDTO) {
        return bathroomApi.list(reqDTO);
    }

    @Override
    public Observable<ApiResult<ShowerRoomRouterRespDTO>> route() {
        return bathroomApi.route();
    }
}
