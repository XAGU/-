package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.device.BriefDeviceDTO;
import com.xiaolian.amigo.data.network.model.device.FavorDeviceReqDTO;
import com.xiaolian.amigo.data.network.model.device.GetDeviceDetailReqDTO;
import com.xiaolian.amigo.data.network.model.device.QueryDeviceListReqDTO;
import com.xiaolian.amigo.data.network.model.device.QueryDeviceListRespDTO;
import com.xiaolian.amigo.data.network.model.device.QueryFavorDeviceRespDTO;
import com.xiaolian.amigo.data.network.model.device.QueryWaterListRespDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * device相关api
 *
 * @author zcd
 * @date 17/12/14
 */

public interface IDeviceApi {
    /**
     * 获取个人收藏的设备列表
     */
    @POST("device/favorite/list")
    Observable<ApiResult<QueryFavorDeviceRespDTO>> getFavorites(@Body QueryDeviceListReqDTO reqDTO);

    /**
     * 收藏饮水机
     */
    @POST("device/favorite")
    Observable<ApiResult<SimpleRespDTO>> favorite(@Body FavorDeviceReqDTO reqDTO);

    /**
     * 取消收藏饮水机
     */
    @POST("device/unFavorite")
    Observable<ApiResult<SimpleRespDTO>> unFavorite(@Body FavorDeviceReqDTO reqDTO);

    /**
     * 首页设备用水校验
     */
    @POST("device/bath/check")
    Observable<ApiResult<DeviceCheckRespDTO>> checkDeviceUseage(@Body DeviceCheckReqDTO reqDTO);

    /**
     * 设备列表
     */
    @POST("device/list")
    Observable<ApiResult<QueryDeviceListRespDTO>> getDeviceList(@Body QueryDeviceListReqDTO reqDTO);

    /**
     * 设备详情 ， 扫一扫中使用
     */
    @POST("device/one")
    Observable<ApiResult<BriefDeviceDTO>>  getDeviceDetail(@Body GetDeviceDetailReqDTO reqDTO);

}
