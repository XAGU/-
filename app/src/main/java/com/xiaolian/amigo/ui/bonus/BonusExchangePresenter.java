package com.xiaolian.amigo.ui.bonus;

import com.xiaolian.amigo.data.manager.intf.IBonusDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusExchangePresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusExchangeView;

import javax.inject.Inject;

/**
 * 兑换红包
 * <p>
 * Created by zcd on 9/18/17.
 */

public class BonusExchangePresenter<V extends IBonusExchangeView> extends BasePresenter<V>
        implements IBonusExchangePresenter<V> {

    private static final String TAG = BonusExchangePresenter.class.getSimpleName();
    private IBonusDataManager manager;

    @Inject
    public BonusExchangePresenter(IBonusDataManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void exchangeBonus() {

    }
}
