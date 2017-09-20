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


import com.xiaolian.amigo.di.WalletActivityContext;
import com.xiaolian.amigo.di.module.WalletActivityModule;
import com.xiaolian.amigo.ui.wallet.RechargeActivity;
import com.xiaolian.amigo.ui.wallet.WalletActivity;

import dagger.Component;


@WalletActivityContext
@Component(dependencies = ApplicationComponent.class, modules = WalletActivityModule.class)
public interface WalletActivityComponent {

    void inject(WalletActivity activity);

    void inject(RechargeActivity activity);
}
