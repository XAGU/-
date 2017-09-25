package com.xiaolian.amigo.ui.repair;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerRepairActivityComponent;
import com.xiaolian.amigo.di.componet.RepairActivityComponent;
import com.xiaolian.amigo.di.module.RepairActivityModule;
import com.xiaolian.amigo.ui.base.BaseToolBarActivity;


public abstract class RepairBaseActivity extends BaseToolBarActivity {

    private RepairActivityComponent mActivityComponent;

    @Override
    protected void initInject() {
        mActivityComponent = DaggerRepairActivityComponent.builder()
                .repairActivityModule(new RepairActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();
        setUp();
    }

    public RepairActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
