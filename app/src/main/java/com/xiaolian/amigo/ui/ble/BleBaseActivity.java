package com.xiaolian.amigo.ui.ble;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.BleActivityComponent;
import com.xiaolian.amigo.di.componet.DaggerBleActivityComponent;
import com.xiaolian.amigo.di.module.BleActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;


public abstract class BleBaseActivity extends BaseActivity {

    private BleActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponent = DaggerBleActivityComponent.builder()
                .bleActivityModule(new BleActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();

    }

    public BleActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
