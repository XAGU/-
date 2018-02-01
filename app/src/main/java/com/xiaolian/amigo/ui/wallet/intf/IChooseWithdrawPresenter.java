package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 选择提现
 *
 * @author zcd
 * @date 17/10/27
 */

public interface IChooseWithdrawPresenter<V extends IChooseWithdrawView> extends IBasePresenter<V> {
    /**
     * 获取账户列表
     *
     * @param type 账户类型
     */
    void requestAccounts(int type);

    /**
     * 删除账户
     *
     * @param id 账户id
     */
    void deleteAccount(Long id);
}
