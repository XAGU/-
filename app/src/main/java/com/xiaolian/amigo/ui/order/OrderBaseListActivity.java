package com.xiaolian.amigo.ui.order;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerOrderActivityComponent;
import com.xiaolian.amigo.di.componet.OrderActivityComponent;
import com.xiaolian.amigo.di.module.OrderActivityModule;
import com.xiaolian.amigo.ui.base.BaseToolBarListActivity;

/**
 * 订单模块列表base activity
 *
 * @author zcd
 * @date 17/9/24
 */

public abstract class OrderBaseListActivity extends BaseToolBarListActivity {
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
