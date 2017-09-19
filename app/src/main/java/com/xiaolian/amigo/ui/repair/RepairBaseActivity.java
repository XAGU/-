package com.xiaolian.amigo.ui.repair;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerOrderActivityComponent;
import com.xiaolian.amigo.di.componet.DaggerRepairActivityComponent;
import com.xiaolian.amigo.di.componet.OrderActivityComponent;
import com.xiaolian.amigo.di.componet.RepairActivityComponent;
import com.xiaolian.amigo.di.module.OrderActivityModule;
import com.xiaolian.amigo.di.module.RepairActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;


public abstract class RepairBaseActivity extends BaseActivity {

    private RepairActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp();
        mActivityComponent = DaggerRepairActivityComponent.builder()
                .repairActivityModule(new RepairActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();

    }

    public RepairActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
