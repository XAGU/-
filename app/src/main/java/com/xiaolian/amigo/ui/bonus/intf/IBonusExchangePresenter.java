package com.xiaolian.amigo.ui.bonus.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 兑换代金券
 * <p>
 * Created by zcd on 9/18/17.
 */

public interface IBonusExchangePresenter<V extends IBonusExchangeView> extends IBasePresenter<V> {

    // 兑换代金券
    void exchangeBonus(String code);
}
