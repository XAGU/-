package com.xiaolian.amigo.ui.device;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerDeviceActivityComponent;
import com.xiaolian.amigo.di.componet.DeviceActivityComponent;
import com.xiaolian.amigo.di.module.DeviceActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;


public abstract class DeviceBaseActivity extends BaseActivity {

    private DeviceActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp();
        mActivityComponent = DaggerDeviceActivityComponent.builder()
                .deviceActivityModule(new DeviceActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();

    }

    public DeviceActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
