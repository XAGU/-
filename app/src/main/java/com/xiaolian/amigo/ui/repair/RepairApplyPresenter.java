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

package com.xiaolian.amigo.ui.repair;


import android.net.Uri;
import android.util.Log;

import com.xiaolian.amigo.data.manager.intf.IRepairDataManager;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.RepairApplyReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.RepairProblemReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.RepairApplyRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.RepairProblemRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairApplyPresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairApplyView;
import com.xiaolian.amigo.util.Constant;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RepairApplyPresenter<V extends IRepairApplyView> extends BasePresenter<V>
        implements IRepairApplyPresenter<V> {

    private static final String TAG = RepairApplyPresenter.class.getSimpleName();
    private IRepairDataManager repairManager;
    private IUserDataManager userManager;

    @Inject
    public RepairApplyPresenter(IRepairDataManager repairmanager, IUserDataManager userManager) {
        super();
        this.repairManager = repairmanager;
        this.userManager = userManager;
    }


    @Override
    public void onSubmit(List<Long> problems, List<String> images, String content, String mobile, int deviceType, Long locationId) {
        RepairApplyReqDTO reqDTO = new RepairApplyReqDTO();
        reqDTO.setCauseIds(problems);
        reqDTO.setDescription(content);
        reqDTO.setDeviceType(deviceType);
        reqDTO.setImages(images);
        reqDTO.setMobile(Long.valueOf(mobile));
        reqDTO.setResidenceId(locationId);

        addObserver(repairManager.applyRepair(reqDTO), new NetworkObserver<ApiResult<RepairApplyRespDTO>>() {

            @Override
            public void onReady(ApiResult<RepairApplyRespDTO> result) {
                if (null == result.getError()) {
                    Log.i(TAG, "报修成功");
                    getMvpView().onSuccess("报修成功");
                    getMvpView().backToRepairNav();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void onUpload(Uri imageUri) {
        RequestBody image = RequestBody.create(MediaType.parse(Constant.UPLOAD_IMAGE_CONTENT_TYPE),
                new File(imageUri.getPath()));
        addObserver(userManager.uploadFile(image), new NetworkObserver<ApiResult<String>>() {

            @Override
            public void onReady(ApiResult<String> result) {
                if (null == result.getError()) {
                    Log.i(TAG, "上传图片成功");
                    getMvpView().addImage(result.getData());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void requestRepairProblems() {
        RepairProblemReqDTO reqDTO = new RepairProblemReqDTO();
        reqDTO.setPage(Constant.PAGE_START_NUM);
        reqDTO.setSize(Constant.PAGE_SIZE);
        addObserver(repairManager.queryRepairProblems(reqDTO), new NetworkObserver<ApiResult<RepairProblemRespDTO>>() {

            @Override
            public void onReady(ApiResult<RepairProblemRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().refreshProblems(result.getData().getCauses());
                }
            }
        });
    }
}
