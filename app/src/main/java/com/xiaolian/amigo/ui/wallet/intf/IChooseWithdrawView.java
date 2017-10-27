package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.wallet.adaptor.ChooseWithdrawAdapter;

import java.util.List;

/**
 * 选择提现账户
 * <p>
 * Created by zcd on 10/27/17.
 */

public interface IChooseWithdrawView extends IBaseView {
    void addMore(List<ChooseWithdrawAdapter.Item> items);

    void refreshList();
}
