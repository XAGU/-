package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerWalletActivityComponent;
import com.xiaolian.amigo.di.componet.WalletActivityComponent;
import com.xiaolian.amigo.di.module.WalletActivityModule;
import com.xiaolian.amigo.ui.base.BaseToolBarActivity;

/**
 * wallet模块base activity
 *
 * @author zcd
 * @date 17/9/18
 */

public abstract class WalletBaseActivity extends BaseToolBarActivity {

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
