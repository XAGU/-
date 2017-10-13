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


import com.xiaolian.amigo.di.DeviceActivityContext;
import com.xiaolian.amigo.di.module.DeviceActivityModule;
import com.xiaolian.amigo.ui.device.DeviceOrderActivity;
import com.xiaolian.amigo.ui.device.dispenser.ChooseDispenserActivity;
import com.xiaolian.amigo.ui.device.dispenser.DispenserActivity;
import com.xiaolian.amigo.ui.device.heater.HeaterActivity;

import dagger.Component;


@DeviceActivityContext
@Component(dependencies = ApplicationComponent.class, modules = DeviceActivityModule.class)
public interface DeviceActivityComponent {

    void inject(HeaterActivity activity);

    void inject(DispenserActivity activity);

    void inject(DeviceOrderActivity activity);

    void inject(ChooseDispenserActivity activity);

}
