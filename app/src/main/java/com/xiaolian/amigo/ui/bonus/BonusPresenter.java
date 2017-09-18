package com.xiaolian.amigo.ui.bonus;

import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusPresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusView;

import javax.inject.Inject;

/**
 * 红包Presenter实现类
 * @author zcd
 */
public class BonusPresenter<V extends IBonusView> extends BasePresenter<V>
        implements IBonusPresenter<V> {
    @Inject
    public BonusPresenter() {
    }
}
