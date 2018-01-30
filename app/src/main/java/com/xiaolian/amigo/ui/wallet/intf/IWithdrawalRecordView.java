package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseListView;
import com.xiaolian.amigo.ui.wallet.adaptor.WithdrawalAdaptor;

import java.util.List;

/**
 * 充值提现记录
 *
 * @author zcd
 * @date 17/10/17
 */

public interface IWithdrawalRecordView extends IBaseListView {
    /**
     * 加载提现记录列表
     *
     * @param wrappers 提现记录
     */
    void addMore(List<WithdrawalAdaptor.WithdrawalWrapper> wrappers);
}
