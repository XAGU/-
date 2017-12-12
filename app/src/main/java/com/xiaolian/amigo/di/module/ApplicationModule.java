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

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.internal.RxBleLog;
import com.xiaolian.amigo.data.base.LogInterceptor;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.data.prefs.SharedPreferencesHelp;
import com.xiaolian.amigo.di.ApplicationContext;
import com.xiaolian.amigo.util.Constant;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(LogInterceptor logInterceptor) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(logInterceptor)
                .connectTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        return new Retrofit.Builder()
                .baseUrl(Constant.SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
    }

    @Singleton
    @Provides
    LogInterceptor provideLogInterceptor(ISharedPreferencesHelp sharedPreferencesHelp) {
        return new LogInterceptor(sharedPreferencesHelp);
    }

    @Singleton
    @Provides
    Gson providerGson() {
        return new Gson();
    }

    @Singleton
    @Provides
    RxBleClient provideRxBleClient() {
        RxBleClient rxBleClient = RxBleClient.create(mApplication.getApplicationContext());
        RxBleClient.setLogLevel(RxBleLog.DEBUG);
        return rxBleClient;
    }

    @Singleton
    @Provides
    ISharedPreferencesHelp providerSharedPreferencesHelp(SharedPreferencesHelp sharedPreferencesHelp) {
        return sharedPreferencesHelp;
    }
}
