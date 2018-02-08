package com.xiaolian.amigo.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.xiaolian.amigo.data.manager.BonusDataManager;
import com.xiaolian.amigo.data.manager.intf.IBonusDataManager;
import com.xiaolian.amigo.di.BonusActivityContext;
import com.xiaolian.amigo.ui.bonus.BonusExchangePresenter;
import com.xiaolian.amigo.ui.bonus.BonusPresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusExchangePresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusExchangeView;
import com.xiaolian.amigo.ui.bonus.intf.IBonusPresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusView;

import dagger.Module;
import dagger.Provides;

/**
 * BonusActivityModule
 *
 * @author zcd
 */
@Module
public class BonusActivityModule {
    private AppCompatActivity mActivity;

    public BonusActivityModule(AppCompatActivity activity) {
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
    @BonusActivityContext
    IBonusPresenter<IBonusView> provideBonusPresenter(
            BonusPresenter<IBonusView> presenter) {
        return presenter;
    }

    @Provides
    @BonusActivityContext
    IBonusExchangePresenter<IBonusExchangeView> provideBonusExchangePresenter(
            BonusExchangePresenter<IBonusExchangeView> presenter) {
        return presenter;
    }

    @Provides
    IBonusDataManager provideBonusDataManager(BonusDataManager manager) {
        return manager;
    }
}
