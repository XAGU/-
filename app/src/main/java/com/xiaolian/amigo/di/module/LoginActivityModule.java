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

import com.xiaolian.amigo.data.manager.intf.ILoginDataManager;
import com.xiaolian.amigo.data.manager.LoginDataManager;
import com.xiaolian.amigo.di.LoginActivityContext;
import com.xiaolian.amigo.ui.login.PasswordRetrievalStep1Presenter;
import com.xiaolian.amigo.ui.login.PasswordRetrievalStep2Presenter;
import com.xiaolian.amigo.ui.login.intf.ILoginPresenter;
import com.xiaolian.amigo.ui.login.intf.ILoginView;
import com.xiaolian.amigo.ui.login.LoginPresenter;
import com.xiaolian.amigo.ui.login.intf.IPasswordRetrievalStep1Presenter;
import com.xiaolian.amigo.ui.login.intf.IPasswordRetrievalStep1View;
import com.xiaolian.amigo.ui.login.intf.IPasswordRetrievalStep2Presenter;
import com.xiaolian.amigo.ui.login.intf.IPasswordRetrievalStep2View;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginActivityModule {

    private AppCompatActivity mActivity;

    public LoginActivityModule(AppCompatActivity activity)
    {
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
    @LoginActivityContext
    ILoginPresenter<ILoginView> provideLoginPresenter(
            LoginPresenter<ILoginView> presenter) {
        return presenter;
    }

    @Provides
    @LoginActivityContext
    IPasswordRetrievalStep1Presenter<IPasswordRetrievalStep1View> providePasswordRetrievalStep1Presenter(
            PasswordRetrievalStep1Presenter<IPasswordRetrievalStep1View> presenter) {
        return presenter;
    }

    @Provides
    @LoginActivityContext
    IPasswordRetrievalStep2Presenter<IPasswordRetrievalStep2View> providePasswordRetrievalStep2Presenter(
            PasswordRetrievalStep2Presenter<IPasswordRetrievalStep2View> presenter) {
        return presenter;
    }

    @Provides
    ILoginDataManager provideLoginDataManager(LoginDataManager manager) {
        return manager;
    }

}
