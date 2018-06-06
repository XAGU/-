package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 充值提现记录
 *
 * @author zcd
 * @date 17/10/17
 */

public interface IWithdrawRecordPresenter<V extends IWithdrawalRecordView> extends IBasePresenter<V> {

    /**
     * 查看充值提现记录
     *
     * @param page 页数
     */
    void requestWithdrawalRecord(int page, Integer fundsType,
                                 Integer year, Integer month);
}
