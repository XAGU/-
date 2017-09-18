package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 我的钱包
 * <p>
 * Created by zcd on 9/18/17.
 */

public interface IWalletPresenter<V extends IWalletView> extends IBasePresenter<V> {

    // 获取余额，预付金额
    void requestNetWork();
}
