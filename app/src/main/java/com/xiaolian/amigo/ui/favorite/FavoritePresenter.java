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
import com.xiaolian.amigo.data.network.model.device.Device;
import com.xiaolian.amigo.data.network.model.dto.request.FavoriteReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.UnFavoriteReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.FavoriteRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.UnFavoriteRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.favorite.adaptor.FavoriteAdaptor;
import com.xiaolian.amigo.ui.favorite.intf.IFavoritePresenter;
import com.xiaolian.amigo.ui.favorite.intf.IFavoriteView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FavoritePresenter<V extends IFavoriteView> extends BasePresenter<V>
        implements IFavoritePresenter<V> {

    private static final String TAG = FavoritePresenter.class.getSimpleName();
    private IFavoriteManager manager;

    @Inject
    public FavoritePresenter(IFavoriteManager manager) {
        super();
        this.manager = manager;
    }


    @Override
    public void requestFavorites(int page) {
        FavoriteReqDTO reqDTO = new FavoriteReqDTO();
        reqDTO.setPage(page);
        reqDTO.setSize(Integer.MAX_VALUE);
        // 查看收藏设备列表
        addObserver(manager.queryFavorites(reqDTO), new NetworkObserver<ApiResult<FavoriteRespDTO>>() {
            @Override
            public void onReady(ApiResult<FavoriteRespDTO> result) {
                if (null == result.getError()) {
                    List<FavoriteAdaptor.FavoriteWrapper> wrappers = new ArrayList<>();
                    if (null != result.getData().getDevices() && result.getData().getDevices().size() > 0) {
                        for (Device device : result.getData().getDevices()) {
                            wrappers.add(new FavoriteAdaptor.FavoriteWrapper(device));
                        }
                        getMvpView().addMore(wrappers);
                    }
                }
            }
        });
    }

    @Override
    public void onDelete(final Long deviceId, int index) {
        UnFavoriteReqDTO reqDTO = new UnFavoriteReqDTO();
        reqDTO.setId(deviceId);
        // 查看收藏设备列表
        addObserver(manager.deleteFavorite(reqDTO), new NetworkObserver<ApiResult<UnFavoriteRespDTO>>() {
            @Override
            public void onReady(ApiResult<UnFavoriteRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().deleteOne(index);
                }
            }
        });
    }
}
