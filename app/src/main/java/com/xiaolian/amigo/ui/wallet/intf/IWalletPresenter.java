package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 我的钱包
 *
 * @author zcd
 * @date 17/9/18
 */

public interface IWalletPresenter<V extends IWalletView> extends IBasePresenter<V> {

    /**
     * 获取余额，预付金额
     */
    void requestNetWork();

    /**
     * 查询提现时间段
     */
    void queryWithdrawTimeValid();
}
