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

import com.xiaolian.amigo.data.manager.BleDataManager;
import com.xiaolian.amigo.data.manager.ClientServiceDataManager;
import com.xiaolian.amigo.data.manager.DeviceDataManager;
import com.xiaolian.amigo.data.manager.FavoriteManager;
import com.xiaolian.amigo.data.manager.OrderDataManager;
import com.xiaolian.amigo.data.manager.WalletDataManager;
import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
import com.xiaolian.amigo.data.manager.intf.IClientServiceDataManager;
import com.xiaolian.amigo.data.manager.intf.IDeviceDataManager;
import com.xiaolian.amigo.data.manager.intf.IFavoriteManager;
import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.di.DeviceActivityContext;
import com.xiaolian.amigo.ui.device.DeviceOrderPresenter;
import com.xiaolian.amigo.ui.device.dispenser.ChooseDispenserPresenter;
import com.xiaolian.amigo.ui.device.dispenser.DispenserPresenter;
import com.xiaolian.amigo.ui.device.dryer.DryerPresenter;
import com.xiaolian.amigo.ui.device.intf.IDeviceOrderPresenter;
import com.xiaolian.amigo.ui.device.intf.IDeviceOrderView;
import com.xiaolian.amigo.ui.device.intf.dispenser.IChooseDispenerView;
import com.xiaolian.amigo.ui.device.intf.dispenser.IChooseDispenserPresenter;
import com.xiaolian.amigo.ui.device.intf.dispenser.IDispenserPresenter;
import com.xiaolian.amigo.ui.device.intf.dispenser.IDispenserView;
import com.xiaolian.amigo.ui.device.intf.dryer.IDryerPresenter;
import com.xiaolian.amigo.ui.device.intf.dryer.IDryerView;
import com.xiaolian.amigo.ui.device.heater.HeaterPresenter;
import com.xiaolian.amigo.ui.device.intf.heator.IHeaterPresenter;
import com.xiaolian.amigo.ui.device.intf.heator.IHeaterView;

import dagger.Module;
import dagger.Provides;

@Module
public class DeviceActivityModule {

    private AppCompatActivity mActivity;

    public DeviceActivityModule(AppCompatActivity activity) {
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
    @DeviceActivityContext
    IBleDataManager provideBleDataManager(BleDataManager manager) {
        return manager;
    }

    @Provides
    @DeviceActivityContext
    IHeaterPresenter<IHeaterView> provideHeaterPresenter(HeaterPresenter<IHeaterView> presenter) {
        return presenter;
    }

    @Provides
    @DeviceActivityContext
    IDispenserPresenter<IDispenserView> provideDispenserPresenter(DispenserPresenter<IDispenserView> presenter) {
        return presenter;
    }

    @Provides
    @DeviceActivityContext
    IDryerPresenter<IDryerView> provideDryerPresenter(DryerPresenter<IDryerView> presenter) {
        return presenter;
    }

    @Provides
    @DeviceActivityContext
    IChooseDispenserPresenter<IChooseDispenerView> provideChooseDispenserPresenter(ChooseDispenserPresenter<IChooseDispenerView> presenter) {
        return presenter;
    }

    @Provides
    IDeviceDataManager provideDeviceDataManager(DeviceDataManager manager) {
        return manager;
    }

    @Provides
    @DeviceActivityContext
    IOrderDataManager provideOrderDataManager(OrderDataManager manager) {
        return manager;
    }

    @Provides
    @DeviceActivityContext
    IWalletDataManager provideWalletDataManager(WalletDataManager manager) {
        return manager;
    }

    @Provides
    @DeviceActivityContext
    IFavoriteManager provideFavoriteDataManager(FavoriteManager manager) {
        return manager;
    }

    @Provides
    @DeviceActivityContext
    IClientServiceDataManager provideClientServiceDataManager(ClientServiceDataManager manager) {
        return manager;
    }

    @Provides
    @DeviceActivityContext
    IDeviceOrderPresenter<IDeviceOrderView> provideDeviceOrderPresenter(DeviceOrderPresenter<IDeviceOrderView> presenter) {
        return presenter;
    }
}
