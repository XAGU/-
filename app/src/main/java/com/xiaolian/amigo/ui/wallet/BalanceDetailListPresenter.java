package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.userbill.QueryMonthlyBillReqDTO;
import com.xiaolian.amigo.data.network.model.userbill.UserMonthlyBillRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.wallet.intf.IBalanceDetailListView;
import com.xiaolian.amigo.ui.wallet.intf.IBalanceDetailListPresenter;

import javax.inject.Inject;

public class BalanceDetailListPresenter<V extends IBalanceDetailListView> extends BasePresenter<V> implements IBalanceDetailListPresenter<V> {
    private IWalletDataManager walletDataManager;

    @Inject
    BalanceDetailListPresenter(IWalletDataManager walletDataManager) {
        this.walletDataManager = walletDataManager;
    }

    @Override
    public void getMonthlyBill(int year, int month) {
        QueryMonthlyBillReqDTO reqDTO = new QueryMonthlyBillReqDTO();
        reqDTO.setYear(year);
        reqDTO.setMonth(month);
        addObserver(walletDataManager.getMonthlyBill(reqDTO),
                new NetworkObserver<ApiResult<UserMonthlyBillRespDTO>>() {

                    @Override
                    public void onReady(ApiResult<UserMonthlyBillRespDTO> result) {
                        if (null == result.getError()) {
                            if (result.getData() != null) {
                                getMvpView().render(result.getData());
                            }
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }
                });
    }

    @Override
    public Long getAccountCreateTime() {
        return walletDataManager.getUser().getCreateTime();
    }

}
