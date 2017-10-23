package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
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
    private IWalletDataManager manager;

    @Inject
    public RechargeDetailPresenter(IWalletDataManager manager) {
        this.manager = manager;
    }
}
