package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.BbsTopicListTradeRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.CollectItemReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.CollectListReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.CommonRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.DeleteLostFoundItemReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LikeItemReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LostAndFoundDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LostFoundCommentsListDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LostFoundRepliesListDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.NoticeCountDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.NoticeListDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.NoticeListReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostAndFoundListReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostAndFoundListRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostFoundCommentsReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostFoundDetailReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostFoundRepliesReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.SaveLostAndFoundDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.SaveLostAndFoundReportDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.SaveLostFoundCommentsRepliesDTO;
import com.xiaolian.amigo.data.vo.User;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 失物招领
 *
 * @author zcd
 * @date 17/9/18
 */

public interface ILostAndFoundDataManager {

    User getUserInfo();

    /**
     * 获取失物招领列表
     */
    Observable<ApiResult<QueryLostAndFoundListRespDTO>> queryLostAndFounds(@Body QueryLostAndFoundListReqDTO reqDTO);

    /**
     * 保存失物招领
     */
    Observable<ApiResult<SimpleRespDTO>> saveLostAndFounds(@Body SaveLostAndFoundDTO reqDTO);

    /**
     * 获取失物招领详情
     */
    Observable<ApiResult<LostAndFoundDTO>> getLostAndFound(@Body QueryLostFoundDetailReqDTO reqDTO);

    /**
     * 我的失物招领
     */
    Observable<ApiResult<QueryLostAndFoundListRespDTO>> getMyLostAndFounds();

    /**
     * 更新失物招领
     */
    Observable<ApiResult<SimpleRespDTO>> updateLostAndFounds(@Body SaveLostAndFoundDTO reqDTO);

    /**
     * 删除失物招领
     */
    Observable<ApiResult<BooleanRespDTO>> deleteLostAndFounds(@Body SimpleReqDTO reqDTO);

    /**
     * 获取评论
     */
    Observable<ApiResult<LostFoundCommentsListDTO>> getCommentList(@Body QueryLostFoundCommentsReqDTO reqDTO);

    /**
     * 发布评论/回复
     */
    Observable<ApiResult<SimpleRespDTO>> publishCommentOrReply(@Body SaveLostFoundCommentsRepliesDTO reqDTO);

    /**
     * 失物招领回复列表
     */
    Observable<ApiResult<LostFoundRepliesListDTO>> getReplies(@Body QueryLostFoundRepliesReqDTO reqDTO);

    /**
     * 举报
     */
    Observable<ApiResult<SimpleRespDTO>> report(@Body SaveLostAndFoundReportDTO reqDTO);

    /**
     * 删除失物招领／评论／回复
     */
    Observable<ApiResult<BooleanRespDTO>> delete(@Body DeleteLostFoundItemReqDTO reqDTO);

    /**
     * 收藏/取消收藏
     */
    Observable<ApiResult<CommonRespDTO>> collect(@Body CollectItemReqDTO reqDTO);

    /**
     * 收藏列表
     */
    Observable<ApiResult<QueryLostAndFoundListRespDTO>> getCollects(@Body CollectListReqDTO reqDTO);

    /**
     * 点赞
     */
    Observable<ApiResult<CommonRespDTO>> like(@Body LikeItemReqDTO reqDTO);

    /**
     * 通知数量
     */
    Observable<ApiResult<NoticeCountDTO>> noticeCount();

    /**
     * 通知列表
     */
    Observable<ApiResult<NoticeListDTO>> getNoticeList(@Body NoticeListReqDTO reqDTO);


    /**
     * 获取当前学校已开启的话题列表
     */
    Observable<ApiResult<BbsTopicListTradeRespDTO>> getTopicList();


    void setTopic(List<BbsTopicListTradeRespDTO.TopicListBean> topicListBeans);


    List<BbsTopicListTradeRespDTO.TopicListBean> getTopic();


    void setCommentEnable(boolean commentEnable);

    boolean getIsFirstAfterLogin();

    void setIsFirstAfterLogin(boolean b);
}
