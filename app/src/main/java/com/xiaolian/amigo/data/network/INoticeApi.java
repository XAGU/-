package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.QueryNotifyListReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryNotifyListRespDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 通知相关Api
 * <p>
 * Created by zcd on 9/22/17.
 */

public interface INoticeApi {
    // 通知公告列表
    @POST("/notify/list")
    Observable<ApiResult<QueryNotifyListRespDTO>> queryNotifyList(@Body QueryNotifyListReqDTO dto);
}