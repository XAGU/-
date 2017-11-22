package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.wallet.intf.IPrepayOrderPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IPrepayOrderView;

import javax.inject.Inject;

/**
 * 待找零账单
 * <p>
 * Created by zcd on 10/12/17.
 */

public class PrepayOrderPresenter<V extends IPrepayOrderView> extends BasePresenter<V>
        implements IPrepayOrderPresenter<V> {
    private static final String TAG = PrepayOrderPresenter.class.getSimpleName();
    private IWalletDataManager manager;

    @Inject
    public PrepayOrderPresenter(IWalletDataManager manager) {
        super();
        this.manager = manager;
    }
}
