package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 充值
 * <p>
 * Created by zcd on 9/20/17.
 */

public interface IRechargePresenter<V extends IRechargeView> extends IBasePresenter<V> {
    void getRechargeList();

    void recharge(Double amount, int type);

    void parseAlipayResult(String resultStatus, String result, String memo);
}
