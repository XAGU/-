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
                    getMvpView().setBalanceText(result.getData().getBalance());
                    getMvpView().setPrepayText(result.getData().getPrepay());
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
}
