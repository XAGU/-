package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.WithdrawReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalView;

import javax.inject.Inject;

/**
 * 提现
 * <p>
 * Created by zcd on 10/14/17.
 */

public class WithdrawalPresenter<V extends IWithdrawalView> extends BasePresenter<V>
        implements IWithdrawalPresenter<V> {
    IWalletDataManager manager;

    @Inject
    public WithdrawalPresenter(IWalletDataManager manager) {
        this.manager = manager;
    }

    @Override
    public void withdraw(String amount, Long withdrawId) {
        WithdrawReqDTO reqDTO = new WithdrawReqDTO();
        reqDTO.setAmount(amount);
        reqDTO.setUserThirdAccountId(withdrawId);
        addObserver(manager.withdraw(reqDTO), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().onSuccess("提现成功");
                    getMvpView().back();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
