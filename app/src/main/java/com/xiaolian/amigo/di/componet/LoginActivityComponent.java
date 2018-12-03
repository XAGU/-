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


import com.xiaolian.amigo.di.LoginActivityContext;
import com.xiaolian.amigo.di.module.LoginActivityModule;
import com.xiaolian.amigo.ui.login.LoginActivity;
import com.xiaolian.amigo.ui.login.PasswordRetrievalStep1Activity;
import com.xiaolian.amigo.ui.login.PasswordRetrievalStep2Activity;
import com.xiaolian.amigo.ui.user.ThirdBindActivity;

import dagger.Component;


@LoginActivityContext
@Component(dependencies = ApplicationComponent.class, modules = LoginActivityModule.class)
public interface LoginActivityComponent {

    void inject(LoginActivity activity);

    void inject(PasswordRetrievalStep1Activity activity);

    void inject(PasswordRetrievalStep2Activity activity);

}
