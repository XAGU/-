package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.QueryLostAndFoundListReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryLostAndFoundListRespDTO;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 失物招领
 * <p>
 * Created by zcd on 9/18/17.
 */
public interface ILostAndFoundApi {
    // 获取失物招领列表
    @POST("/lost/list")
    Observable<ApiResult<QueryLostAndFoundListRespDTO>> queryLostAndFounds(@Body QueryLostAndFoundListReqDTO reqDTO);
}
