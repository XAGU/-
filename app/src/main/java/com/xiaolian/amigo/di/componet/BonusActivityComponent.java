package com.xiaolian.amigo.di.componet;

import com.xiaolian.amigo.di.BonusActivityContext;
import com.xiaolian.amigo.di.module.BonusActivityModule;
import com.xiaolian.amigo.ui.bonus.BonusActivity;

import dagger.Component;

/**
 * BonusActivityComponent
 * @author zcd
 */
@BonusActivityContext
@Component(dependencies = ApplicationComponent.class, modules = BonusActivityModule.class)
public interface BonusActivityComponent {
    void inject(BonusActivity activity);
}
