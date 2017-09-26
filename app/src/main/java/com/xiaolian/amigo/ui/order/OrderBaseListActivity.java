package com.xiaolian.amigo.ui.order;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerOrderActivityComponent;
import com.xiaolian.amigo.di.componet.OrderActivityComponent;
import com.xiaolian.amigo.di.module.OrderActivityModule;
import com.xiaolian.amigo.ui.base.BaseListActivity;
import com.xiaolian.amigo.ui.base.BaseToolBarListActivity;

/**
 * <p>
 * Created by zcd on 9/24/17.
 */

public abstract class OrderBaseListActivity extends BaseToolBarListActivity{
    private OrderActivityComponent mActivityComponent;

    @Override
    protected void initInject() {
        mActivityComponent = DaggerOrderActivityComponent.builder()
                .orderActivityModule(new OrderActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();
    }

    public OrderActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
