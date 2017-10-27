package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 提现
 * <p>
 * Created by zcd on 10/14/17.
 */

public interface IWithdrawalPresenter<V extends IWithdrawalView> extends IBasePresenter<V> {

    void withdraw(String amount, Long withdrawId);
}
