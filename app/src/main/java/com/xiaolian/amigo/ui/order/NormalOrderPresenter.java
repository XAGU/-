package com.xiaolian.amigo.ui.order;

import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.order.intf.INormalOrderPresenter;
import com.xiaolian.amigo.ui.order.intf.INormalOrderView;

import javax.inject.Inject;

/**
 * <p>
 * Created by zcd on 18/1/18.
 */

public class NormalOrderPresenter<V extends INormalOrderView> extends BasePresenter<V>
        implements INormalOrderPresenter<V> {
    private IOrderDataManager orderDataManager;

    @Inject
    public NormalOrderPresenter(IOrderDataManager orderDataManager) {
        this.orderDataManager = orderDataManager;
    }
}
