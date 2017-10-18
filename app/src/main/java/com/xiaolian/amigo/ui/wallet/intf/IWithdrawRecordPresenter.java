package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 充值提现记录
 * <p>
 * Created by zcd on 10/17/17.
 */

public interface IWithdrawRecordPresenter<V extends IWithdrawalRecordView> extends IBasePresenter<V>{

    // 查看充值提现记录
    void requestWithdrawalRecord(int page);
}
