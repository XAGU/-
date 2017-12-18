package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IFavoriteManager;
import com.xiaolian.amigo.data.network.IDeviceApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.device.QueryWaterListRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * favorite manager
 * Created by caidong on 2017/9/18.
 */
public class FavoriteManager implements IFavoriteManager {

    private static final String TAG = FavoriteManager.class.getSimpleName();

    private IDeviceApi deviceApi;

    @Inject
    public FavoriteManager(Retrofit retrofit) {
        deviceApi = retrofit.create(IDeviceApi.class);
    }

    @Override
    public Observable<ApiResult<QueryWaterListRespDTO>> queryFavorites(SimpleQueryReqDTO reqDTO) {
        return deviceApi.queryFavorites(reqDTO);
    }

    @Override
    public Observable<ApiResult<SimpleRespDTO>> favorite(SimpleReqDTO reqDTO) {
        return deviceApi.favorite(reqDTO);
    }

    @Override
    public Observable<ApiResult<SimpleRespDTO>> unFavorite(SimpleReqDTO reqDTO) {
        return deviceApi.unFavorite(reqDTO);
    }
}
