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

import com.xiaolian.amigo.data.manager.BLEDataManager;
import com.xiaolian.amigo.data.manager.intf.IBLEDataManager;
import com.xiaolian.amigo.di.BLEActivityContext;
import com.xiaolian.amigo.di.BonusActivityContext;
import com.xiaolian.amigo.ui.ble.intf.BLEPresenter;
import com.xiaolian.amigo.ui.ble.intf.IBLEPresenter;
import com.xiaolian.amigo.ui.ble.intf.IBLEView;
import com.xiaolian.amigo.ui.bonus.BonusExchangePresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusExchangePresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusExchangeView;

import dagger.Module;
import dagger.Provides;

@Module
public class BLEActivityModule {

    private AppCompatActivity mActivity;

    public BLEActivityModule(AppCompatActivity activity) {
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
    @BLEActivityContext
    IBLEDataManager provideBLEDataManager(BLEDataManager manager) {
        return manager;
    }

    @Provides
    @BLEActivityContext
    IBLEPresenter<IBLEView> provideBLEPresenter(BLEPresenter<IBLEView> presenter) {
        return presenter;
    }
}
