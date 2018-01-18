package com.xiaolian.amigo.ui.order;

import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.order.intf.IRefundOrderPresenter;
import com.xiaolian.amigo.ui.order.intf.IRefundOrderView;

import javax.inject.Inject;

/**
 * <p>
 * Created by zcd on 18/1/18.
 */

public class RefundOrderPresenter<V extends IRefundOrderView> extends BasePresenter<V>
    implements IRefundOrderPresenter<V> {
    private IOrderDataManager orderDataManager;

    @Inject
    public RefundOrderPresenter(IOrderDataManager orderDataManager) {
        this.orderDataManager = orderDataManager;
    }
}
