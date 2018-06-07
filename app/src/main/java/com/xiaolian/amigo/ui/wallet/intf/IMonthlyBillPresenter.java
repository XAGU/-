package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * @author zcd
 * @date 18/6/1
 */
public interface IMonthlyBillPresenter<V extends IBaseView> extends IBasePresenter<V> {
    void getMonthlyBill(int year, int month);

    Long getAccountCreateTime();
}
