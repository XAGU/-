package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IFavoriteManager;
import com.xiaolian.amigo.data.network.IFavoriteApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.FavoriteReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.UnFavoriteReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.ScanDeviceRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.UnFavoriteRespDTO;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.http.Body;
import rx.Observable;

/**
 * favorite manager
 * Created by caidong on 2017/9/18.
 */
public class FavoriteManager implements IFavoriteManager {

    private static final String TAG = FavoriteManager.class.getSimpleName();

    private IFavoriteApi favoriteApi;

    @Inject
    public FavoriteManager(Retrofit retrofit) {
        favoriteApi = retrofit.create(IFavoriteApi.class);
    }

    @Override
    public Observable<ApiResult<ScanDeviceRespDTO>> queryFavorites(@Body FavoriteReqDTO reqDTO) {
        return favoriteApi.queryFavorites(reqDTO);
    }

    @Override
    public Observable<ApiResult<UnFavoriteRespDTO>> deleteFavorite(@Body UnFavoriteReqDTO reqDTO) {
        return favoriteApi.deleteFavorite(reqDTO);
    }

    @Override
    public Observable<ApiResult<SimpleRespDTO>> favorite(@Body SimpleReqDTO reqDTO) {
        return favoriteApi.favorite(reqDTO);
    }

    @Override
    public Observable<ApiResult<SimpleReqDTO>> unFavorite(@Body SimpleReqDTO reqDTO) {
        return favoriteApi.unFavorite(reqDTO);
    }
}
