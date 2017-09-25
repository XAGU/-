package com.xiaolian.amigo.ui.wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerWalletActivityComponent;
import com.xiaolian.amigo.di.componet.WalletActivityComponent;
import com.xiaolian.amigo.di.module.WalletActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;
import com.xiaolian.amigo.ui.base.BaseToolBarActivity;

/**
 * <p>
 * Created by zcd on 9/18/17.
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

    @Override
    protected void setUp() {

    }

    public WalletActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
