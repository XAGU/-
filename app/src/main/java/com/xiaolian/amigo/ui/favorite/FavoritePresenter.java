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

package com.xiaolian.amigo.ui.favorite;


import com.xiaolian.amigo.data.manager.intf.IFavoriteManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.device.ScanDeviceGroup;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.ScanDeviceRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.UnFavoriteRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.favorite.adaptor.FavoriteAdaptor;
import com.xiaolian.amigo.ui.favorite.intf.IFavoritePresenter;
import com.xiaolian.amigo.ui.favorite.intf.IFavoriteView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FavoritePresenter<V extends IFavoriteView> extends BasePresenter<V>
        implements IFavoritePresenter<V> {
    @SuppressWarnings("unused")
    private static final String TAG = FavoritePresenter.class.getSimpleName();
    private IFavoriteManager favoriteManager;

    @Inject
    public FavoritePresenter(IFavoriteManager favoriteManager) {
        super();
        this.favoriteManager = favoriteManager;
    }


    @Override
    public void requestFavorites(int page) {
        SimpleQueryReqDTO reqDTO = new SimpleQueryReqDTO();
        reqDTO.setPage(page);
        reqDTO.setSize(Constant.PAGE_SIZE);
        // 查看收藏设备列表
        addObserver(favoriteManager.queryFavorites(reqDTO), new NetworkObserver<ApiResult<ScanDeviceRespDTO>>(false, true) {
            @Override
            public void onReady(ApiResult<ScanDeviceRespDTO> result) {
                getMvpView().setRefreshComplete();
                getMvpView().setLoadMoreComplete();
                getMvpView().hideEmptyView();
                getMvpView().hideErrorView();
                if (null == result.getError()) {
                    if (null != result.getData().getDevices() && result.getData().getDevices().size() > 0) {
                        List<FavoriteAdaptor.FavoriteWrapper> wrappers = new ArrayList<>();
                        for (ScanDeviceGroup device : result.getData().getDevices()) {
                            wrappers.add(new FavoriteAdaptor.FavoriteWrapper(device));
                        }
                        getMvpView().addMore(wrappers);
                        getMvpView().addPage();
                    } else {
                        getMvpView().showEmptyView();
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                    getMvpView().showErrorView();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().setRefreshComplete();
                getMvpView().setLoadMoreComplete();
                getMvpView().showErrorView();
            }
        });
    }

    @Override
    public void onDelete(final Long residenceId, int index) {
        SimpleReqDTO reqDTO = new SimpleReqDTO();
        reqDTO.setId(residenceId);
        // 查看收藏设备列表
        addObserver(favoriteManager.unFavorite(reqDTO), new NetworkObserver<ApiResult<UnFavoriteRespDTO>>() {
            @Override
            public void onReady(ApiResult<UnFavoriteRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().deleteOne(index);
                }
            }
        });
    }
}
