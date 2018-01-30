package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.wallet.adaptor.ChooseWithdrawAdapter;

import java.util.List;

/**
 * 选择提现账户
 *
 * @author zcd
 * @date 17/10/27
 */

public interface IChooseWithdrawView extends IBaseView {
    /**
     * 加载列表
     *
     * @param items 列表元素
     */
    void addMore(List<ChooseWithdrawAdapter.Item> items);

    /**
     * 刷新列表
     */
    void refreshList();
}
