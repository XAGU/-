package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 待找零账单
 *
 * @author zcd
 * @date 17/10/12
 */

public interface IPrepayOrderPresenter<V extends IPrepayOrderView> extends IBasePresenter<V> {
    /**
     * 获取账单
     *
     * @param orderId 账单id
     */
    void getOrder(Long orderId);
}