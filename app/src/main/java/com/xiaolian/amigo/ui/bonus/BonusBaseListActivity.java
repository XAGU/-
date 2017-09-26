package com.xiaolian.amigo.ui.bonus;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.BonusActivityComponent;
import com.xiaolian.amigo.di.componet.DaggerBonusActivityComponent;
import com.xiaolian.amigo.di.module.BonusActivityModule;
import com.xiaolian.amigo.ui.base.BaseListActivity;
import com.xiaolian.amigo.ui.base.BaseToolBarActivity;
import com.xiaolian.amigo.ui.base.BaseToolBarListActivity;

/**
 * BonusBaseListActivity
 * @author zcd
 */

public abstract class BonusBaseListActivity extends BaseToolBarListActivity {
    @Override
    protected void setUp() {

    }

    private BonusActivityComponent mActivityComponent;

    protected  void initInject() {
        mActivityComponent = DaggerBonusActivityComponent.builder()
                .bonusActivityModule(new BonusActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();
    }

    public BonusActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
