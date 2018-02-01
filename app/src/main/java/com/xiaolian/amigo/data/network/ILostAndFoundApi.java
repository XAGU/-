package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.lostandfound.LostAndFoundDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostAndFoundListReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.SaveLostAndFoundDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostAndFoundListRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;

import rx.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 失物招领
 *
 * @author zcd
 * @date 17/9/18
 */
public interface ILostAndFoundApi {
    /**
     * 获取失物招领列表
     */
    @POST("lost/list")
    Observable<ApiResult<QueryLostAndFoundListRespDTO>> queryLostAndFounds(@Body QueryLostAndFoundListReqDTO reqDTO);

    /**
     * 保存失物招领
     */
    @POST("lost/add")
    Observable<ApiResult<SimpleRespDTO>> saveLostAndFounds(@Body SaveLostAndFoundDTO reqDTO);

    /**
     * 获取失物招领详情
     */
    @POST("lost/one")
    Observable<ApiResult<LostAndFoundDTO>> getLostAndFound(@Body SimpleReqDTO reqDTO);

    /**
     * 我的失物招领
     */
    @POST("lost/personal/list")
    Observable<ApiResult<QueryLostAndFoundListRespDTO>> getMyLostAndFounds();

    /**
     * 更新失物招领
     */
    @POST("lost/update")
    Observable<ApiResult<SimpleRespDTO>> updateLostAndFounds(@Body SaveLostAndFoundDTO reqDTO);

    /**
     * 删除失物招领
     */
    @POST("lost/delete")
    Observable<ApiResult<BooleanRespDTO>> deleteLostAndFounds(@Body SimpleReqDTO reqDTO);

}
