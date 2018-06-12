package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.userbill.QueryMonthlyBillReqDTO;
import com.xiaolian.amigo.data.network.model.userbill.UserMonthlyBillRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.wallet.intf.IMonthlyBillPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IMonthlyBillView;

import javax.inject.Inject;

/**
 * @author zcd
 * @date 18/6/1
 */
public class MonthlyBillPresenter<V extends IMonthlyBillView> extends BasePresenter<V>
        implements IMonthlyBillPresenter<V> {
    private IWalletDataManager walletDataManager;

    @Inject
    public MonthlyBillPresenter(IWalletDataManager walletDataManager) {
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
