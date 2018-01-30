package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.wallet.intf.IPrepayOrderPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IPrepayOrderView;

import javax.inject.Inject;

/**
 * 待找零账单
 *
 * @author zcd
 * @date 17/10/12
 */

public class PrepayOrderPresenter<V extends IPrepayOrderView> extends BasePresenter<V>
        implements IPrepayOrderPresenter<V> {
    @SuppressWarnings("unused")
    private static final String TAG = PrepayOrderPresenter.class.getSimpleName();
    private IWalletDataManager walletDataManager;

    @Inject
    PrepayOrderPresenter(IWalletDataManager walletDataManager) {
        super();
        this.walletDataManager = walletDataManager;
    }
}
