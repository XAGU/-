package com.xiaolian.amigo.ui.device.bathroom.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * @author zcd
 * @date 18/6/29
 */
public interface IBookingPresenter<V extends IBookingView> extends IBasePresenter<V> {
    void pay(Double prepayAmount, Long bonusId);

    void cancel();
}
