package com.xiaolian.amigo.ui.order;

import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.order.intf.IRefundOrderPresenter;
import com.xiaolian.amigo.ui.order.intf.IRefundOrderView;

import javax.inject.Inject;

/**
 * 退款账单
 *
 * @author zcd
 * @date 18/1/18
 */

public class RefundOrderPresenter<V extends IRefundOrderView> extends BasePresenter<V>
        implements IRefundOrderPresenter<V> {
    private IOrderDataManager orderDataManager;

    @Inject
    RefundOrderPresenter(IOrderDataManager orderDataManager) {
        this.orderDataManager = orderDataManager;
    }
}
