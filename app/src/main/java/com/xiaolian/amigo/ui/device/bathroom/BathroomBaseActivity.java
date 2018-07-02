package com.xiaolian.amigo.ui.device.bathroom;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.BathroomActivityComponent;
import com.xiaolian.amigo.di.componet.DaggerBathroomActivityComponent;
import com.xiaolian.amigo.di.module.BathroomActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;

/**
 * @author zcd
 * @date 18/6/29
 */
public abstract class BathroomBaseActivity extends BaseActivity {

    private BathroomActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInject();
    }

    protected void initInject() {
        mActivityComponent = DaggerBathroomActivityComponent.builder()
                .bathroomActivityModule(new BathroomActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();
    }

    public BathroomActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    @Override
    protected void setUp() {

    }
}
