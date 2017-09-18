package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.ILostAndFoundDataManager;
import com.xiaolian.amigo.data.network.ILostAndFoundApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.QueryLostAndFoundListReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryLostAndFoundListRespDTO;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.http.Body;

/**
 * 失物招领
 * <p>
 * Created by zcd on 9/18/17.
 */

public class LostAndFoundDataManager implements ILostAndFoundDataManager {

    ILostAndFoundApi lostAndFoundApi;

    @Inject
    public LostAndFoundDataManager(Retrofit retrofit) {
        lostAndFoundApi = retrofit.create(ILostAndFoundApi.class);
    }

    @Override
    public Observable<ApiResult<QueryLostAndFoundListRespDTO>> queryLostAndFounds(@Body QueryLostAndFoundListReqDTO reqDTO) {
        return lostAndFoundApi.queryLostAndFounds(reqDTO);
    }
}
