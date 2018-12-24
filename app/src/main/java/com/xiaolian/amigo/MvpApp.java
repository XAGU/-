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

package com.xiaolian.amigo;

import android.app.Application;
import android.content.Context;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xiaolian.amigo.di.componet.ApplicationComponent;
import com.xiaolian.amigo.di.componet.DaggerApplicationComponent;
import com.xiaolian.amigo.di.module.ApplicationModule;
import com.xiaolian.amigo.ui.base.swipeback.ActivityLifecycleHelper;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;
import com.xiaolian.amigo.util.crash.MyHttpSenderFactory;
import com.xiaolian.amigo.util.crash.acra.ACRA;
import com.xiaolian.amigo.util.crash.acra.ReportField;
import com.xiaolian.amigo.util.crash.acra.ReportingInteractionMode;
import com.xiaolian.amigo.util.crash.acra.config.ACRAConfiguration;
import com.xiaolian.amigo.util.crash.acra.config.ACRAConfigurationException;
import com.xiaolian.amigo.util.crash.acra.config.ConfigurationBuilder;
import com.xiaolian.amigo.util.crash.acra.sender.HttpSender;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MvpApp extends Application {


    private ApplicationComponent mApplicationComponent;

    private static Context context ;
    public static IWXAPI mWxApi;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            final ACRAConfiguration config = new ConfigurationBuilder(this)
                    .setFormUri(Constant.SERVER + "/android/crash/add")
                    .setReportType(HttpSender.Type.JSON)
                    .setHttpMethod(HttpSender.Method.POST)
                    .setReportSenderFactoryClasses(MyHttpSenderFactory.class)
                    .setReportingInteractionMode(ReportingInteractionMode.TOAST)
                    .setResToastText(R.string.crash_toast_text)
                    .setCustomReportContent(
                            ReportField.REPORT_ID,
                            ReportField.DEVICE_ID,
                            ReportField.APP_VERSION_CODE,
                            ReportField.APP_VERSION_NAME,
                            ReportField.PACKAGE_NAME,
                            ReportField.PHONE_MODEL,
                            ReportField.ANDROID_VERSION,
                            ReportField.BRAND,
                            ReportField.PRODUCT,
                            ReportField.TOTAL_MEM_SIZE,
                            ReportField.AVAILABLE_MEM_SIZE,
                            ReportField.BUILD_CONFIG,
                            ReportField.INITIAL_CONFIGURATION,
                            ReportField.STACK_TRACE,
                            ReportField.CRASH_CONFIGURATION,
                            ReportField.DISPLAY,
                            ReportField.DROPBOX,
                            ReportField.LOGCAT,
                            ReportField.DEVICE_FEATURES,
                            ReportField.EVENTSLOG,
                            ReportField.SHARED_PREFERENCES,
                            ReportField.SETTINGS_GLOBAL,
                            ReportField.SETTINGS_SECURE,
                            ReportField.SETTINGS_SYSTEM
                            )
                    .build();
            ACRA.init(this, config);
        } catch (ACRAConfigurationException e) {
            Log.e("MvpApp", "ACRA初始化失败");
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        Log.setContext(this.getApplicationContext());
        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build());
//        android.util.Log.e("test", "onCreate: " + System.currentTimeMillis() );
//        CrashHandler.getInstance().init(this, BuildConfig.DEBUG, true, 0, SplashActivity.class);

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();

        mApplicationComponent.inject(this);

        registToWX();
        closeAndroidPDialog();
    }


    public static Context getContext(){
        return context ;
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }

    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }

    private void registToWX() {
        mWxApi = WXAPIFactory.createWXAPI(this, Constant.WECHAT_APP_ID, false);
        mWxApi.registerApp(Constant.WECHAT_APP_ID);
    }

    private void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
