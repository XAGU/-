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
import com.xiaolian.amigo.data.manager.RepairDataManager;
import com.xiaolian.amigo.data.manager.UserDataManager;
import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.data.manager.intf.IRepairDataManager;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.di.OrderActivityContext;
import com.xiaolian.amigo.di.RepairActivityContext;
import com.xiaolian.amigo.ui.order.OrderPresenter;
import com.xiaolian.amigo.ui.order.intf.IOrderPresenter;
import com.xiaolian.amigo.ui.order.intf.IOrderView;
import com.xiaolian.amigo.ui.repair.RepairApplyPresenter;
import com.xiaolian.amigo.ui.repair.RepairDetailPresenter;
import com.xiaolian.amigo.ui.repair.RepairEvaluationPresenter;
import com.xiaolian.amigo.ui.repair.RepairPresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairApplyPresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairApplyView;
import com.xiaolian.amigo.ui.repair.intf.IRepairDetailPresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairDetailView;
import com.xiaolian.amigo.ui.repair.intf.IRepairEvaluationPresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairEvaluationView;
import com.xiaolian.amigo.ui.repair.intf.IRepairPresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairView;

import dagger.Module;
import dagger.Provides;

@Module
public class RepairActivityModule {

    private AppCompatActivity mActivity;

    public RepairActivityModule(AppCompatActivity activity) {
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
    @RepairActivityContext
    IRepairDataManager provideRepairDataManager(RepairDataManager manager) {
        return manager;
    }

    @Provides
    @RepairActivityContext
    IUserDataManager provideUserDataManager(UserDataManager manager) {
        return manager;
    }



    @Provides
    @RepairActivityContext
    IRepairPresenter<IRepairView> provideRepairPresenter(
            RepairPresenter<IRepairView> presenter) {
        return presenter;
    }

    @Provides
    @RepairActivityContext
    IRepairEvaluationPresenter<IRepairEvaluationView> provideRepairEvaluationPresenter(
            RepairEvaluationPresenter<IRepairEvaluationView> presenter) {
        return presenter;
    }

    @Provides
    @RepairActivityContext
    IRepairDetailPresenter<IRepairDetailView> provideRepairDetailPresenter(
            RepairDetailPresenter<IRepairDetailView> presenter) {
        return presenter;
    }

    @Provides
    @RepairActivityContext
    IRepairApplyPresenter<IRepairApplyView> provideRepairApplyPresenter(
            RepairApplyPresenter<IRepairApplyView> presenter) {
        return presenter;
    }
}
