package com.xiaolian.amigo.ui.bonus;

import com.xiaolian.amigo.data.manager.intf.IBonusDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bonus.Bonus;
import com.xiaolian.amigo.data.network.model.dto.request.QueryUserBonusReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryUserBonusListRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.bonus.adaptor.BonusAdaptor;
import com.xiaolian.amigo.ui.bonus.intf.IBonusPresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 红包Presenter实现类
 * @author zcd
 */
public class BonusPresenter<V extends IBonusView> extends BasePresenter<V>
        implements IBonusPresenter<V> {
    private static final String TAG = BonusPresenter.class.getSimpleName();
    private IBonusDataManager manager;

    @Inject
    public BonusPresenter(IBonusDataManager manager) {
        this.manager = manager;
    }

    @Override
    public void requestBonusList(int page) {
        QueryUserBonusReqDTO dto = new QueryUserBonusReqDTO();
        dto.setPage(page);
        dto.setSize(Constant.PAGE_SIZE);
        dto.setUseStatus(2);
        dto.setValidStatus(1);
        addObserver(manager.queryOrders(dto), new NetworkObserver<ApiResult<QueryUserBonusListRespDTO>>() {
            @Override
            public void onReady(ApiResult<QueryUserBonusListRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().setLoadAll(false);
                    List<BonusAdaptor.BonusWrapper> wrappers = new ArrayList<>();
                    if (null != result.getData().getBonuses() && result.getData().getBonuses().size() > 0) {
                        for (Bonus bonus : result.getData().getBonuses()) {
                            wrappers.add(new BonusAdaptor.BonusWrapper(bonus));
                        }
                        getMvpView().addMore(wrappers);
                        getMvpView().loadComplete();
                        if (result.getData().getBonuses().size() < Constant.PAGE_SIZE) {
                            getMvpView().showNoMoreDataView();
                            getMvpView().setLoadAll(true);
                        } else {
                            getMvpView().hideLoadMoreView();
                            getMvpView().addPage();
                        }
                        if (getMvpView().isRefreshing()) {
                            getMvpView().setRefreshing(false);
                        }
                    }
                } else {
                    getMvpView().setLoadAll(true);
                    getMvpView().hideLoadMoreView();
                    getMvpView().loadComplete();
                    getMvpView().showMessage(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
