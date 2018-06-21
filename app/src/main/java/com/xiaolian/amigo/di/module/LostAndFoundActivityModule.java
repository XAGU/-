/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

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
import com.xiaolian.amigo.ui.lostandfound.PublishLostAndFoundPresenter;
import com.xiaolian.amigo.ui.lostandfound.PublishLostPresenter;
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
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostAndFoundPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostAndFoundView;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostView;

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

}
