package com.xiaolian.amigo.ui.repair;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerRepairActivityComponent;
import com.xiaolian.amigo.di.componet.RepairActivityComponent;
import com.xiaolian.amigo.di.module.RepairActivityModule;
import com.xiaolian.amigo.ui.base.BaseListActivity;

/**
 * 报修模块BaseListActivity
 * <p>
 * Created by zcd on 9/25/17.
 */

public abstract class RepairBaseListActivity extends BaseListActivity {
    private RepairActivityComponent mActivityComponent;


    @Override
    protected void initInject() {
        mActivityComponent = DaggerRepairActivityComponent.builder()
                .repairActivityModule(new RepairActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();

    }

    public RepairActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
