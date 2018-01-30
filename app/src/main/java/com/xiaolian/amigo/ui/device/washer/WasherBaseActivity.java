package com.xiaolian.amigo.ui.device.washer;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerWasherActivityComponent;
import com.xiaolian.amigo.di.componet.WasherActivityComponent;
import com.xiaolian.amigo.di.module.WasherActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;

/**
 * 洗衣机模块base activity
 *
 * @author zcd
 * @date 1/12/18
 */

public abstract class WasherBaseActivity extends BaseActivity {

    private WasherActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInject();
    }

    protected void initInject() {
        mActivityComponent = DaggerWasherActivityComponent.builder()
                .washerActivityModule(new WasherActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();
    }

    public WasherActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
