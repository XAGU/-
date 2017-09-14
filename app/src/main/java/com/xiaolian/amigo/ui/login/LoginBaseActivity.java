package com.xiaolian.amigo.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerLoginActivityComponent;
import com.xiaolian.amigo.di.componet.LoginActivityComponent;
import com.xiaolian.amigo.di.module.LoginActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;


abstract public class LoginBaseActivity extends BaseActivity {

    private LoginActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponent = DaggerLoginActivityComponent.builder()
                .loginActivityModule(new LoginActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();

    }

    public LoginActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
