package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.ILostAndFoundDataManager;
import com.xiaolian.amigo.data.network.ILostAndFoundApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.DeleteLostFoundItemReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LostAndFoundDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LostFoundCommentsListDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LostFoundRepliesListDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostAndFoundListReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostAndFoundListRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostFoundCommentsReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostFoundDetailReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostFoundRepliesReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.SaveLostAndFoundDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.SaveLostAndFoundReportDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.SaveLostFoundCommentsRepliesDTO;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.http.Body;
import rx.Observable;

/**
 * 失物招领
 *
 * @author zcd
 * @date 17/9/18
 */

public class LostAndFoundDataManager implements ILostAndFoundDataManager {

    private ILostAndFoundApi lostAndFoundApi;
    private ISharedPreferencesHelp sharedPreferencesHelp;

    @Inject
    public LostAndFoundDataManager(Retrofit retrofit, ISharedPreferencesHelp sharedPreferencesHelp) {
        lostAndFoundApi = retrofit.create(ILostAndFoundApi.class);
        this.sharedPreferencesHelp = sharedPreferencesHelp;
    }

    @Override
    public Observable<ApiResult<QueryLostAndFoundListRespDTO>> queryLostAndFounds(@Body QueryLostAndFoundListReqDTO reqDTO) {
        return lostAndFoundApi.queryLostAndFounds(reqDTO);
    }

    @Override
    public Observable<ApiResult<SimpleRespDTO>> saveLostAndFounds(@Body SaveLostAndFoundDTO reqDTO) {
        return lostAndFoundApi.saveLostAndFounds(reqDTO);
    }

    @Override
    public Observable<ApiResult<LostAndFoundDTO>> getLostAndFound(@Body QueryLostFoundDetailReqDTO reqDTO) {
        return lostAndFoundApi.getLostAndFound(reqDTO);
    }

    @Override
    public Observable<ApiResult<QueryLostAndFoundListRespDTO>> getMyLostAndFounds() {
        return lostAndFoundApi.getMyLostAndFounds();
    }

    @Override
    public Observable<ApiResult<SimpleRespDTO>> updateLostAndFounds(@Body SaveLostAndFoundDTO reqDTO) {
        return lostAndFoundApi.updateLostAndFounds(reqDTO);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> deleteLostAndFounds(SimpleReqDTO reqDTO) {
        return lostAndFoundApi.deleteLostAndFounds(reqDTO);
    }

    @Override
    public Observable<ApiResult<LostFoundCommentsListDTO>> getCommentList(QueryLostFoundCommentsReqDTO reqDTO) {
        return lostAndFoundApi.getCommentList(reqDTO);
    }

    @Override
    public Observable<ApiResult<SimpleRespDTO>> publishCommentOrReply(SaveLostFoundCommentsRepliesDTO reqDTO) {
        return lostAndFoundApi.publishCommentOrReply(reqDTO);
    }

    @Override
    public Observable<ApiResult<LostFoundRepliesListDTO>> getReplies(QueryLostFoundRepliesReqDTO reqDTO) {
        return lostAndFoundApi.getReplies(reqDTO);
    }

    @Override
    public Observable<ApiResult<SimpleRespDTO>> report(SaveLostAndFoundReportDTO reqDTO) {
        return lostAndFoundApi.report(reqDTO);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> delete(DeleteLostFoundItemReqDTO reqDTO) {
        return lostAndFoundApi.delete(reqDTO);
    }

    @Override
    public User getUserInfo() {
        return sharedPreferencesHelp.getUserInfo();
    }
}
