package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.FavoriteReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.OrderReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.UnFavoriteReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.FavoriteRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.OrderRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.UnFavoriteRespDTO;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 收藏设备相关接口
 * <p>
 * Created by caidong on 2017/9/18.
 */
public interface IFavoriteApi {

    // 查询收藏设备列表
    @POST("/device/personal/list")
    Observable<ApiResult<FavoriteRespDTO>> queryFavorites(@Body FavoriteReqDTO reqDTO);

    // 删除收藏的设备
    @POST("/device/unFavorite")
    Observable<ApiResult<UnFavoriteRespDTO>> deleteFavorite(@Body UnFavoriteReqDTO reqDTO);
}
