package com.xiaolian.amigo.ui.more;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerMoreActivityComponent;
import com.xiaolian.amigo.di.componet.MoreActivityComponent;
import com.xiaolian.amigo.di.module.MoreActivityModule;
import com.xiaolian.amigo.ui.base.BaseToolBarActivity;

/**
 * 更多
 *
 * @author zcd
 * @date 17/10/13
 */

public abstract class MoreBaseActivity extends BaseToolBarActivity {

    private MoreActivityComponent mActivityComponent;

    @Override
    protected void initInject() {
        mActivityComponent = DaggerMoreActivityComponent.builder()
                .moreActivityModule(new MoreActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();

    }

    public MoreActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
