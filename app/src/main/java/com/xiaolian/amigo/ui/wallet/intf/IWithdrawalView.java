package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 提现
 * <p>
 * Created by zcd on 10/14/17.
 */

public interface IWithdrawalView extends IBaseView {
    void back();

    void gotoWithdrawDetail(Long id);

    void showWithdrawAccount(String accountName, Long id);
}
