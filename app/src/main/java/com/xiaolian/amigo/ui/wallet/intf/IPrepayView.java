package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseListView;
import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.wallet.adaptor.PrepayAdaptor;

import java.util.List;

/**
 * 预付金额
 * <p>
 * Created by zcd on 10/10/17.
 */

public interface IPrepayView extends IBaseListView {
    void addMore(List<PrepayAdaptor.OrderWrapper> orders);
}
