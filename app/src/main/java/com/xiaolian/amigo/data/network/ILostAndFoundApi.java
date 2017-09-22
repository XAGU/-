package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.QueryLostAndFoundListReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SaveLostAndFoundDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryLostAndFoundListRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LostAndFound;

import rx.Observable;
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

    // 保存失物招领
    @POST("/lost/add")
    Observable<ApiResult<SimpleRespDTO>> saveLostAndFounds(@Body SaveLostAndFoundDTO reqDTO);

    // 获取失物招领详情
    @POST("/lost/one")
    Observable<ApiResult<LostAndFound>> getLostAndFound(@Body SimpleReqDTO reqDTO);

    // 我的失物招领
    @POST("/lost/personal/list")
    Observable<ApiResult<QueryLostAndFoundListRespDTO>> getMyLostAndFounds();

    // 更新失物招领
    @POST("/lost/update")
    Observable<ApiResult<SimpleRespDTO>> updateLostAndFounds(@Body SaveLostAndFoundDTO reqDTO);

}
