package com.xiaolian.amigo.ui.lostandfound;

import android.support.v4.util.ObjectsCompat;

import com.xiaolian.amigo.data.manager.intf.ILostAndFoundDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.DeleteLostFoundItemReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LostFoundRepliesListDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LostFoundReplyDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostFoundRepliesReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.SaveLostAndFoundReportDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.SaveLostFoundCommentsRepliesDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundReplyDetailAdapter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundReplyDetailPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundReplyDetailView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author zcd
 * @date 18/5/16
 */
public class LostAndFoundReplyDetailPresenter<V extends ILostAndFoundReplyDetailView>
    extends BasePresenter<V> implements ILostAndFoundReplyDetailPresenter<V> {
    private ILostAndFoundDataManager lostAndFoundManager;
    private Long commentId;
    private Integer page = Constant.PAGE_START_NUM;
    private static final int size = 20;
    private Long ownerId;
    private Long lostFoundId;

    @Inject
    public LostAndFoundReplyDetailPresenter(ILostAndFoundDataManager lostAndFoundManager) {
        this.lostAndFoundManager = lostAndFoundManager;
    }

    @Override
    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    @Override
    public void getReplies() {
        QueryLostFoundRepliesReqDTO reqDTO = new QueryLostFoundRepliesReqDTO();
        reqDTO.setFrom((page-1)*size);
        reqDTO.setId(commentId);
        reqDTO.setSize(size);
        addObserver(lostAndFoundManager.getReplies(reqDTO),
                new NetworkObserver<ApiResult<LostFoundRepliesListDTO>>(false, true) {

                    @Override
                    public void onReady(ApiResult<LostFoundRepliesListDTO> result) {
                        getMvpView().setRefreshComplete();
                        getMvpView().setLoadMoreComplete();
                        getMvpView().hideEmptyView();
                        getMvpView().hideErrorView();
                        if (null == result.getError()) {
                            if (null != result.getData()) {
                                if (result.getData().getSize() <= 0 && page == Constant.PAGE_START_NUM) {
                                    getMvpView().showEmptyView();
                                    return;
                                }
                                List<LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper> wrappers = new ArrayList<>();
                                for (LostFoundReplyDTO reply : result.getData().getReplies()) {
                                    wrappers.add(
                                            new LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper(
                                                    reply,
                                            ObjectsCompat.equals(reply.getUserId(),
                                                    ownerId)));
                                }
                                getMvpView().hideEmptyView();
                                page ++;
                                getMvpView().addMore(wrappers);
                            }
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getMvpView().setRefreshComplete();
                        getMvpView().setLoadMoreComplete();
                        getMvpView().showErrorView();
                    }
                });

    }

    @Override
    public void resetPage() {
        page = Constant.PAGE_START_NUM;
    }

    @Override
    public boolean isOwner() {
        return ObjectsCompat.equals(ownerId, lostAndFoundManager.getUserInfo().getId());
    }

    @Override
    public void publishReply(Long replyToId, Long replyToUserId, String reply) {
        SaveLostFoundCommentsRepliesDTO reqDTO = new SaveLostFoundCommentsRepliesDTO();
        reqDTO.setContent(reply);
        reqDTO.setReplyToId(replyToId);
        reqDTO.setReplyToUserId(replyToUserId);
        reqDTO.setLostFoundId(lostFoundId);
        /**
         * 评论1 回复2
         */
        reqDTO.setType(2);
        addObserver(lostAndFoundManager.publishCommentOrReply(reqDTO),
                new NetworkObserver<ApiResult<SimpleRespDTO>>(false, true) {

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
    public void setLostFoundId(Long lostFoundId) {
        this.lostFoundId = lostFoundId;
    }

    @Override
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public void reportOrDelete() {
        if (isOwner()) {
            DeleteLostFoundItemReqDTO reqDTO = new DeleteLostFoundItemReqDTO();
            reqDTO.setId(commentId);
            reqDTO.setType(3);
            addObserver(lostAndFoundManager.delete(reqDTO),
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
            reqDTO.setId(commentId);
            reqDTO.setType(3);
            addObserver(lostAndFoundManager.report(reqDTO),
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
    public Long getUserId() {
        return lostAndFoundManager.getUserInfo().getId();
    }

    @Override
    public void deleteReply(Long id) {
        DeleteLostFoundItemReqDTO reqDTO = new DeleteLostFoundItemReqDTO();
        reqDTO.setId(id);
        reqDTO.setType(4);
        addObserver(lostAndFoundManager.delete(reqDTO),
                new NetworkObserver<ApiResult<BooleanRespDTO>>() {

                    @Override
                    public void onReady(ApiResult<BooleanRespDTO> result) {
                        if (null == result.getError()) {
                            if (result.getData().isResult()) {
                                getMvpView().onSuccess("删除成功");
                                getMvpView().onRefresh();
                            } else {
                                getMvpView().onError("删除失败");
                            }
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }
                });
    }
}
