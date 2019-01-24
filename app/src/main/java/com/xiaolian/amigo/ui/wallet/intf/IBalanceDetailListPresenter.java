package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;
import com.xiaolian.amigo.ui.wallet.intf.IBalanceDetailListView;

public interface IBalanceDetailListPresenter<V extends IBalanceDetailListView> extends IBasePresenter<V> {
    void getMonthlyBill(int year, int month);
    Long getAccountCreateTime();
    void getUserBillList(String timeStr, Integer billType, Integer billStatus, Long lastId, boolean pagedown, int pageSize);
}
