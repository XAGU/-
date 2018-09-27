package com.xiaolian.amigo.ui.lostandfound;

import android.content.Intent;
import android.text.TextUtils;

import com.xiaolian.amigo.data.manager.intf.ILostAndFoundDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.lostandfound.CommonRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LikeItemReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostAndFoundListReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostAndFoundListRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IBlogPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IBlogView;

import javax.inject.Inject;

import static com.xiaolian.amigo.ui.lostandfound.LostAndFoundActivity2.KEY_COMMENT_COUNT;
import static com.xiaolian.amigo.ui.lostandfound.LostAndFoundActivity2.KEY_LIKE;
import static com.xiaolian.amigo.ui.lostandfound.LostAndFoundDetailActivity2.KEY_DELETE;

public class BlogPresenter<V extends IBlogView>  extends BasePresenter<V> implements IBlogPresenter<V> {


    private boolean commentEnable = false;

    int currentHotPosition =  -1 ;

    int currentChoosePosition = - 1 ;


    private ILostAndFoundDataManager lostAndFoundDataManager ;
    @Inject
    BlogPresenter(ILostAndFoundDataManager lostAndFoundDataManager){
        super();
        this.lostAndFoundDataManager = lostAndFoundDataManager ;
    }

    public void setCurrentChoosePosition(int currentChoosePosition) {
        this.currentChoosePosition = currentChoosePosition;
    }

    public void setCurrentHotPosition(int currentHotPosition) {
        this.currentHotPosition = currentHotPosition;
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
                    if (result.getError() == null) {
                        commentEnable = result.getData().getCommentEnable();
                        lostAndFoundDataManager.setCommentEnable(commentEnable);
                            if (page == 1) {
                                if (result.getData().getPosts() == null && result.getData().getHotPosts() == null || (result.getData().getPosts().size() == 0 &&
                                        result.getData().getHotPosts().size() == 0)) {
                                    getMvpView().onEmpty();
                                    return;
                                } else {
                                    if (result.getData().getPosts() != null && result.getData().getPosts().size() != 0) {
                                        getMvpView().referPost(result.getData().getPosts());
                                    } else {
                                        getMvpView().postEmpty();
                                    }

                                    if (result.getData().getHotPosts() != null && result.getData().getHotPosts().size() != 0) {
                                        getMvpView().referHotPost(result.getData().getHotPosts());
                                    } else {
                                        getMvpView().hostPostsEmpty();
                                    }

                                }

                            } else {
                                if (result.getData().getPosts() == null || result.getData().getPosts().size() == 0) {
                                    getMvpView().reducePage();
                                    return;
                                }
                                getMvpView().loadMore(result.getData());
                            }
                        } else {
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
                getMvpView().onErrorView();
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
    public void handleData(Intent data) {
        if (data == null) return ;


        if (data != null && currentHotPosition != -1) {

            boolean isDelete = data.getBooleanExtra(KEY_DELETE, false);
            if (isDelete) {
                getMvpView().removeHotAdapter(currentHotPosition);
            } else {
                getMvpView().notifyHotAdapter(data ,currentHotPosition);
            }
            currentHotPosition = -1;
        }

        if (data != null && currentChoosePosition != -1) {

            boolean isDelete = data.getBooleanExtra(KEY_DELETE, false);
            if (isDelete) {
                getMvpView().removePotItem(currentChoosePosition);
            }else{
                getMvpView().notifyPotAdapter(data ,currentChoosePosition);
            }
            currentChoosePosition = -1;
        }
    }

    private void likeOrUnLikeCommentOrContent(int position, long id, boolean comment, boolean like) {
        LikeItemReqDTO reqDTO = new LikeItemReqDTO();
        reqDTO.setItemId(id);
        // 是否是点赞，1 点赞 2 取消点赞
        reqDTO.setLike(like ? 1 : 2);
        // 被点赞/取消点赞的类型，1 联子 2 评论 3 回复
        reqDTO.setType(1);
        addObserver(lostAndFoundDataManager.like(reqDTO),
                new NetworkObserver<ApiResult<CommonRespDTO>>(false) {

                    @Override
                    public void onReady(ApiResult<CommonRespDTO> result) {
                        if (null == result.getError()) {
                            if (comment) {
                                if (like) {
                                    getMvpView().notifyAdapter(position, true);
                                } else {
                                    getMvpView().notifyAdapter(position, false);
                                }
                            }
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }
                });

    }
}
