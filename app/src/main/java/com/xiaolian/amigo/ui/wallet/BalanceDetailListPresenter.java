package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.wallet.intf.IBalanceDetailListView;
import com.xiaolian.amigo.ui.wallet.intf.IBalanceDetailListPresenter;

import javax.inject.Inject;

public class BalanceDetailListPresenter<V extends IBalanceDetailListView> extends BasePresenter<V> implements IBalanceDetailListPresenter<V> {
    @Inject
    BalanceDetailListPresenter() {
        super();
    }

}
