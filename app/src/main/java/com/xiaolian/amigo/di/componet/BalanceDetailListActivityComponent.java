package com.xiaolian.amigo.di.componet;

import com.xiaolian.amigo.di.BalanceDetailListActivityContext;
import com.xiaolian.amigo.di.module.BalanceDetailListActivityModule;
import com.xiaolian.amigo.ui.wallet.BalanceDetailListActivity;

import com.xiaolian.amigo.ui.wallet.BalanceDetailListBaseActivity;

import dagger.Component;

@BalanceDetailListActivityContext
@Component(dependencies = ApplicationComponent.class, modules = BalanceDetailListActivityModule.class)
public interface BalanceDetailListActivityComponent {

    void inject(BalanceDetailListActivity activity);

    void inject(BalanceDetailListBaseActivity activity);

}
