package com.xiaolian.amigo.ui.lostandfound;

import com.xiaolian.amigo.data.manager.intf.ILostAndFoundDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.lostandfound.CollectListReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.CommonRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LikeItemReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LostAndFoundDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostAndFoundListRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundAdaptor2;
import com.xiaolian.amigo.ui.lostandfound.intf.IMyCollectPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IMyCollectView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author zcd
 * @date 18/6/21
 */
public class MyCollectPresenter<V extends IMyCollectView> extends BasePresenter<V>
        implements IMyCollectPresenter<V> {
    private ILostAndFoundDataManager lostAndFoundDataManager;
    private int page = Constant.PAGE_START_NUM;
    private static final int size = Constant.PAGE_SIZE;

    @Inject
    public MyCollectPresenter(ILostAndFoundDataManager lostAndFoundDataManager) {
        this.lostAndFoundDataManager = lostAndFoundDataManager;
    }

    @Override
    public void getMyCollects() {
        CollectListReqDTO reqDTO = new CollectListReqDTO();
        reqDTO.setPage(page);
        reqDTO.setSize(size);
        addObserver(lostAndFoundDataManager.getCollects(reqDTO),
                new NetworkObserver<ApiResult<QueryLostAndFoundListRespDTO>>() {

                    @Override
                    public void onReady(ApiResult<QueryLostAndFoundListRespDTO> result) {
                        getMvpView().setRefreshComplete();
                        getMvpView().setLoadMoreComplete();
                        getMvpView().hideEmptyView();
                        getMvpView().hideErrorView();
                        if (null == result.getError()) {
                            if (null != result.getData().getPosts()) {
                                List<LostAndFoundDTO> wrappers = new ArrayList<>();
                                if (wrappers.isEmpty() && page == Constant.PAGE_START_NUM) {
                                    getMvpView().showEmptyView();
                                    return;
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
    public void resetPage() {
        page = Constant.PAGE_START_NUM;
    }



    @Override
    public void unLikeComment(int position, long id) {
        likeOrUnLikeCommentOrContent(position, id, true, false);
    }

    @Override
    public void likeComment(int position, long id) {
        likeOrUnLikeCommentOrContent(position, id, true, true);
    }
}
