package com.xiaolian.amigo.ui.point;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerPointActivityComponent;
import com.xiaolian.amigo.di.componet.PointActivityComponent;
import com.xiaolian.amigo.di.module.PointActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;

/**
 * 积分模块base activity
 *
 * @author zcd
 * @date 18/2/23
 */

public abstract class PointBaseActivity extends BaseActivity {

    private PointActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponent = DaggerPointActivityComponent.builder()
                .pointActivityModule(new PointActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();
        setUp();
    }


    public PointActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
