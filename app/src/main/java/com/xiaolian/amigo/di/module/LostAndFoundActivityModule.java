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
import com.xiaolian.amigo.data.manager.OrderDataManager;
import com.xiaolian.amigo.data.manager.UserDataManager;
import com.xiaolian.amigo.data.manager.intf.ILostAndFoundDataManager;
import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.di.LostAndFoundActivityContext;
import com.xiaolian.amigo.di.OrderActivityContext;
import com.xiaolian.amigo.ui.lostandfound.LostAndFoundDetailPresenter;
import com.xiaolian.amigo.ui.lostandfound.LostAndFoundPresenter;
import com.xiaolian.amigo.ui.lostandfound.PublishLostPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundDetailPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundDetailView;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundView;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostView;
import com.xiaolian.amigo.ui.order.OrderPresenter;
import com.xiaolian.amigo.ui.order.intf.IOrderPresenter;
import com.xiaolian.amigo.ui.order.intf.IOrderView;

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
    ILostAndFoundDataManager provideLostAndFoundDataManager(LostAndFoundDataManager manager) {
        return manager;
    }

    @Provides
    IUserDataManager provideUserDataManager(UserDataManager manager) {
        return manager;
    }

}
