package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.notify.NotifyDTO;
import com.xiaolian.amigo.data.network.model.notify.QueryNotifyListReqDTO;
import com.xiaolian.amigo.data.network.model.notify.ReadNotifyReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.notify.QueryNotifyListRespDTO;
import com.xiaolian.amigo.data.network.model.notify.RollingNotifyRespDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 通知相关Api
 *
 * @author zcd
 * @date 17/9/22
 */

public interface INotifyApi {
    /**
     * 通知公告列表
     */
    @POST("notify/list")
    Observable<ApiResult<QueryNotifyListRespDTO>> queryNotifyList(@Body QueryNotifyListReqDTO dto);

    /**
     * 告诉服务端通知已读（紧急公告）
     */
    @POST("notify/read")
    Observable<ApiResult<BooleanRespDTO>> readUrgentNotify(@Body ReadNotifyReqDTO reqDTO);

    /**
     * 通知公共详情
     */
    @POST("notify/one")
    Observable<ApiResult<NotifyDTO>> getNotice(@Body SimpleReqDTO reqDTO);

    /**
     * 滚动公告
     */
    @POST("notify/rolling/list")
    Observable<ApiResult<RollingNotifyRespDTO>> rollingList();

}
