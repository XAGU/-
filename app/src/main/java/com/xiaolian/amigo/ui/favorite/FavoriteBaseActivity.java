package com.xiaolian.amigo.ui.favorite;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerFavoriteActivityComponent;
import com.xiaolian.amigo.di.componet.FavoriteActivityComponent;
import com.xiaolian.amigo.di.module.FavoriteActivityModule;
import com.xiaolian.amigo.ui.base.BaseToolBarListActivity;


public abstract class FavoriteBaseActivity extends BaseToolBarListActivity {

    private FavoriteActivityComponent mActivityComponent;

    @Override
    protected void initInject() {
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
