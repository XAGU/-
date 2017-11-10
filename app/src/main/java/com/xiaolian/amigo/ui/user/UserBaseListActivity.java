package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerUserActivityComponent;
import com.xiaolian.amigo.di.componet.UserActivityComponent;
import com.xiaolian.amigo.di.module.UserActivityModule;
import com.xiaolian.amigo.ui.base.BaseToolBarListActivity;

/**
 * <p>
 * Created by zcd on 9/25/17.
 */

public abstract class UserBaseListActivity extends BaseToolBarListActivity {

    private UserActivityComponent mActivityComponent;

    @Override
    protected void setUp() {

    }

    protected  void initInject() {
        mActivityComponent = DaggerUserActivityComponent.builder()
                .userActivityModule(new UserActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();
    }

    public UserActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
