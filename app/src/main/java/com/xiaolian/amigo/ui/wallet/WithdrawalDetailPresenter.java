package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.FundsDTO;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalDetailPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalDetailView;

import javax.inject.Inject;

/**
 * 提现详情
 * <p>
 * Created by zcd on 10/23/17.
 */

public class WithdrawalDetailPresenter<V extends IWithdrawalDetailView> extends BasePresenter<V>
        implements IWithdrawalDetailPresenter<V> {
    private static final String TAG = WithdrawalDetailPresenter.class.getSimpleName();
    private IWalletDataManager walletDataManager;
    private ISharedPreferencesHelp sharedPreferencesHelp;

    @Inject
    public WithdrawalDetailPresenter(IWalletDataManager manager,
                                     ISharedPreferencesHelp sharedPreferencesHelp) {
        this.walletDataManager = manager;
        this.sharedPreferencesHelp = sharedPreferencesHelp;
    }

    @Override
    public void requestData(Long id) {
        SimpleReqDTO reqDTO = new SimpleReqDTO();
        reqDTO.setId(id);
        addObserver(walletDataManager.queryWithdrawRechargeDetail(reqDTO), new NetworkObserver<ApiResult<FundsDTO>>() {

            @Override
            public void onReady(ApiResult<FundsDTO> result) {
                if (null == result.getError()) {
                    getMvpView().render(result.getData());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public String getToken() {
        return sharedPreferencesHelp.getToken();
    }
}
