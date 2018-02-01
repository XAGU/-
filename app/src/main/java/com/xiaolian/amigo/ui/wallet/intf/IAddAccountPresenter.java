package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 添加账户
 *
 * @author zcd
 * @date 17/10/27
 */

public interface IAddAccountPresenter<V extends IAddAccountView> extends IBasePresenter<V> {
    /**
     * 添加账户
     *
     * @param accountName  账户名称
     * @param userRealName 用户真实姓名
     * @param accountType  账户类型
     */
    void addAccount(String accountName, String userRealName, Integer accountType);
}
