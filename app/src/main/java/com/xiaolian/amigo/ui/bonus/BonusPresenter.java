package com.xiaolian.amigo.ui.bonus;

import com.xiaolian.amigo.data.manager.intf.IBonusDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bonus.QueryUserBonusListRespDTO;
import com.xiaolian.amigo.data.network.model.bonus.QueryUserBonusReqDTO;
import com.xiaolian.amigo.data.network.model.bonus.UserBonusDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.bonus.adaptor.BonusAdaptor;
import com.xiaolian.amigo.ui.bonus.intf.IBonusPresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 代金券Presenter实现类
 *
 * @author zcd
 * @date 17/9/18
 */
public class BonusPresenter<V extends IBonusView> extends BasePresenter<V>
        implements IBonusPresenter<V> {
    @SuppressWarnings("unused")
    private static final String TAG = BonusPresenter.class.getSimpleName();
    private IBonusDataManager bonusDataManager;

    @Inject
    BonusPresenter(IBonusDataManager bonusDataManager) {
        super();
        this.bonusDataManager = bonusDataManager;
    }

    @Override
    public void requestBonusList(int page, Integer deviceType, boolean checkUse) {
        // checkUse 是否检查生效时间
        QueryUserBonusReqDTO dto = new QueryUserBonusReqDTO();
        dto.setPage(page);
        dto.setDeviceType(deviceType);
        dto.setSize(Constant.PAGE_SIZE);
        // 显示代金券 未使用1 未过期1 过期代金券 未使用1 已过期2
        dto.setUseStatus(1);
        dto.setValidStatus(1);
        addObserver(bonusDataManager.queryOrders(dto), new NetworkObserver<ApiResult<QueryUserBonusListRespDTO>>(false, true) {
            @Override
            public void onReady(ApiResult<QueryUserBonusListRespDTO> result) {
                getMvpView().setRefreshComplete();
                getMvpView().setLoadMoreComplete();
                getMvpView().hideEmptyView();
                getMvpView().hideErrorView();
                if (null == result.getError()) {
                    if (null != result.getData().getBonuses() && result.getData().getBonuses().size() > 0) {
                        List<BonusAdaptor.BonusWrapper> wrappers = new ArrayList<>();
                        for (UserBonusDTO bonus : result.getData().getBonuses()) {
                            if (checkUse) {
                                if (bonus.getStartTime() != null && result.getTimestamp() >= bonus.getStartTime()) {
                                    wrappers.add(new BonusAdaptor.BonusWrapper(bonus.transform()));
                                }
                            } else {
                                wrappers.add(new BonusAdaptor.BonusWrapper(bonus.transform()));
                            }
                        }
                        getMvpView().addMore(wrappers);
                        getMvpView().addPage();
                    } else {
                        getMvpView().showEmptyView();
                    }
                } else {
                    getMvpView().showErrorView();
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
    public void requestExpiredBonusList(int page) {
        QueryUserBonusReqDTO dto = new QueryUserBonusReqDTO();
        dto.setPage(page);
        dto.setSize(Constant.PAGE_SIZE);
        // 显示代金券 未使用1 未过期1 过期代金券 未使用1 已过期2
        dto.setUseStatus(1);
        dto.setValidStatus(2);
        addObserver(bonusDataManager.queryOrders(dto), new NetworkObserver<ApiResult<QueryUserBonusListRespDTO>>(false) {
            @Override
            public void onReady(ApiResult<QueryUserBonusListRespDTO> result) {
                getMvpView().setRefreshComplete();
                getMvpView().setLoadMoreComplete();
                getMvpView().hideEmptyView();
                getMvpView().hideErrorView();
                if (null == result.getError()) {
                    if (null != result.getData().getBonuses() && result.getData().getBonuses().size() > 0) {
                        List<BonusAdaptor.BonusWrapper> wrappers = new ArrayList<>();
                        for (UserBonusDTO bonus : result.getData().getBonuses()) {
                            wrappers.add(new BonusAdaptor.BonusWrapper(bonus.transform()));
                        }
                        getMvpView().addMore(wrappers);
                        getMvpView().addPage();
                    } else {
                        getMvpView().showEmptyView();
                    }
                } else {
                    getMvpView().showErrorView();
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
}
