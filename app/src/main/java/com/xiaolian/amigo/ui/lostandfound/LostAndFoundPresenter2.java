package com.xiaolian.amigo.ui.lostandfound;

import com.xiaolian.amigo.data.manager.intf.ILostAndFoundDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.lostandfound.LostAndFoundDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.NoticeCountDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostAndFoundListReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostAndFoundListRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundAdaptor2;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundPresenter2;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundView2;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author zcd
 * @date 18/5/12
 */
public class LostAndFoundPresenter2<V extends ILostAndFoundView2> extends BasePresenter<V>
    implements ILostAndFoundPresenter2<V> {
    private ILostAndFoundDataManager lostAndFoundDataManager;
    private int page = Constant.PAGE_START_NUM;
    private static final int size = Constant.PAGE_SIZE;
    private boolean commentEnable = false;

    private int noticeCount = 0;

    @Inject
    public LostAndFoundPresenter2(ILostAndFoundDataManager lostAndFoundDataManager) {
        this.lostAndFoundDataManager = lostAndFoundDataManager;
    }

    @Override
    public void getList(boolean isSearch, String searchStr) {
        QueryLostAndFoundListReqDTO reqDTO = new QueryLostAndFoundListReqDTO();
        if (isSearch) {
            reqDTO.setSelectKey(searchStr);
            reqDTO.setPage(Constant.PAGE_START_NUM);
            reqDTO.setSize(100);
        } else {
            reqDTO.setPage(page);
            reqDTO.setSize(size);
        }
        reqDTO.setSchoolId(lostAndFoundDataManager.getUserInfo().getSchoolId());
        addObserver(lostAndFoundDataManager.queryLostAndFounds(reqDTO),
                new NetworkObserver<ApiResult<QueryLostAndFoundListRespDTO>>(false, true) {

                    @Override
                    public void onReady(ApiResult<QueryLostAndFoundListRespDTO> result) {
                        getMvpView().setRefreshComplete();
                        getMvpView().setLoadMoreComplete();
                        getMvpView().hideEmptyView();
                        getMvpView().hideErrorView();
                        if (null == result.getError()) {
                            if (result.getData().getCommentEnable() != null
                                    && result.getData().getCommentEnable()) {
                                commentEnable = true;
                                getMvpView().showFootView();
                            } else {
                                commentEnable = false;
                                getMvpView().hideFootView();
                            }
                            if (null != result.getData().getLostAndFounds()) {
                                List<LostAndFoundAdaptor2.LostAndFoundWrapper> wrappers = new ArrayList<>();
                                for (LostAndFoundDTO lost : result.getData().getLostAndFounds()) {
                                    wrappers.add(new LostAndFoundAdaptor2.LostAndFoundWrapper(lost.transform()));
                                }
                                if (isSearch) {
                                    if (wrappers.isEmpty()) {
                                        getMvpView().showNoSearchResult(searchStr);
                                    } else {
                                        getMvpView().showSearchResult(wrappers);
                                    }
                                } else {
                                    if (wrappers.isEmpty() && page == Constant.PAGE_START_NUM) {
                                        getMvpView().showEmptyView();
                                        return;
                                    }
                                    getMvpView().hideEmptyView();
                                    page ++;
                                    getMvpView().addMore(wrappers);
                                }
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
    public void getMyList() {
        addObserver(lostAndFoundDataManager.getMyLostAndFounds(),
                new NetworkObserver<ApiResult<QueryLostAndFoundListRespDTO>>(false, true) {

                    @Override
                    public void onReady(ApiResult<QueryLostAndFoundListRespDTO> result) {
                        getMvpView().setRefreshComplete();
                        getMvpView().setLoadMoreComplete();
                        getMvpView().hideEmptyView();
                        getMvpView().hideErrorView();
                        if (null == result.getError()) {
                            if (result.getData().getCommentEnable() != null) {
                                commentEnable = result.getData().getCommentEnable();
                            }
                            if (null != result.getData().getLostAndFounds()) {
                                List<LostAndFoundAdaptor2.LostAndFoundWrapper> wrappers = new ArrayList<>();
                                for (LostAndFoundDTO lost : result.getData().getLostAndFounds()) {
                                    wrappers.add(new LostAndFoundAdaptor2.LostAndFoundWrapper(lost.transform()));
                                }
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
                        }
                    }
                });
    }
}
