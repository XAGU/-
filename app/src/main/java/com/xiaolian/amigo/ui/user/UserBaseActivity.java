package com.xiaolian.amigo.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerUserActivityComponent;
import com.xiaolian.amigo.di.componet.UserActivityComponent;
import com.xiaolian.amigo.di.module.UserActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;
import com.xiaolian.amigo.ui.base.BaseToolBarActivity;

/**
 * 编辑个人信息BaseActivity
 * @author zcd
 */

public abstract class UserBaseActivity extends BaseToolBarActivity {

    private UserActivityComponent mActivityComponent;

    @Override
    protected void initInject() {
        mActivityComponent = DaggerUserActivityComponent.builder()
                .userActivityModule(new UserActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();
    }

    @Override
    protected void setUp() {

    }

    public UserActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
