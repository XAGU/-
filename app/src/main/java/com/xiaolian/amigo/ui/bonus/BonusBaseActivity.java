package com.xiaolian.amigo.ui.bonus;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.BonusActivityComponent;
import com.xiaolian.amigo.di.componet.DaggerBonusActivityComponent;
import com.xiaolian.amigo.di.module.BonusActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;
import com.xiaolian.amigo.ui.base.BaseToolBarActivity;

/**
 * BonusBaseActivity
 * Created by zcd on 9/18/17.
 */

public abstract class BonusBaseActivity extends BaseToolBarActivity {


    private BonusActivityComponent mActivityComponent;

    @Override
    protected void initInject() {
        setUp();
        mActivityComponent = DaggerBonusActivityComponent.builder()
                .bonusActivityModule(new BonusActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();

    }

    public BonusActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
