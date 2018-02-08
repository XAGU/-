package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IClientServiceDataManager;
import com.xiaolian.amigo.data.network.IClientServiceApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.cs.CsMobileRespDTO;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * client service
 *
 * @author caidong
 * @date 17/11/2
 */
public class ClientServiceDataManager implements IClientServiceDataManager {

    private IClientServiceApi clientServiceApi;

    @Inject
    public ClientServiceDataManager(Retrofit retrofit) {
        clientServiceApi = retrofit.create(IClientServiceApi.class);
    }

    @Override
    public Observable<ApiResult<CsMobileRespDTO>> queryCsInfo() {
        return clientServiceApi.queryCsInfo();
    }
}
