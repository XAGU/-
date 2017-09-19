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


import com.xiaolian.amigo.data.manager.intf.IRepairDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.RepairDetailReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.RepairReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.RepairDetailRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.RepairRespDTO;
import com.xiaolian.amigo.data.network.model.repair.Repair;
import com.xiaolian.amigo.data.network.model.repair.RepairStep;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.repair.adaptor.RepairAdaptor;
import com.xiaolian.amigo.ui.repair.adaptor.RepairProgressAdaptor;
import com.xiaolian.amigo.ui.repair.intf.IRepairDetailPresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairDetailView;
import com.xiaolian.amigo.ui.repair.intf.IRepairPresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RepairDetailPresenter<V extends IRepairDetailView> extends BasePresenter<V>
        implements IRepairDetailPresenter<V> {

    private static final String TAG = RepairDetailPresenter.class.getSimpleName();
    private IRepairDataManager manager;

    @Inject
    public RepairDetailPresenter(IRepairDataManager manager) {
        super();
        this.manager = manager;
    }


    @Override
    public void requestRepailDetail(Long id) {
        RepairDetailReqDTO reqDTO = new RepairDetailReqDTO();
        reqDTO.setId(id);
        addObserver(manager.queryRepairDetail(reqDTO), new NetworkObserver<ApiResult<RepairDetailRespDTO>>() {

            @Override
            public void onReady(ApiResult<RepairDetailRespDTO> result) {
                if (null == result.getError()) {
                    List<RepairProgressAdaptor.ProgressWrapper> wrappers = new ArrayList<>();
                    for (RepairStep step : result.getData().getSteps()) {
                        wrappers.add(new RepairProgressAdaptor.ProgressWrapper(step));
                    }
                    getMvpView().addMoreProgresses(wrappers);
                    getMvpView().render(result.getData());
                }
            }
        });
    }
}
