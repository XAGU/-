package com.xiaolian.amigo.ui.favorite;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerFavoriteActivityComponent;
import com.xiaolian.amigo.di.componet.DaggerOrderActivityComponent;
import com.xiaolian.amigo.di.componet.FavoriteActivityComponent;
import com.xiaolian.amigo.di.componet.OrderActivityComponent;
import com.xiaolian.amigo.di.module.FavoriteActivityModule;
import com.xiaolian.amigo.di.module.OrderActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;


public abstract class FavoriteBaseActivity extends BaseActivity {

    private FavoriteActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp();
        mActivityComponent = DaggerFavoriteActivityComponent.builder()
                .favoriteActivityModule(new FavoriteActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();

    }

    public FavoriteActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
