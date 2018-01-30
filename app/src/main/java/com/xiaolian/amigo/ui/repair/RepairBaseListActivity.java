package com.xiaolian.amigo.ui.repair;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerRepairActivityComponent;
import com.xiaolian.amigo.di.componet.RepairActivityComponent;
import com.xiaolian.amigo.di.module.RepairActivityModule;
import com.xiaolian.amigo.ui.base.BaseToolBarListActivity;

/**
 * 报修模块BaseListActivity
 *
 * @author zcd
 * @date 17/9/25
 */

public abstract class RepairBaseListActivity extends BaseToolBarListActivity {
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
