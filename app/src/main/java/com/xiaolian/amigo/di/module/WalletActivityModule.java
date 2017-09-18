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

import com.xiaolian.amigo.data.manager.OrderDataManager;
import com.xiaolian.amigo.data.manager.WalletDataManager;
import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.di.OrderActivityContext;
import com.xiaolian.amigo.di.WalletActivityContext;
import com.xiaolian.amigo.ui.order.OrderPresenter;
import com.xiaolian.amigo.ui.order.intf.IOrderPresenter;
import com.xiaolian.amigo.ui.order.intf.IOrderView;
import com.xiaolian.amigo.ui.wallet.WalletPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWalletPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWalletView;

import dagger.Module;
import dagger.Provides;

@Module
public class WalletActivityModule {

    private AppCompatActivity mActivity;

    public WalletActivityModule(AppCompatActivity activity) {
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
    @WalletActivityContext
    IWalletPresenter<IWalletView> provideWalletPresenter(
            WalletPresenter<IWalletView> presenter) {
        return presenter;
    }

    @Provides
    IWalletDataManager provideWalletDataManager(WalletDataManager manager) {
        return manager;
    }

}
