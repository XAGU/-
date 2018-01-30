package com.xiaolian.amigo.ui.bonus;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.IBonusDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bonus.RedeemBonusReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusExchangePresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusExchangeView;

import javax.inject.Inject;

/**
 * 兑换代金券
 *
 * @author zcd
 * @date 17/9/18
 */

public class BonusExchangePresenter<V extends IBonusExchangeView> extends BasePresenter<V>
        implements IBonusExchangePresenter<V> {
    @SuppressWarnings("unused")
    private static final String TAG = BonusExchangePresenter.class.getSimpleName();
    private IBonusDataManager bonusDataManager;

    @Inject
    BonusExchangePresenter(IBonusDataManager bonusDataManager) {
        super();
        this.bonusDataManager = bonusDataManager;
    }

    @Override
    public void exchangeBonus(String code) {
        RedeemBonusReqDTO reqDTO = new RedeemBonusReqDTO();
        reqDTO.setCode(code);
        addObserver(bonusDataManager.redeem(reqDTO), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

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
