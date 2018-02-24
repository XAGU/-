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


import com.xiaolian.amigo.di.PointActivityContext;
import com.xiaolian.amigo.di.WalletActivityContext;
import com.xiaolian.amigo.di.module.PointActivityModule;
import com.xiaolian.amigo.di.module.WalletActivityModule;
import com.xiaolian.amigo.ui.point.PointActivity;
import com.xiaolian.amigo.ui.wallet.AddAccountActivity;
import com.xiaolian.amigo.ui.wallet.ChooseWithdrawActivity;
import com.xiaolian.amigo.ui.wallet.PrepayActivity;
import com.xiaolian.amigo.ui.wallet.PrepayOrderActivity;
import com.xiaolian.amigo.ui.wallet.RechargeActivity;
import com.xiaolian.amigo.ui.wallet.RechargeDetailActivity;
import com.xiaolian.amigo.ui.wallet.WalletActivity;
import com.xiaolian.amigo.ui.wallet.WithdrawalActivity;
import com.xiaolian.amigo.ui.wallet.WithdrawalDetailActivity;
import com.xiaolian.amigo.ui.wallet.WithdrawalRecordActivity;

import dagger.Component;


@PointActivityContext
@Component(dependencies = ApplicationComponent.class, modules = PointActivityModule.class)
public interface PointActivityComponent {
    void inject(PointActivity activity);
}
