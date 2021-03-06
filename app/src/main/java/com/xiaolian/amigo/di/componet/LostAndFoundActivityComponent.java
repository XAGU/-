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


import com.xiaolian.amigo.di.LostAndFoundActivityContext;
import com.xiaolian.amigo.di.module.LostAndFoundActivityModule;
import com.xiaolian.amigo.ui.lostandfound.BlogFragment;
import com.xiaolian.amigo.ui.lostandfound.LostAndFoundActivity;
import com.xiaolian.amigo.ui.lostandfound.LostAndFoundActivity2;
import com.xiaolian.amigo.ui.lostandfound.LostAndFoundDetailActivity;
import com.xiaolian.amigo.ui.lostandfound.LostAndFoundDetailActivity2;
import com.xiaolian.amigo.ui.lostandfound.LostAndFoundNoticeActivity;
import com.xiaolian.amigo.ui.lostandfound.LostAndFoundReplyDetailActivity;
import com.xiaolian.amigo.ui.lostandfound.MyCollectActivity;
import com.xiaolian.amigo.ui.lostandfound.MyPublishActivity;
import com.xiaolian.amigo.ui.lostandfound.MyPublishActivity2;
import com.xiaolian.amigo.ui.lostandfound.PublishFoundActivity;
import com.xiaolian.amigo.ui.lostandfound.PublishLostActivity;
import com.xiaolian.amigo.ui.lostandfound.PublishLostAndFoundActivity;
import com.xiaolian.amigo.ui.lostandfound.SocalFragment;
import com.xiaolian.amigo.ui.lostandfound.WriteLZActivity;

import dagger.Component;


@LostAndFoundActivityContext
@Component(dependencies = ApplicationComponent.class, modules = LostAndFoundActivityModule.class)
public interface LostAndFoundActivityComponent {

    void inject(LostAndFoundActivity activity);

    void inject(LostAndFoundActivity2 activity);

    void inject(LostAndFoundDetailActivity activity);

    void inject(MyPublishActivity activity);

    void inject(PublishLostActivity activity);

    void inject(PublishFoundActivity activity);

    void inject(PublishLostAndFoundActivity activity);

    void inject(LostAndFoundDetailActivity2 activity);

    void inject(LostAndFoundReplyDetailActivity activity);

    void inject(MyPublishActivity2 activity);

    void inject(LostAndFoundNoticeActivity activity);

    void inject(MyCollectActivity activity);

    void inject(WriteLZActivity activity);

    void inject(SocalFragment socalFragment);

    void inject(BlogFragment blogFragment);
}
