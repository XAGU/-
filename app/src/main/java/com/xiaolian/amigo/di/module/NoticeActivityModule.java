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

import com.xiaolian.amigo.data.manager.NoticeDataManager;
import com.xiaolian.amigo.data.manager.intf.INoticeDataManager;
import com.xiaolian.amigo.di.NoticeActivityContext;
import com.xiaolian.amigo.ui.notice.NoticeDetailPresenter;
import com.xiaolian.amigo.ui.notice.NoticePresenter;
import com.xiaolian.amigo.ui.notice.intf.INoticeDetailPresenter;
import com.xiaolian.amigo.ui.notice.intf.INoticeDetailView;
import com.xiaolian.amigo.ui.notice.intf.INoticePresenter;
import com.xiaolian.amigo.ui.notice.intf.INoticeView;

import dagger.Module;
import dagger.Provides;

@Module
public class NoticeActivityModule {

    private AppCompatActivity mActivity;

    public NoticeActivityModule(AppCompatActivity activity) {
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
    @NoticeActivityContext
    INoticePresenter<INoticeView> provideNoticePresenter(
            NoticePresenter<INoticeView> presenter) {
        return presenter;
    }

    @Provides
    @NoticeActivityContext
    INoticeDetailPresenter<INoticeDetailView> provideNoticeDetailPresenter(
            NoticeDetailPresenter<INoticeDetailView> presenter) {
        return presenter;
    }

    @Provides
    INoticeDataManager provideNoticeDataManager(NoticeDataManager manager) {
        return manager;
    }

}
