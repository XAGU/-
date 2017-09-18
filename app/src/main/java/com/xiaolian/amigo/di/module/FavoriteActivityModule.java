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

import com.xiaolian.amigo.data.manager.FavoriteManager;
import com.xiaolian.amigo.data.manager.OrderDataManager;
import com.xiaolian.amigo.data.manager.intf.IFavoriteManager;
import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.di.FavoriteActivityContext;
import com.xiaolian.amigo.di.OrderActivityContext;
import com.xiaolian.amigo.ui.favorite.FavoritePresenter;
import com.xiaolian.amigo.ui.favorite.intf.IFavoritePresenter;
import com.xiaolian.amigo.ui.favorite.intf.IFavoriteView;
import com.xiaolian.amigo.ui.order.OrderPresenter;
import com.xiaolian.amigo.ui.order.intf.IOrderPresenter;
import com.xiaolian.amigo.ui.order.intf.IOrderView;

import dagger.Module;
import dagger.Provides;

@Module
public class FavoriteActivityModule {

    private AppCompatActivity mActivity;

    public FavoriteActivityModule(AppCompatActivity activity) {
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
    @FavoriteActivityContext
    IFavoriteManager provideFavoriteDataManager(FavoriteManager manager) {
        return manager;
    }

    @Provides
    @FavoriteActivityContext
    IFavoritePresenter<IFavoriteView> provideFavoritePresenter(
            FavoritePresenter<IFavoriteView> presenter) {
        return presenter;
    }
}
