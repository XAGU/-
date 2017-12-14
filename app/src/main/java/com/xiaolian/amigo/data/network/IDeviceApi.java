package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.device.QueryWaterListRespDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.SimpleRespDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * device相关api
 * <p>
 * Created by zcd on 17/12/14.
 */

public interface IDeviceApi {
    // 查询收藏设备列表
    @POST("device/water/favorite/list")
    Observable<ApiResult<QueryWaterListRespDTO>> queryFavorites(@Body SimpleQueryReqDTO reqDTO);

    // 收藏饮水机
    @POST("device/water/favorite")
    Observable<ApiResult<SimpleRespDTO>> favorite(@Body SimpleReqDTO reqDTO);

    // 取消收藏饮水机
    @POST("device/water/unFavorite")
    Observable<ApiResult<SimpleRespDTO>> unFavorite(@Body SimpleReqDTO reqDTO);

    // 首页设备用水校验
    @POST("device/check")
    Observable<ApiResult<DeviceCheckRespDTO>> checkDeviceUseage(@Body DeviceCheckReqDTO reqDTO);
}
