package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.funds.PersonalWalletDTO;
import com.xiaolian.amigo.data.network.model.timerange.QueryTimeValidRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWalletPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWalletView;

import javax.inject.Inject;

/**
 * 我的钱包
 *
 * @author zcd
 * @date 17/9/18
 */

public class WalletPresenter<V extends IWalletView> extends BasePresenter<V>
        implements IWalletPresenter<V> {
    @SuppressWarnings("unused")
    private static final String TAG = WalletPresenter.class.getSimpleName();
    private IWalletDataManager manager;

    /**
     * 可用余额
     */
    private Double allBalance;
    /**
     * 可提现余额
     */
    private Double chargeBalance;
    /**
     * 赠送的余额
     */
    private Double givingBalance;
    /**
     * 赠送规则
     */
    private String givingRule;
    /**
     * 使用期限
     */
    private String useLimit;

    @Inject
    WalletPresenter(IWalletDataManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void requestNetWork() {
        addObserver(manager.queryWallet(), new NetworkObserver<ApiResult<PersonalWalletDTO>>() {
            @Override
            public void onReady(ApiResult<PersonalWalletDTO> result) {
                if (null == result.getError()) {
                    getMvpView().setBalanceText(result.getData().getAllBalance());
                    allBalance = result.getData().getAllBalance();
                    getMvpView().setPrepayText(result.getData().getPrepay());
                    getMvpView().setBalancePresentText(result.getData().getGivingBalance());
                    givingBalance = result.getData().getGivingBalance();
                    getMvpView().setWithdrawAvailableText(result.getData().getChargeBalance());
                    chargeBalance = result.getData().getChargeBalance();
                    givingRule = result.getData().getGivingRule();
                    useLimit = result.getData().getUseLimit();
                    if (!result.getData().getExistGiving()) {
                        getMvpView().hideBalanceExplain();
                    } else {
                        getMvpView().showBalanceExplain();
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void queryWithdrawTimeValid() {
        addObserver(manager.queryWithDrawTimeValid(), new NetworkObserver<ApiResult<QueryTimeValidRespDTO>>() {

            @Override
            public void onReady(ApiResult<QueryTimeValidRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isValid()) {
                        getMvpView().gotoWithDraw();
                    } else {
                        getMvpView().showTimeValidDialog(result.getData().getTitle(), result.getData().getRemark());
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public String getGivingRule() {
        return givingRule;
    }

    @Override
    public String getUseLimit() {
        return useLimit;
    }

    @Override
    public Double getAllBalance() {
        return allBalance;
    }

    @Override
    public Double getChargeBalance() {
        return chargeBalance;
    }

    @Override
    public Double getGivingBalance() {
        return givingBalance;
    }
}
