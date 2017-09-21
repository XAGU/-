package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryRechargeAmountsRespDTO;
import com.xiaolian.amigo.data.network.model.wallet.RechargeDenominations;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.wallet.adaptor.RechargeAdaptor;
import com.xiaolian.amigo.ui.wallet.intf.IRechargePresenter;
import com.xiaolian.amigo.ui.wallet.intf.IRechargeView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 充值
 * <p>
 * Created by zcd on 9/20/17.
 */

public class RechargePresenter<V extends IRechargeView> extends BasePresenter<V>
        implements IRechargePresenter<V> {
    private static final String TAG = RechargePresenter.class.getSimpleName();
    private IWalletDataManager manager;

    @Inject
    public RechargePresenter(IWalletDataManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void getRechargeList() {
        addObserver(manager.queryRechargeAmountList(new SimpleQueryReqDTO()), new NetworkObserver<ApiResult<QueryRechargeAmountsRespDTO>>() {

            @Override
            public void onReady(ApiResult<QueryRechargeAmountsRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getRechargeDenominations() != null
                            && result.getData().getRechargeDenominations().size() > 0) {
                        List<RechargeAdaptor.RechargeWapper> rechargeWapper = new ArrayList<>();
                        for (RechargeDenominations rechargeDenominations : result.getData().getRechargeDenominations()) {
                            rechargeWapper.add(new RechargeAdaptor.RechargeWapper(rechargeDenominations));
                        }
                        getMvpView().addMore(rechargeWapper);
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
