package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 提现
 *
 * @author zcd
 * @date 17/10/14
 */

public interface IWithdrawalPresenter<V extends IWithdrawalView> extends IBasePresenter<V> {
    /**
     * 提现
     *
     * @param amount       金额
     * @param withdrawName 提现名称
     * @param withdrawId   提现id
     */
    void withdraw(String amount, String withdrawName, Long withdrawId);

    /**
     * 获取账户列表
     *
     * @param type 账户类型
     */
    void requestAccounts(int type);

    /**
     * 清除账户
     */
    void clearAccount();
}
