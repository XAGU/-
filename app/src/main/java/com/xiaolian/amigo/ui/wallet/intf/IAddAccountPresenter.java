package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 添加账户
 * <p>
 * Created by zcd on 10/27/17.
 */

public interface IAddAccountPresenter<V extends IAddAccountView> extends IBasePresenter<V> {
    void addAccount(String accountName, String userRealName, Integer accountType);
}
