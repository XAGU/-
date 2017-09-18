package com.xiaolian.amigo.ui.lostandfound;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerLostAndFoundActivityComponent;
import com.xiaolian.amigo.di.componet.LostAndFoundActivityComponent;
import com.xiaolian.amigo.di.module.LostAndFoundActivityModule;
import com.xiaolian.amigo.ui.base.BaseListActivity;

/**
 * 失物招领
 * <p>
 * Created by zcd on 9/18/17.
 */

public abstract class LostAndFoundBaseListActivity extends BaseListActivity {
    @Override
    protected void setUp() {

    }

    private LostAndFoundActivityComponent mActivityComponent;

    protected  void initInject() {
        mActivityComponent = DaggerLostAndFoundActivityComponent.builder()
                .lostAndFoundActivityModule(new LostAndFoundActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();
    }

    public LostAndFoundActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

}
