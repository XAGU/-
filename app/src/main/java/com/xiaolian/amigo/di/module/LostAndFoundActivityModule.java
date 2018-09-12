
package com.xiaolian.amigo.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.xiaolian.amigo.data.manager.LostAndFoundDataManager;
import com.xiaolian.amigo.data.manager.OssDataManager;
import com.xiaolian.amigo.data.manager.UserDataManager;
import com.xiaolian.amigo.data.manager.intf.ILostAndFoundDataManager;
import com.xiaolian.amigo.data.manager.intf.IOssDataManager;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.di.LostAndFoundActivityContext;
import com.xiaolian.amigo.ui.lostandfound.LostAndFoundDetailPresenter;
import com.xiaolian.amigo.ui.lostandfound.LostAndFoundDetailPresenter2;
import com.xiaolian.amigo.ui.lostandfound.LostAndFoundNoticePresenter;
import com.xiaolian.amigo.ui.lostandfound.LostAndFoundPresenter;
import com.xiaolian.amigo.ui.lostandfound.LostAndFoundPresenter2;
import com.xiaolian.amigo.ui.lostandfound.LostAndFoundReplyDetailPresenter;
import com.xiaolian.amigo.ui.lostandfound.MyCollectPresenter;
import com.xiaolian.amigo.ui.lostandfound.PublishLostAndFoundPresenter;
import com.xiaolian.amigo.ui.lostandfound.PublishLostPresenter;
import com.xiaolian.amigo.ui.lostandfound.SocalPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundDetailPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundDetailPresenter2;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundDetailView;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundDetailView2;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundNoticePresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundNoticeView;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundPresenter2;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundReplyDetailPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundReplyDetailView;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundView;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundView2;
import com.xiaolian.amigo.ui.lostandfound.intf.IMyCollectPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IMyCollectView;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostAndFoundPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostAndFoundView;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostView;
import com.xiaolian.amigo.ui.lostandfound.intf.ISocalPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ISocalView;

import dagger.Module;
import dagger.Provides;

@Module
public class LostAndFoundActivityModule {

    private AppCompatActivity mActivity;

    public LostAndFoundActivityModule(AppCompatActivity activity) {
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
    @LostAndFoundActivityContext
    ILostAndFoundPresenter<ILostAndFoundView> provideLostAndFoundPresenter(
            LostAndFoundPresenter<ILostAndFoundView> presenter) {
        return presenter;
    }

    @Provides
    @LostAndFoundActivityContext
    ILostAndFoundPresenter2<ILostAndFoundView2> provideLostAndFoundPresenter2(
            LostAndFoundPresenter2<ILostAndFoundView2> presenter) {
        return presenter;
    }

    @Provides
    @LostAndFoundActivityContext
    ILostAndFoundDetailPresenter<ILostAndFoundDetailView> provideLostAndFoundDetailPresenter(
            LostAndFoundDetailPresenter<ILostAndFoundDetailView> presenter) {
        return presenter;
    }

    @Provides
    @LostAndFoundActivityContext
    IPublishLostPresenter<IPublishLostView> providePublishLostPresenter(
            PublishLostPresenter<IPublishLostView> presenter) {
        return presenter;
    }

    @Provides
    @LostAndFoundActivityContext
    IPublishLostAndFoundPresenter<IPublishLostAndFoundView> providePublishLostAndFoundPresenter(
            PublishLostAndFoundPresenter<IPublishLostAndFoundView> presenter) {
        return presenter;
    }

    @Provides
    @LostAndFoundActivityContext
    ILostAndFoundReplyDetailPresenter<ILostAndFoundReplyDetailView> provideLostAndFoundReplyDetailPresenter(
            LostAndFoundReplyDetailPresenter<ILostAndFoundReplyDetailView> presenter) {
        return presenter;
    }

    @Provides
    @LostAndFoundActivityContext
    ILostAndFoundDetailPresenter2<ILostAndFoundDetailView2> provideLostAndFoundDetailPresenter2(
            LostAndFoundDetailPresenter2<ILostAndFoundDetailView2> presenter) {
        return presenter;
    }

    @Provides
    @LostAndFoundActivityContext
    ILostAndFoundNoticePresenter<ILostAndFoundNoticeView> provideLostAndFoundNoticePresenter(
            LostAndFoundNoticePresenter<ILostAndFoundNoticeView> presenter) {
        return presenter;
    }

    @Provides
    @LostAndFoundActivityContext
    IMyCollectPresenter<IMyCollectView> provideMyCollectPresenter(
            MyCollectPresenter<IMyCollectView> presenter) {
        return presenter;
    }

    @Provides
    ILostAndFoundDataManager provideLostAndFoundDataManager(LostAndFoundDataManager manager) {
        return manager;
    }

    @Provides
    IUserDataManager provideUserDataManager(UserDataManager manager) {
        return manager;
    }

    @Provides
    IOssDataManager provideOssDataManager(OssDataManager manager) {
        return manager;
    }

    @Provides
    @LostAndFoundActivityContext
    ISocalPresenter<ISocalView> provideSocalPresenter(
            SocalPresenter<ISocalView> presenter) {
        return presenter;
    }

}
