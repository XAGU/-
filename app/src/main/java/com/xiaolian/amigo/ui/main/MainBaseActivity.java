package com.xiaolian.amigo.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerMainActivityComponent;
import com.xiaolian.amigo.di.componet.MainActivityComponent;
import com.xiaolian.amigo.di.module.MainActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;

/**
 * 主页
 * <p>
 * Created by zcd on 9/20/17.
 */

public abstract class MainBaseActivity extends BaseActivity {

    private MainActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp();
        mActivityComponent = DaggerMainActivityComponent.builder()
                .mainActivityModule(new MainActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();

    }

    public MainActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
