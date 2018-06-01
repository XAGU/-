package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
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
}
