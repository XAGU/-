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

package com.xiaolian.amigo.ui.favorite.intf;


import com.xiaolian.amigo.ui.base.intf.IBaseListView;
import com.xiaolian.amigo.ui.favorite.adaptor.FavoriteAdaptor;

import java.util.List;

/**
 * 收藏页面
 *
 * @author caidong
 * @date 17/9/18
 */
public interface IFavoriteView extends IBaseListView {

    /**
     * 刷新收藏设备
     *
     * @param favorites 收藏设备列表
     */
    void addMore(List<FavoriteAdaptor.FavoriteWrapper> favorites);

    /**
     * 删除收藏设备
     *
     * @param index 待删除的设备在设备列表中的编号
     */
    void deleteOne(int index);
}
