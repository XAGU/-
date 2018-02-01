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


import com.xiaolian.amigo.ui.base.intf.IBaseListView;
import com.xiaolian.amigo.ui.order.adaptor.OrderAdaptor;

import java.util.List;

/**
 * 账单
 *
 * @author caidong
 * @date 17/9/15
 */
public interface IOrderView extends IBaseListView {

    /**
     * 刷新消费记录列表
     *
     * @param orders 待添加的订单列表
     */
    void addMore(List<OrderAdaptor.OrderWrapper> orders);
}
