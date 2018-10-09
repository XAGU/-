package com.xiaolian.amigo.ui.lostandfound;

import android.support.v4.util.ObjectsCompat;

import com.xiaolian.amigo.data.manager.intf.ILostAndFoundDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.CollectItemReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.CommonRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.DeleteLostFoundItemReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LikeItemReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LostAndFoundDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LostFoundCommentDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LostFoundCommentsListDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostFoundCommentsReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostFoundDetailReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.SaveLostAndFoundReportDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.SaveLostFoundCommentsRepliesDTO;
import com.xiaolian.amigo.data.vo.LostAndFound;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundDetailAdapter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundDetailPresenter2;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundDetailView2;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author zcd
 * @date 18/5/14
 */
public class LostAndFoundDetailPresenter2<V extends ILostAndFoundDetailView2>
    extends BasePresenter<V> implements ILostAndFoundDetailPresenter2<V> {
    private static final String TAG = LostAndFoundDetailPresenter2.class.getSimpleName();

    private ILostAndFoundDataManager lostAndFoundDataManager;
    private int type;
    private LostAndFound lostAndFound;
    private Long id;
    private Integer page = Constant.PAGE_START_NUM;
    private Integer size = 10;
    private Integer replySize = 2;
    /**
     * 原始数量
     */
    private Integer preViewCount;
    private Integer preReplyCount;

    /**
     * 是否开启评论
     */
    private Boolean commentEnable = false;

    @Inject
    public LostAndFoundDetailPresenter2(ILostAndFoundDataManager lostAndFoundDataManager) {
        this.lostAndFoundDataManager = lostAndFoundDataManager;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void getDetail(long id) {
        this.id = id;
        QueryLostFoundDetailReqDTO reqDTO = new QueryLostFoundDetailReqDTO();
        reqDTO.setId(id);
        reqDTO.setCountView(true);
        addObserver(lostAndFoundDataManager.getLostAndFound(reqDTO),
                new NetworkObserver<ApiResult<LostAndFoundDTO>>(false, true) {

                    @Override
                    public void onReady(ApiResult<LostAndFoundDTO> result) {
                        getMvpView().setRefreshComplete();
                        getMvpView().hideEmptyView();
                        if (null == result.getError()) {
                            getMvpView().hideErrorView();
                            getMvpView().showContent();
                            if (result.getData().getCommentEnable() != null
                                    && result.getData().getCommentEnable()) {
                                commentEnable = true;
                                getMvpView().showFootView(ObjectsCompat.equals(result.getData().getCollected(), 1));
                            } else {
                                commentEnable = false;
                                getMvpView().hideFootView();
                            }
                            lostAndFound = result.getData().transform();
                            preViewCount = lostAndFound.getViewCount();
                            preReplyCount = lostAndFound.getCommentsCount();
                            getMvpView().post(() ->
                                    getMvpView().render(result.getData().transform()));
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getMvpView().setRefreshComplete();
                        getMvpView().setLoadMoreComplete();
                        getMvpView().hideEmptyView();
                        getMvpView().hideContent();
                        getMvpView().showErrorView();
                    }
                });
    }

    @Override
    public void refreshDetail() {
        QueryLostFoundDetailReqDTO reqDTO = new QueryLostFoundDetailReqDTO();
        reqDTO.setId(id);
        reqDTO.setCountView(false);
        addObserver(lostAndFoundDataManager.getLostAndFound(reqDTO),
                new NetworkObserver<ApiResult<LostAndFoundDTO>>(false, true) {

                    @Override
                    public void onReady(ApiResult<LostAndFoundDTO> result) {
                        getMvpView().setRefreshComplete();
                        getMvpView().setLoadMoreComplete();
                        getMvpView().hideEmptyView();
                        if (null == result.getError()) {
                            getMvpView().showContent();
                            getMvpView().hideErrorView();
                            if (result.getData().getCommentEnable() != null
                                    && result.getData().getCommentEnable()) {
                                commentEnable = true;
                                getMvpView().showFootView(ObjectsCompat.equals(result.getData().getCollected(), 1));
                            } else {
                                commentEnable = false;
                                getMvpView().hideFootView();
                            }
                            lostAndFound = result.getData().transform();
                            preViewCount = lostAndFound.getViewCount();
                            preReplyCount = lostAndFound.getCommentsCount();
                            getMvpView().post(() ->
                                    getMvpView().render(result.getData().transform()));
                        } else {
                            getMvpView().showErrorView();
                            getMvpView().hideContent();
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getMvpView().setRefreshComplete();
                        getMvpView().setLoadMoreComplete();
                        getMvpView().hideContent();
                        getMvpView().hideEmptyView();
                        getMvpView().showErrorView();
                    }
                });
    }

    @Override
    public LostAndFound getLostAndFound() {
        return this.lostAndFound;
    }

    @Override
    public void getComments() {
        QueryLostFoundCommentsReqDTO reqDTO = new QueryLostFoundCommentsReqDTO();
        reqDTO.setCommentsSize(size);
        reqDTO.setRepliesSize(replySize);
        reqDTO.setFrom((page-1)*size);
        reqDTO.setId(id);
        reqDTO.setHot(page == Constant.PAGE_START_NUM ? 1 : 2);
        addObserver(lostAndFoundDataManager.getCommentList(reqDTO),
                new NetworkObserver<ApiResult<LostFoundCommentsListDTO>>(false, true) {

                    @Override
                    public void onReady(ApiResult<LostFoundCommentsListDTO> result) {
                        getMvpView().setRefreshComplete();
                        getMvpView().setLoadMoreComplete();
                        getMvpView().hideEmptyView();
                        getMvpView().showContent();
                        if (null == result.getError()) {
                            if (null != result.getData()) {
                                if ((result.getData().getCommentsSize() <= 0
                                        && (result.getData().getHot() == null
                                        || result.getData().getHot().isEmpty()))
                                        && page == Constant.PAGE_START_NUM) {
                                    getMvpView().showEmptyView();
                                    return;
                                }
                                List<LostAndFoundDetailAdapter.LostAndFoundDetailWrapper> wrappers = new ArrayList<>();
                                for (LostFoundCommentDTO comment : result.getData().getComments()) {
                                    wrappers.add(new LostAndFoundDetailAdapter.LostAndFoundDetailWrapper(comment,
                                            ObjectsCompat.equals(comment.getUserId(), lostAndFound.getUserId()),
                                            lostAndFound.getUserId(), type, commentEnable));
                                }
                                List<LostAndFoundDetailAdapter.LostAndFoundDetailWrapper> hots = new ArrayList<>();
                                for (LostFoundCommentDTO comment : result.getData().getHot()) {
                                    hots.add(new LostAndFoundDetailAdapter.LostAndFoundDetailWrapper(comment,
                                            ObjectsCompat.equals(comment.getUserId(), lostAndFound.getUserId()),
                                            lostAndFound.getUserId(), type, commentEnable));
                                }
                                getMvpView().hideEmptyView();
                                page ++;
                                getMvpView().post(() ->
                                        getMvpView().addMore(wrappers, hots));
                            }
                        } else {
                            getMvpView().hideErrorView();
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getMvpView().setRefreshComplete();
                        getMvpView().setLoadMoreComplete();
//                        getMvpView().showErrorView();
                        getMvpView().hideContent();
                        getMvpView().hideEmptyView();
                    }
                });
    }

    @Override
    public void resetPage() {
        page = Constant.PAGE_START_NUM;
    }

    @Override
    public void publishComment(String comment) {
        SaveLostFoundCommentsRepliesDTO reqDTO = new SaveLostFoundCommentsRepliesDTO();
        reqDTO.setContent(comment);
        reqDTO.setLostFoundId(id);
        /**
         * 评论1 回复2
         */
        reqDTO.setType(1);
        addObserver(lostAndFoundDataManager.publishCommentOrReply(reqDTO),
                new NetworkObserver<ApiResult<SimpleRespDTO>>() {

                    @Override
                    public void onReady(ApiResult<SimpleRespDTO> result) {
                        if (null == result.getError()) {
                            getMvpView().closePublishDialog();
                            getMvpView().onSuccess("发布成功");
                            getMvpView().onRefresh();
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }
                });
    }

    @Override
    public void publishReply(Long replyToId, Long replyToUserId, String reply) {
        SaveLostFoundCommentsRepliesDTO reqDTO = new SaveLostFoundCommentsRepliesDTO();
        reqDTO.setContent(reply);
        reqDTO.setReplyToId(replyToId);
        reqDTO.setReplyToUserId(replyToUserId);
        reqDTO.setLostFoundId(id);
        /**
         * 评论1 回复2
         */
        reqDTO.setType(2);
        addObserver(lostAndFoundDataManager.publishCommentOrReply(reqDTO),
                new NetworkObserver<ApiResult<SimpleRespDTO>>() {

                    @Override
                    public void onReady(ApiResult<SimpleRespDTO> result) {
                        if (null == result.getError()) {
                            getMvpView().closePublishDialog();
                            getMvpView().onSuccess("发布成功");
                            getMvpView().onRefresh();
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }
                });

    }

    @Override
    public boolean isOwner() {
        return lostAndFound != null
                && ObjectsCompat.equals(lostAndFound.getUserId(), lostAndFoundDataManager.getUserInfo().getId());
    }

    @Override
    public void reportOrDelete() {
        if (isOwner()) {
            DeleteLostFoundItemReqDTO reqDTO = new DeleteLostFoundItemReqDTO();
            reqDTO.setId(id);
            reqDTO.setType(1);
            addObserver(lostAndFoundDataManager.delete(reqDTO),
                    new NetworkObserver<ApiResult<BooleanRespDTO>>() {

                        @Override
                        public void onReady(ApiResult<BooleanRespDTO> result) {
                            if (null == result.getError()) {
                                if (result.getData().isResult()) {
                                    getMvpView().onSuccess("删除成功");
                                    getMvpView().finishView();
                                } else {
                                    getMvpView().onError("删除失败");
                                }
                            } else {
                                getMvpView().onError(result.getError().getDisplayMessage());
                            }
                        }
                    });
        } else {
            SaveLostAndFoundReportDTO reqDTO = new SaveLostAndFoundReportDTO();
            reqDTO.setId(id);
            reqDTO.setType(1);
            addObserver(lostAndFoundDataManager.report(reqDTO),
                    new NetworkObserver<ApiResult<SimpleRespDTO>>(){

                        @Override
                        public void onReady(ApiResult<SimpleRespDTO> result) {
                            if (null == result.getError()) {
                                getMvpView().onSuccess("举报成功");
                            } else {
                                getMvpView().onError(result.getError().getDisplayMessage());
                            }
                        }
                    });
        }
    }

    @Override
    public boolean needRefresh() {
        return lostAndFound != null
                && (!ObjectsCompat.equals(preReplyCount, lostAndFound.getCommentsCount()) || !ObjectsCompat.equals(preViewCount, lostAndFound.getViewCount()));
    }

    @Override
    public boolean isCommentEnable() {
        return commentEnable == null ? false : commentEnable;
    }

    @Override
    public void collect() {
        collectOrUnCollect(1);
    }

    @Override
    public void unCollect() {
        collectOrUnCollect(2);
    }

    private void likeOrUnLikeCommentOrContent(int position, long id, boolean comment, boolean like) {
        LikeItemReqDTO reqDTO = new LikeItemReqDTO();
        reqDTO.setItemId(id);
        // 是否是点赞，1 点赞 2 取消点赞
        reqDTO.setLike(like ? 1 : 2);
        // 被点赞/取消点赞的类型，1 失物招领 2 评论
        reqDTO.setType(comment ? 2 : 1);
        addObserver(lostAndFoundDataManager.like(reqDTO),
                new NetworkObserver<ApiResult<CommonRespDTO>>(false) {

                    @Override
                    public void onReady(ApiResult<CommonRespDTO> result) {
                        if (null == result.getError()) {
                            if (like) {
                                getMvpView().notifyAdapter(position, true);
                            } else {
                                getMvpView().notifyAdapter(position, false);
                            }
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }
                });

    }

    @Override
    public void unLikeComment(int position, long id) {
        likeOrUnLikeCommentOrContent(position, id, true, false);
    }

    @Override
    public void likeComment(int position, long id) {
        likeOrUnLikeCommentOrContent(position, id, true, true);
    }

    @Override
    public void unLikeContent(int position, long id) {

        likeOrUnLikeCommentOrContent(position, id, false, false);
    }

    @Override
    public void likeContent(int position, long id) {
        likeOrUnLikeCommentOrContent(position, id, false, true);
    }

    private void collectOrUnCollect(Integer collect) {
        CollectItemReqDTO reqDTO = new CollectItemReqDTO();
        // 是否是收藏 1 收藏 2 取消收藏
        reqDTO.setCollect(collect);
        reqDTO.setLostFoundId(id);
        addObserver(lostAndFoundDataManager.collect(reqDTO),
                new NetworkObserver<ApiResult<CommonRespDTO>>() {

                    @Override
                    public void onReady(ApiResult<CommonRespDTO> result) {
                        if (null == result.getError()) {
                            if (ObjectsCompat.equals(collect, 1)) {
                                getMvpView().collectSuccess();
                            } else {
                                getMvpView().unCollectSuccess();
                            }
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }
                });
    }

}
