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

package com.xiaolian.amigo.ui.repair.intf;


import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.di.RepairActivityContext;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 报修详情
 *
 * @author caidong
 * @date 17/9/19
 */
@RepairActivityContext
public interface IRepairDetailPresenter<V extends IRepairDetailView> extends IBasePresenter<V> {

    /**
     * 查询报修单详情
     *
     * @param id 报修id
     */
    void requestRepailDetail(Long id);

    /**
     * 提醒客服
     *
     * @param sourceId 来源id
     */
    void remind(Long sourceId);

    /**
     * 获取deivce
     *
     * @return device
     */
    Device getDevice();

    /**
     * 取消报修
     */
    void cancelRepair();

    /**
     * 获取Token
     */
    void getToken();


    /**
     * 弹框提示
     */
    public void notShowRemindAlert();

    /**
     * 显示温馨提示
     */
    void showGuide(Integer credits);
    }
