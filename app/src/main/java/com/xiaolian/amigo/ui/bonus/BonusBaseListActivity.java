package com.xiaolian.amigo.ui.bonus;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.BonusActivityComponent;
import com.xiaolian.amigo.di.componet.DaggerBonusActivityComponent;
import com.xiaolian.amigo.di.module.BonusActivityModule;
import com.xiaolian.amigo.ui.base.BaseToolBarListActivity;

/**
 * BonusBaseListActivity
 *
 * @author zcd
 * @date 17/9/18
 */

public abstract class BonusBaseListActivity extends BaseToolBarListActivity {

    private BonusActivityComponent mActivityComponent;

    @Override
    protected void initInject() {
        mActivityComponent = DaggerBonusActivityComponent.builder()
                .bonusActivityModule(new BonusActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();
    }

    public BonusActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
