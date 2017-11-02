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

package com.xiaolian.amigo.ui.order.intf;


import com.xiaolian.amigo.di.LoginActivityContext;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

@LoginActivityContext
public interface IOrderPresenter<V extends IOrderView> extends IBasePresenter<V> {

    // 刷新个人消费账单
    void requestOrders(int page);

    String getToken();
}
