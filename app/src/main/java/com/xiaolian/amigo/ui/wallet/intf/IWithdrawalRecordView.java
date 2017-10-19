package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseListView;
import com.xiaolian.amigo.ui.wallet.adaptor.WithdrawalAdaptor;

import java.util.List;

/**
 * 充值提现记录
 * <p>
 * Created by zcd on 10/17/17.
 */

public interface IWithdrawalRecordView extends IBaseListView {
    void addMore(List<WithdrawalAdaptor.WithdrawalWrapper> wrappers);
}
