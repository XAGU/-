package com.xiaolian.amigo.ui.lostandfound;

import com.xiaolian.amigo.data.manager.intf.ILostAndFoundDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.lostandfound.CollectListReqDTO;
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
    public void resetPage() {
        page = Constant.PAGE_START_NUM;
    }
}
