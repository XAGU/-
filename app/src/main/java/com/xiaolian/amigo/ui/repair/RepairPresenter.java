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


import android.os.Build;
import android.support.annotation.RequiresApi;

import com.xiaolian.amigo.data.manager.intf.IRepairDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.RepairReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.RepairRespDTO;
import com.xiaolian.amigo.data.network.model.repair.Repair;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.repair.adaptor.RepairAdaptor;
import com.xiaolian.amigo.ui.repair.intf.IRepairPresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class RepairPresenter<V extends IRepairView> extends BasePresenter<V>
        implements IRepairPresenter<V> {

    private static final String TAG = RepairPresenter.class.getSimpleName();
    private IRepairDataManager manager;

    @Inject
    public RepairPresenter(IRepairDataManager manager) {
        super();
        this.manager = manager;
    }


    @Override
    public void requestRepairs(int page) {
        RepairReqDTO reqDTO = new RepairReqDTO();
        reqDTO.setPage(page);
        reqDTO.setSize(Integer.MAX_VALUE);
        addObserver(manager.queryRepairs(reqDTO), new NetworkObserver<ApiResult<RepairRespDTO>>() {

            @Override
            public void onReady(ApiResult<RepairRespDTO> result) {
                if (null == result.getError()) {
                    List<RepairAdaptor.RepairWrapper> wrappers = new ArrayList<>();
                    for (Repair repair : result.getData().getRepairDevices()) {
                        wrappers.add(new RepairAdaptor.RepairWrapper(repair));
                    }
                    getMvpView().addMore(wrappers);
                }
            }
        });
    }
}
