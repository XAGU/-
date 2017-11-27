package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.FavoriteReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.UnFavoriteReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.ScanDeviceRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.UnFavoriteRespDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 收藏设备相关接口
 * <p>
 * Created by caidong on 2017/9/18.
 */
public interface IFavoriteApi {

    // 查询收藏设备列表
    @POST("device/water/favorite/list")
    Observable<ApiResult<ScanDeviceRespDTO>> queryFavorites(@Body FavoriteReqDTO reqDTO);

    // 删除收藏的设备
    @POST("device/unFavorite")
    Observable<ApiResult<UnFavoriteRespDTO>> deleteFavorite(@Body UnFavoriteReqDTO reqDTO);

    // 收藏饮水机
    @POST("device/water/favorite")
    Observable<ApiResult<SimpleRespDTO>> favorite(@Body SimpleReqDTO reqDTO);

    // 取消收藏饮水机
    @POST("device/water/unFavorite")
    Observable<ApiResult<SimpleReqDTO>> unFavorite(@Body SimpleReqDTO reqDTO);
}
