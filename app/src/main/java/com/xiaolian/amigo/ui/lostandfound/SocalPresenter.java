package com.xiaolian.amigo.ui.lostandfound;

import android.text.TextUtils;

import com.xiaolian.amigo.data.manager.intf.ILostAndFoundDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.lostandfound.BbsTopicListTradeRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.CommonRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LikeItemReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.NoticeCountDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostAndFoundListReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostAndFoundListRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostFoundDetailReqDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ISocalPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ISocalView;

import javax.inject.Inject;

public class SocalPresenter <V extends ISocalView> extends BasePresenter<V>
        implements ISocalPresenter<V> {

    private ILostAndFoundDataManager lostAndFoundDataManager ;


    private boolean commentEnable = false;

    private int noticeCount = 0;

    @Inject
    SocalPresenter(ILostAndFoundDataManager lostAndFoundDataManager) {
        super();
        this.lostAndFoundDataManager = lostAndFoundDataManager;
    }


    @Override
    public void getTopicList() {
        addObserver(lostAndFoundDataManager.getTopicList(), new NetworkObserver<ApiResult<BbsTopicListTradeRespDTO>>(){
            @Override
            public void onStart() {
            }

            @Override
            public void onReady(ApiResult<BbsTopicListTradeRespDTO> result) {
                if (result.getError() == null){
                    getMvpView().setReferComplete();
                    if (result.getData().getTopicList() != null && result.getData().getTopicList().size() > 0) {
                        lostAndFoundDataManager.setTopic(result.getData().getTopicList());
                        getMvpView().referTopic(result.getData());
                    }

                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void getLostList(String hotPosIds , int page , String selectKey , int topicId) {
        QueryLostAndFoundListReqDTO reqDTO = new QueryLostAndFoundListReqDTO();
        if (!TextUtils.isEmpty(hotPosIds)){
            reqDTO.setHotPostIds(hotPosIds);
        }
        if (page != 0){
            reqDTO.setPage(page);
        }

        if (!TextUtils.isEmpty(selectKey)){
            reqDTO.setSelectKey(selectKey);
        }

        if (topicId != 0){
            reqDTO.setTopicId((long) topicId);
        }

        addObserver(lostAndFoundDataManager.queryLostAndFounds(reqDTO) , new NetworkObserver<ApiResult<QueryLostAndFoundListRespDTO>>(){

            @Override
            public void onStart() {

            }

            @Override
            public void onReady(ApiResult<QueryLostAndFoundListRespDTO> result) {
                getMvpView().setReferComplete();
                if (result.getError() == null){
                    if (page == 1) {
                        if (result.getData().getPosts() == null && result.getData().getHotPosts()== null ||( result.getData().getPosts().size() == 0 &&
                                result.getData().getHotPosts().size() == 0)){
                            getMvpView().onEmpty();
                        }else {
                            getMvpView().referTopicList(result.getData());
                        }
                    }else{
                        getMvpView().loadMore(result.getData());
                    }
                }else{
                    getMvpView().reducePage();
                    getMvpView().onError(result.getError().getDisplayMessage());
                    getMvpView().onErrorView();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().setReferComplete();
                getMvpView().reducePage();
            }
        });

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
    public boolean isCommentEnable() {
        return commentEnable;
    }

    @Override
    public int getNoticeCount() {
        return noticeCount;
    }


    @Override
    public void fetchNoticeCount() {
        addObserver(lostAndFoundDataManager.noticeCount(),
                new NetworkObserver<ApiResult<NoticeCountDTO>>(false) {

                    @Override
                    public void onReady(ApiResult<NoticeCountDTO> result) {
                        if (null == result.getError()) {
                            noticeCount = result.getData().getNoticeCount();
                            if (noticeCount != 0) {
                                getMvpView().showNoticeRemind(noticeCount);
                            } else {
                                getMvpView().hideNoticeRemind();
                            }
                        }
                    }
                });
    }
}
