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

import com.xiaolian.amigo.di.MainActivityContext;
import com.xiaolian.amigo.di.module.MainActivityModule;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.main.HomeFragment2;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.main.SplashActivity;
import com.xiaolian.amigo.ui.service.BleCountService;

import dagger.Component;

@MainActivityContext
@Component(dependencies = ApplicationComponent.class, modules = MainActivityModule.class)
public interface MainActivityComponent {

    void inject(MainActivity activity);

    void inject(SplashActivity activity);

    void inject(HomeFragment2 fragment2);

    void inject(WebActivity activity);

    void inject(BleCountService service);

}
