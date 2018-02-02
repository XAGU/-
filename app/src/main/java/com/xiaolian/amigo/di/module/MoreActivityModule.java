package com.xiaolian.amigo.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.xiaolian.amigo.data.manager.MainDataManager;
import com.xiaolian.amigo.data.manager.MoreDataManager;
import com.xiaolian.amigo.data.manager.intf.IMainDataManager;
import com.xiaolian.amigo.data.manager.intf.IMoreDataManager;
import com.xiaolian.amigo.di.MoreActivityContext;
import com.xiaolian.amigo.ui.more.AboutUsPresenter;
import com.xiaolian.amigo.ui.more.MorePresenter;
import com.xiaolian.amigo.ui.more.intf.IAboutUsPresenter;
import com.xiaolian.amigo.ui.more.intf.IAboutUsView;
import com.xiaolian.amigo.ui.more.intf.IMorePresenter;
import com.xiaolian.amigo.ui.more.intf.IMoreView;

import dagger.Module;
import dagger.Provides;

/**
 * MoreActivityModule
 *
 * @author zcd
 */
@Module
public class MoreActivityModule {
    private AppCompatActivity mActivity;

    public MoreActivityModule(AppCompatActivity activity) {
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
    @MoreActivityContext
    IMorePresenter<IMoreView> provideMorePresenter(
            MorePresenter<IMoreView> presenter) {
        return presenter;
    }

    @Provides
    @MoreActivityContext
    IAboutUsPresenter<IAboutUsView> provideAboutUsPresenter(
            AboutUsPresenter<IAboutUsView> presenter) {
        return presenter;
    }

    @Provides
    IMoreDataManager provideMoreDataManager(MoreDataManager manager) {
        return manager;
    }

    @Provides
    IMainDataManager provideMainDataManager(MainDataManager manager) {
        return manager;
    }
}
