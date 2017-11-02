package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.FundsDTO;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.wallet.intf.IRechargeDetailPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IRechargeDetailView;

import javax.inject.Inject;

/**
 * 充值详情
 * <p>
 * Created by zcd on 10/23/17.
 */

public class RechargeDetailPresenter<V extends IRechargeDetailView> extends BasePresenter<V>
    implements IRechargeDetailPresenter<V> {
    private static final String TAG = RechargeDetailPresenter.class.getSimpleName();
    private IWalletDataManager walletDataManager;
    private ISharedPreferencesHelp sharedPreferencesHelp;

    @Inject
    public RechargeDetailPresenter(IWalletDataManager walletDataManager, ISharedPreferencesHelp sharedPreferencesHelp) {
        this.walletDataManager = walletDataManager;
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
