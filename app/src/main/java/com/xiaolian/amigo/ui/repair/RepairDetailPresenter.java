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


import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.manager.intf.IRepairDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.cs.RemindReqDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairDetailReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairDetailRespDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairStep;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.repair.adaptor.RepairProgressAdaptor;
import com.xiaolian.amigo.ui.repair.intf.IRepairDetailPresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairDetailView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RepairDetailPresenter<V extends IRepairDetailView> extends BasePresenter<V>
        implements IRepairDetailPresenter<V> {
    @SuppressWarnings("unused")
    private static final String TAG = RepairDetailPresenter.class.getSimpleName();
    private IRepairDataManager repairDataManager;
    private Device device;
    private Long id;

    @Inject
    public RepairDetailPresenter(IRepairDataManager repairDataManager) {
        super();
        this.repairDataManager = repairDataManager;
    }


    @Override
    public void requestRepailDetail(Long id) {
        this.id = id;
        RepairDetailReqDTO reqDTO = new RepairDetailReqDTO();
        reqDTO.setId(id);
        addObserver(repairDataManager.queryRepairDetail(reqDTO), new NetworkObserver<ApiResult<RepairDetailRespDTO>>() {

            @Override
            public void onReady(ApiResult<RepairDetailRespDTO> result) {
                if (null == result.getError()) {
                    List<RepairProgressAdaptor.ProgressWrapper> wrappers = new ArrayList<>();
                    for (RepairStep step : result.getData().getSteps()) {
                        wrappers.add(new RepairProgressAdaptor.ProgressWrapper(step));
                    }
                    getMvpView().addMoreProgresses(wrappers);
                    device = Device.getDevice(result.getData().getDeviceType());
                    getMvpView().render(result.getData());
                }
            }
        });
    }

    @Override
    public void remind(Long sourceId) {
        RemindReqDTO reqDTO = new RemindReqDTO();
        reqDTO.setSourceId(sourceId);
        // 2 表示维修
        reqDTO.setType(2);
        addObserver(repairDataManager.remind(reqDTO), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isResult()) {
                        getMvpView().onSuccess("提醒客服成功");
                    } else {
                        getMvpView().onError("提醒客服失败");
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public Device getDevice() {
        return device;
    }

    @Override
    public void cancelRepair() {
        SimpleReqDTO reqDTO = new SimpleReqDTO();
        reqDTO.setId(this.id);
        addObserver(repairDataManager.cancelRepair(reqDTO), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isResult()) {
                        getMvpView().onSuccess(R.string.cancel_repair_success);
                        getMvpView().backToList();
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
