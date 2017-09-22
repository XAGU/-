package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.INoticeDataManager;
import com.xiaolian.amigo.data.network.INoticeApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.QueryNotifyListReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryNotifyListRespDTO;

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
    private static final String TAG = NoticeDataManager.class.getSimpleName();

    private INoticeApi noticeApi;

    @Inject
    public NoticeDataManager(Retrofit retrofit) {
        noticeApi = retrofit.create(INoticeApi.class);
    }

    @Override
    public Observable<ApiResult<QueryNotifyListRespDTO>> queryNotifyList(@Body QueryNotifyListReqDTO dto) {
        return noticeApi.queryNotifyList(dto);
    }
}
