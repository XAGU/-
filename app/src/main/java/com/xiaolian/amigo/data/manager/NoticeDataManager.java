package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.INoticeDataManager;
import com.xiaolian.amigo.data.network.INotifyApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.notify.QueryNotifyListReqDTO;
import com.xiaolian.amigo.data.network.model.notify.ReadNotifyReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.notify.QueryNotifyListRespDTO;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.http.Body;
import rx.Observable;

/**
 * 通知中心
 * <p>
 * Created by zcd on 9/22/17.
 */

public class NoticeDataManager implements INoticeDataManager {
    @SuppressWarnings("unused")
    private static final String TAG = NoticeDataManager.class.getSimpleName();

    private INotifyApi notifyApi;

    @Inject
    public NoticeDataManager(Retrofit retrofit) {
        notifyApi = retrofit.create(INotifyApi.class);
    }

    @Override
    public Observable<ApiResult<QueryNotifyListRespDTO>> queryNotifyList(@Body QueryNotifyListReqDTO dto) {
        return notifyApi.queryNotifyList(dto);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> readUrgentNotify(@Body ReadNotifyReqDTO reqDTO) {
        return notifyApi.readUrgentNotify(reqDTO);
    }
}
