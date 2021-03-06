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


import android.content.Context;
import android.net.Uri;

import com.xiaolian.amigo.di.RepairActivityContext;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

import java.util.List;

/**
 * 报修申请
 *
 * @author caidong
 * @date 17/9/20
 */
@RepairActivityContext
public interface IRepairApplyPresenter<V extends IRepairApplyView> extends IBasePresenter<V> {

    /**
     * 提交报修申请
     *
     * @param problems   报修原因
     * @param images     报修图片
     * @param content    报修内容
     * @param mobile     电话
     * @param deviceType 设备类型
     * @param locationId 位置id
     */
    void onSubmit(List<Long> problems, List<String> images, String content, String mobile, int deviceType, Long locationId);

    /**
     * 上传报修图片
     */
    void onUpload(Context context, Uri imageUri, int position);

    /**
     * 请求报修问题列表
     */
    void requestRepairProblems(int deviceType);

    /**
     * 设置上传报修时间
     *
     * @param l 时间
     */
    void setLastRepairTime(long l);

    /**
     * 获取手机号
     *
     * @return 手机号
     */
    String getMobile();
}
