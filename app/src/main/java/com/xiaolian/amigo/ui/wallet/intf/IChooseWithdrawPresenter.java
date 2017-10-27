package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 选择提现
 * <p>
 * Created by zcd on 10/27/17.
 */

public interface IChooseWithdrawPresenter<V extends IChooseWithdrawView> extends IBasePresenter<V> {
    void requestAccounts(int type);

    void deleteAccount(Long id);
}
