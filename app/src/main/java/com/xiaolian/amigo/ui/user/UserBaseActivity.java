package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerUserActivityComponent;
import com.xiaolian.amigo.di.componet.UserActivityComponent;
import com.xiaolian.amigo.di.module.UserActivityModule;
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


    public UserActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
