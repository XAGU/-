package com.xiaolian.amigo.ui.lostandfound;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerLostAndFoundActivityComponent;
import com.xiaolian.amigo.di.componet.LostAndFoundActivityComponent;
import com.xiaolian.amigo.di.module.LostAndFoundActivityModule;
import com.xiaolian.amigo.ui.base.BaseToolBarListActivity;

/**
 * 失物招领
 *
 * @author zcd
 * @date 17/9/18
 */

public abstract class LostAndFoundBaseListActivity extends BaseToolBarListActivity {
    @Override
    protected void setUp() {

    }

    private LostAndFoundActivityComponent mActivityComponent;

    @Override
    protected void initInject() {
        mActivityComponent = DaggerLostAndFoundActivityComponent.builder()
                .lostAndFoundActivityModule(new LostAndFoundActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();
    }

    public LostAndFoundActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

}
