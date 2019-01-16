package com.xiaolian.amigo.ui.wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.BalanceDetailListActivityComponent;
import com.xiaolian.amigo.di.componet.DaggerBalanceDetailListActivityComponent;
import com.xiaolian.amigo.di.module.BalanceDetailListActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;

public class BalanceDetailListBaseActivity extends BaseActivity {
    private BalanceDetailListActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponent = DaggerBalanceDetailListActivityComponent.builder()
                .balanceDetailListActivityModule(new BalanceDetailListActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();

        initInject();
        setUp();
    }

    @Override
    protected void setUp() {

    }

    protected void initInject() {
    }

    public BalanceDetailListActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
