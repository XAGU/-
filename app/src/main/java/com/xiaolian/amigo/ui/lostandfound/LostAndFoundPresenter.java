package com.xiaolian.amigo.ui.lostandfound;

import com.xiaolian.amigo.data.manager.intf.ILostAndFoundDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.QueryLostAndFoundListReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryLostAndFoundListRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LostAndFound;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundAdaptor;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 失物招领
 * <p>
 * Created by zcd on 9/18/17.
 */

public class LostAndFoundPresenter<V extends ILostAndFoundView> extends BasePresenter<V>
        implements ILostAndFoundPresenter<V> {

    ILostAndFoundDataManager manager;

    @Inject
    public LostAndFoundPresenter(ILostAndFoundDataManager manager) {
        super();
        this.manager = manager;
    }

    public void queryLostAndFoundList(Integer page, Integer size, Integer type, String selectKey, boolean isSearch) {
        QueryLostAndFoundListReqDTO dto = new QueryLostAndFoundListReqDTO();
        dto.setPage(page);
        // TODO: 添加schoolId
        dto.setSchoolId(null);
//        dto.setSchoolId(manager.getUserInfo().getSchoolId());
        dto.setSelectKey(selectKey);
        dto.setSize(size);
        dto.setType(type);
        addObserver(manager.queryLostAndFounds(dto), new NetworkObserver<ApiResult<QueryLostAndFoundListRespDTO>>() {

            @Override
            public void onReady(ApiResult<QueryLostAndFoundListRespDTO> result) {
                if (null == result.getError()) {

                    List<LostAndFoundAdaptor.LostAndFoundWapper> wrappers = new ArrayList<>();
                    if (null != result.getData().getLostAndFounds()) {
                        for (LostAndFound lost : result.getData().getLostAndFounds()) {
                            wrappers.add(new LostAndFoundAdaptor.LostAndFoundWapper(lost));
                        }
                        if (isSearch) {
                            if (wrappers.isEmpty()) {
                                getMvpView().showNoSearchResult(selectKey);
                            } else {
                                getMvpView().showSearchResult(wrappers);
                            }
                        } else {
                            if (type == null) {
                                getMvpView().addMore(wrappers);
                            } else {
                                if (type == 1) {
                                    getMvpView().addMoreLost(wrappers);
                                } else {
                                    getMvpView().addMoreFound(wrappers);
                                }
                            }
                        }
                    }
                } else {
                    getMvpView().showMessage(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void queryLostAndFoundList(Integer page, Integer size, Integer type, String selectKey) {
        queryLostAndFoundList(page, size, type, selectKey, false);
    }

    @Override
    public void queryLostList(Integer page, Integer size) {
        // type 1 表示失物
        queryLostAndFoundList(page, size, 1, null);
    }

    @Override
    public void queryFoundList(Integer page, Integer size) {
        // type 2 表示招领
        queryLostAndFoundList(page, size, 2, null);
    }

    @Override
    public void searchLostList(Integer page, Integer size, String selectKey) {
        // type 1 表示失物
        queryLostAndFoundList(page, size, 1, selectKey, true);
    }

    @Override
    public void searchFoundList(Integer page, Integer size, String selectKey) {
        // type 2 表示招领
        queryLostAndFoundList(page, size, 2, selectKey, true);
    }

}
