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
import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.di.OrderActivityContext;
import com.xiaolian.amigo.ui.order.NormalOrderPresenter;
import com.xiaolian.amigo.ui.order.OrderDetailPresenter;
import com.xiaolian.amigo.ui.order.OrderPresenter;
import com.xiaolian.amigo.ui.order.intf.INormalOrderPresenter;
import com.xiaolian.amigo.ui.order.intf.INormalOrderView;
import com.xiaolian.amigo.ui.order.intf.IOrderDetailPresenter;
import com.xiaolian.amigo.ui.order.intf.IOrderDetailView;
import com.xiaolian.amigo.ui.order.intf.IOrderPresenter;
import com.xiaolian.amigo.ui.order.intf.IOrderView;
import com.xiaolian.amigo.ui.order.intf.IRefundOrderPresenter;
import com.xiaolian.amigo.ui.order.intf.IRefundOrderView;

import dagger.Module;
import dagger.Provides;

@Module
public class OrderActivityModule {

    private AppCompatActivity mActivity;

    public OrderActivityModule(AppCompatActivity activity) {
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
    @OrderActivityContext
    IOrderPresenter<IOrderView> provideOrderPresenter(
            OrderPresenter<IOrderView> presenter) {
        return presenter;
    }

    @Provides
    @OrderActivityContext
    INormalOrderPresenter<INormalOrderView> provideNormalOrderPresenter(
            NormalOrderPresenter<INormalOrderView> presenter) {
        return presenter;
    }

    @Provides
    @OrderActivityContext
    IOrderDetailPresenter<IOrderDetailView> provideOrderDetailPresenter(
            OrderDetailPresenter<IOrderDetailView> presenter) {
        return presenter;
    }

    @Provides
    IOrderDataManager provideOrderDataManager(OrderDataManager manager) {
        return manager;
    }

}
