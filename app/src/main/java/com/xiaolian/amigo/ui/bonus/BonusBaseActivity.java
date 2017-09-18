package com.xiaolian.amigo.ui.bonus;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.BonusActivityComponent;
import com.xiaolian.amigo.di.componet.DaggerBonusActivityComponent;
import com.xiaolian.amigo.di.module.BonusActivityModule;
import com.xiaolian.amigo.ui.base.BaseListActivity;

/**
 * BonusBaseActivity
 * @author zcd
 */

public abstract class BonusBaseActivity extends BaseListActivity {
    @Override
    protected void setUp() {

    }

    private BonusActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponent = DaggerBonusActivityComponent.builder()
                .bonusActivityModule(new BonusActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();

    }

    public BonusActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
