package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.notify.QueryNotifyListReqDTO;
import com.xiaolian.amigo.data.network.model.notify.ReadNotifyReqDTO;
import com.xiaolian.amigo.data.network.model.notify.QueryNotifyListRespDTO;

import retrofit2.http.Body;
import rx.Observable;

/**
 * 通知中心
 * <p>
 * Created by zcd on 9/22/17.
 */

public interface INoticeDataManager {

    // 通知公告列表
    Observable<ApiResult<QueryNotifyListRespDTO>> queryNotifyList(@Body QueryNotifyListReqDTO dto);

    // 告诉服务端通知已读（紧急公告）
    Observable<ApiResult<BooleanRespDTO>> readUrgentNotify(@Body ReadNotifyReqDTO reqDTO);
}
