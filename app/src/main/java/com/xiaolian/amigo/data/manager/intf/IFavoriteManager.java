package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.device.QueryWaterListRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;

import retrofit2.http.Body;
import rx.Observable;

/**
 * Created by caidong on 2017/9/18.
 */
public interface IFavoriteManager {
    // 查询收藏设备列表
    Observable<ApiResult<QueryWaterListRespDTO>> queryFavorites(@Body SimpleQueryReqDTO reqDTO);

    // 收藏饮水机
    Observable<ApiResult<SimpleRespDTO>> favorite(@Body SimpleReqDTO reqDTO);

    // 取消收藏饮水机
    Observable<ApiResult<SimpleRespDTO>> unFavorite(@Body SimpleReqDTO reqDTO);
}
