package com.xiaolian.amigo.ui.lostandfound;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerLostAndFoundActivityComponent;
import com.xiaolian.amigo.di.componet.LostAndFoundActivityComponent;
import com.xiaolian.amigo.di.module.LostAndFoundActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;

/**
 * 失物招领
 * <p>
 * Created by zcd on 9/18/17.
 */
public abstract class LostAndFoundBaseActivity extends BaseActivity {
    private LostAndFoundActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp();
        mActivityComponent = DaggerLostAndFoundActivityComponent.builder()
                .lostAndFoundActivityModule(new LostAndFoundActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();

    }

    public LostAndFoundActivityComponent getActivityComponent() {
        return mActivityComponent;
    }


}
