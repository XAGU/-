package com.xiaolian.amigo.di.componet;

import com.xiaolian.amigo.di.BonusActivityContext;
import com.xiaolian.amigo.di.module.BonusActivityModule;
import com.xiaolian.amigo.ui.bonus.BonusActivity;
import com.xiaolian.amigo.ui.bonus.BonusExchangeActivity;
import com.xiaolian.amigo.ui.bonus.ExpiredBonusActivity;

import dagger.Component;

/**
 * BonusActivityComponent
 *
 * @author zcd
 */
@BonusActivityContext
@Component(dependencies = ApplicationComponent.class, modules = BonusActivityModule.class)
public interface BonusActivityComponent {
    void inject(BonusActivity activity);

    void inject(BonusExchangeActivity activity);

    void inject(ExpiredBonusActivity activity);
}
