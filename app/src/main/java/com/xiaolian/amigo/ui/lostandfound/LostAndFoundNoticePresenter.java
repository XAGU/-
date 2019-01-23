package com.xiaolian.amigo.ui.lostandfound;

import com.xiaolian.amigo.data.manager.intf.ILostAndFoundDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LostFoundNoticeDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.NoticeListDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.NoticeListReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.SaveLostFoundCommentsRepliesDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundNoticeAdapter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundNoticePresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundNoticeView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author zcd
 * @date 18/6/13
 */
public class LostAndFoundNoticePresenter<V extends ILostAndFoundNoticeView> extends BasePresenter<V>
        implements ILostAndFoundNoticePresenter<V> {
    private ILostAndFoundDataManager lostAndFoundDataManager;
    private LostAndFoundNoticeAdapter.ItemType currentItemType =
            LostAndFoundNoticeAdapter.ItemType.REPLY;
    private int replyPage = Constant.PAGE_START_NUM;
    private int likePage = Constant.PAGE_START_NUM;
    private static final int size = Constant.PAGE_SIZE;

    @Inject
    public LostAndFoundNoticePresenter(ILostAndFoundDataManager lostAndFoundDataManager) {
        this.lostAndFoundDataManager = lostAndFoundDataManager;
    }

    @Override
    public void changeItemTo(LostAndFoundNoticeAdapter.ItemType itemType) {
        this.currentItemType = itemType;
    }

    @Override
    public void getList() {
        if (currentItemType == LostAndFoundNoticeAdapter.ItemType.REPLY) {
            getReplyList();
        } else {
            getLikeList();
        }
    }

    private void getReplyList() {
        NoticeListReqDTO reqDTO = new NoticeListReqDTO();
        reqDTO.setPage(replyPage);
        reqDTO.setSize(size);
        // 通知内容类型 1 回复 2 点赞
        reqDTO.setType(1);
        addObserver(lostAndFoundDataManager.getNoticeList(reqDTO),
                new NetworkObserver<ApiResult<NoticeListDTO>>(true) {

                    @Override
                    public void onReady(ApiResult<NoticeListDTO> result) {
                        getMvpView().setRefreshComplete();
                        getMvpView().setLoadMoreComplete();
                        getMvpView().hideEmptyView();
                        getMvpView().hideErrorView();
                        if (null == result.getError()) {
                            if (null != result.getData().getList()) {
                                List<LostAndFoundNoticeAdapter.NoticeWrapper> wrappers = new ArrayList<>();
                                for (LostFoundNoticeDTO notice : result.getData().getList()) {
                                    wrappers.add(new LostAndFoundNoticeAdapter.NoticeWrapper(notice,
                                            LostAndFoundNoticeAdapter.ItemType.REPLY));
                                }
                                if (wrappers.isEmpty() && replyPage == Constant.PAGE_START_NUM) {
                                    getMvpView().showEmptyView();
                                    return;
                                }
                                getMvpView().hideEmptyView();
                                replyPage ++;
                                getMvpView().addMoreReply(wrappers);
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

    private void getLikeList() {
        NoticeListReqDTO reqDTO = new NoticeListReqDTO();
        reqDTO.setPage(likePage);
        reqDTO.setSize(size);
        // 通知内容类型 1 回复 2 点赞
        reqDTO.setType(2);
        addObserver(lostAndFoundDataManager.getNoticeList(reqDTO),
                new NetworkObserver<ApiResult<NoticeListDTO>>(false) {

                    @Override
                    public void onReady(ApiResult<NoticeListDTO> result) {
                        getMvpView().setRefreshComplete();
                        getMvpView().setLoadMoreComplete();
                        getMvpView().hideEmptyView();
                        getMvpView().hideErrorView();
                        if (null == result.getError()) {
                            if (null != result.getData().getList()) {
                                List<LostAndFoundNoticeAdapter.NoticeWrapper> wrappers = new ArrayList<>();
                                for (LostFoundNoticeDTO notice : result.getData().getList()) {
                                    wrappers.add(new LostAndFoundNoticeAdapter.NoticeWrapper(notice,
                                            LostAndFoundNoticeAdapter.ItemType.LIKE));
                                }
                                if (wrappers.isEmpty() && likePage == Constant.PAGE_START_NUM) {
                                    getMvpView().showEmptyView();
                                    return;
                                }
                                getMvpView().hideEmptyView();
                                likePage ++;
                                getMvpView().addMoreLike(wrappers);
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
    public void publishReply(Long lostFoundId, Long replyToId, Long replyToUserId, String reply) {
        SaveLostFoundCommentsRepliesDTO reqDTO = new SaveLostFoundCommentsRepliesDTO();
        reqDTO.setContent(reply);
        reqDTO.setReplyToId(replyToId);
        reqDTO.setReplyToUserId(replyToUserId);
        reqDTO.setLostFoundId(lostFoundId);
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
                            getMvpView().onSuccess("回复成功");
//                            getMvpView().onRefresh();
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }
                });

    }

    @Override
    public void resetPage() {
        if (currentItemType == LostAndFoundNoticeAdapter.ItemType.REPLY) {
            replyPage = Constant.PAGE_START_NUM;
        } else {
            likePage = Constant.PAGE_START_NUM;
        }
    }
}
