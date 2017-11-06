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


import android.content.SharedPreferences;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.IRepairDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.RepairReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.RepairRespDTO;
import com.xiaolian.amigo.data.network.model.repair.Repair;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.repair.adaptor.RepairAdaptor;
import com.xiaolian.amigo.ui.repair.intf.IRepairPresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RepairPresenter<V extends IRepairView> extends BasePresenter<V>
        implements IRepairPresenter<V> {

    private static final String TAG = RepairPresenter.class.getSimpleName();
    private IRepairDataManager manager;
    private ISharedPreferencesHelp sharedPreferencesHelp;

    @Inject
    public RepairPresenter(IRepairDataManager manager, ISharedPreferencesHelp sharedPreferencesHelp) {
        super();
        this.manager = manager;
        this.sharedPreferencesHelp = sharedPreferencesHelp;
    }


    @Override
    public void requestRepairs(int page) {
        RepairReqDTO reqDTO = new RepairReqDTO();
        reqDTO.setPage(page);
        reqDTO.setSize(Constant.PAGE_SIZE);
        addObserver(manager.queryRepairs(reqDTO), new NetworkObserver<ApiResult<RepairRespDTO>>(false) {

            @Override
            public void onReady(ApiResult<RepairRespDTO> result) {
                getMvpView().setLoadMoreComplete();
                getMvpView().setRefreshComplete();
                getMvpView().hideEmptyView();
                getMvpView().hideErrorView();
                if (null == result.getError()) {
                    if (null != result.getData() && result.getData().getRepairDevices().size() > 0) {
                        List<RepairAdaptor.RepairWrapper> wrappers = new ArrayList<>();
                        for (Repair repair : result.getData().getRepairDevices()) {
                            wrappers.add(new RepairAdaptor.RepairWrapper(repair));
                        }
                        getMvpView().addMore(wrappers);
                        getMvpView().addPage();
                    } else {
                        getMvpView().showEmptyView(R.string.empty_tip_1);
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                    getMvpView().showErrorView();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().setLoadMoreComplete();
                getMvpView().setRefreshComplete();
                getMvpView().showErrorView();
            }
        });
    }

    @Override
    public void setLastRepairTime(Long time) {
        sharedPreferencesHelp.setLastRepairTime(time);
    }
}
