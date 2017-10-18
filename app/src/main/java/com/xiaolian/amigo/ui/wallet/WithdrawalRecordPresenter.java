package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawRecordPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalRecordView;

import javax.inject.Inject;

/**
 * 充值提现记录
 * <p>
 * Created by zcd on 10/17/17.
 */

public class WithdrawalRecordPresenter<V extends IWithdrawalRecordView> extends BasePresenter<V>
        implements IWithdrawRecordPresenter<V> {
    private static final String TAG = WithdrawalRecordPresenter.class.getSimpleName();
    private IWalletDataManager manager;

    @Inject
    public WithdrawalRecordPresenter(IWalletDataManager manager) {
        super();
        this.manager = manager;
    }
    @Override
    public void requestWithdrawalRecord(int page) {

    }
}
