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

package com.xiaolian.amigo.di.componet;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.data.base.LogInterceptor;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.di.ApplicationContext;
import com.xiaolian.amigo.di.BathroomServer;
import com.xiaolian.amigo.di.UserServer;
import com.xiaolian.amigo.di.module.ApplicationModule;
import com.xiaolian.blelib.IBluetoothClient;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;


@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    Application application();

    @UserServer
    Retrofit retrofit();

    @BathroomServer
    Retrofit retrofit2();

    IBluetoothClient bluetoothClient();

    Gson gson();

    ISharedPreferencesHelp sharedPreferencesHelp();

    LogInterceptor logInterceptor();

    void inject(MvpApp app);

}