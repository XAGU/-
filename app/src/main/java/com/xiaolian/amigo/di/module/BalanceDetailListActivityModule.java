package com.xiaolian.amigo.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;


import com.xiaolian.amigo.data.manager.WalletDataManager;
import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.di.BalanceDetailListActivityContext;
import com.xiaolian.amigo.ui.wallet.BalanceDetailListPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IBalanceDetailListPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IBalanceDetailListView;

import dagger.Module;
import dagger.Provides;

@Module
public class BalanceDetailListActivityModule {
    private AppCompatActivity mActivity;

    public BalanceDetailListActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @Provides
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    @BalanceDetailListActivityContext
    IBalanceDetailListPresenter<IBalanceDetailListView> provideBalanceDetailListPresenter(
            BalanceDetailListPresenter<IBalanceDetailListView> presenter) {
        return presenter;
    }

    @Provides
    IWalletDataManager provideWalletDataManager(WalletDataManager manager) {
        return manager;
    }
}
