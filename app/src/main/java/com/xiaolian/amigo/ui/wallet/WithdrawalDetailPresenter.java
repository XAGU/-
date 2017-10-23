package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
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
    private IWalletDataManager manager;

    @Inject
    public WithdrawalDetailPresenter(IWalletDataManager manager) {
        this.manager = manager;
    }
}
