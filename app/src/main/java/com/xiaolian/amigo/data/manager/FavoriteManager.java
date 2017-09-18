package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IFavoriteManager;
import com.xiaolian.amigo.data.network.IFavoriteApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.FavoriteReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.FavoriteRespDTO;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.http.Body;

/**
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
    public Observable<ApiResult<FavoriteRespDTO>> queryFavorites(@Body FavoriteReqDTO reqDTO) {
        return favoriteApi.queryFavorites(reqDTO);
    }
}
