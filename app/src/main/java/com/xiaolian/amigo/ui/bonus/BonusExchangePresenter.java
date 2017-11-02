package com.xiaolian.amigo.ui.bonus;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.IBonusDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.RedeemBonusReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusExchangePresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusExchangeView;

import javax.inject.Inject;

/**
 * 兑换红包
 * <p>
 * Created by zcd on 9/18/17.
 */

public class BonusExchangePresenter<V extends IBonusExchangeView> extends BasePresenter<V>
        implements IBonusExchangePresenter<V> {

    private static final String TAG = BonusExchangePresenter.class.getSimpleName();
    private IBonusDataManager manager;

    @Inject
    public BonusExchangePresenter(IBonusDataManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void exchangeBonus(String code) {
        RedeemBonusReqDTO reqDTO = new RedeemBonusReqDTO();
        reqDTO.setCode(code);
        addObserver(manager.redeem(reqDTO), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isResult()) {
                        getMvpView().onSuccess(R.string.bonus_exchange_success);
                        getMvpView().backToBonus();
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });

    }
}
