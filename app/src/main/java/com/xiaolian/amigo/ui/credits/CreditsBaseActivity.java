package com.xiaolian.amigo.ui.credits;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.CreditsActivityComponent;
import com.xiaolian.amigo.di.componet.DaggerCreditsActivityComponent;
import com.xiaolian.amigo.di.module.CreditsActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;

/**
 * 积分模块base activity
 *
 * @author zcd
 * @date 18/2/23
 */

public abstract class CreditsBaseActivity extends BaseActivity {

    private CreditsActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponent = DaggerCreditsActivityComponent.builder()
                .creditsActivityModule(new CreditsActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();
        setUp();
    }


    public CreditsActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
