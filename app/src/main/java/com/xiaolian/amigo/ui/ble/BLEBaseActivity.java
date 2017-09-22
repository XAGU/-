package com.xiaolian.amigo.ui.ble;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.BLEActivityComponent;
import com.xiaolian.amigo.di.componet.DaggerBLEActivityComponent;
import com.xiaolian.amigo.di.componet.DaggerOrderActivityComponent;
import com.xiaolian.amigo.di.componet.OrderActivityComponent;
import com.xiaolian.amigo.di.module.BLEActivityModule;
import com.xiaolian.amigo.di.module.OrderActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;


public abstract class BLEBaseActivity extends BaseActivity {

    private BLEActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp();
        mActivityComponent = DaggerBLEActivityComponent.builder()
                .bLEActivityModule(new BLEActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();

    }

    public BLEActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
