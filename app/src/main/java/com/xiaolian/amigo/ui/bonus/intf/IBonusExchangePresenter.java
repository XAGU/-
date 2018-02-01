package com.xiaolian.amigo.ui.bonus.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 兑换代金券
 *
 * @author zcd
 * @date 17/9/18
 */

public interface IBonusExchangePresenter<V extends IBonusExchangeView> extends IBasePresenter<V> {

    /**
     * 兑换代金券
     *
     * @param code 兑换码
     */
    void exchangeBonus(String code);
}
