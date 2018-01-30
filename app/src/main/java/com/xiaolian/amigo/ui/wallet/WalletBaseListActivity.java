package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerWalletActivityComponent;
import com.xiaolian.amigo.di.componet.WalletActivityComponent;
import com.xiaolian.amigo.di.module.WalletActivityModule;
import com.xiaolian.amigo.ui.base.BaseToolBarListActivity;

/**
 * wallet模块base list activity
 *
 * @author zcd
 * @date 17/9/24
 */

public abstract class WalletBaseListActivity extends BaseToolBarListActivity {
    private WalletActivityComponent mActivityComponent;

    @Override
    protected void initInject() {
        mActivityComponent = DaggerWalletActivityComponent.builder()
                .walletActivityModule(new WalletActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();
    }

    public WalletActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
