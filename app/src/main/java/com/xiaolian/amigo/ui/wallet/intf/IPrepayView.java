package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseListView;
import com.xiaolian.amigo.ui.wallet.adaptor.PrepayAdaptor;

import java.util.List;

/**
 * 预付金额
 *
 * @author zcd
 * @date 17/10/10
 */

public interface IPrepayView extends IBaseListView {
    /**
     * 加载预付列表
     *
     * @param orders 预付账单
     */
    void addMore(List<PrepayAdaptor.OrderWrapper> orders);
}
