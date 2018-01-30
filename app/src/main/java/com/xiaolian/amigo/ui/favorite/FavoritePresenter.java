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
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.device.FavorDeviceDTO;
import com.xiaolian.amigo.data.network.model.device.FavorDeviceReqDTO;
import com.xiaolian.amigo.data.network.model.device.QueryDeviceListReqDTO;
import com.xiaolian.amigo.data.network.model.device.QueryFavorDeviceRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.favorite.adaptor.FavoriteAdaptor;
import com.xiaolian.amigo.ui.favorite.intf.IFavoritePresenter;
import com.xiaolian.amigo.ui.favorite.intf.IFavoriteView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * favorite presenter
 *
 * @author caidong
 * @date 17/9/18
 */
public class FavoritePresenter<V extends IFavoriteView> extends BasePresenter<V>
        implements IFavoritePresenter<V> {
    @SuppressWarnings("unused")
    private static final String TAG = FavoritePresenter.class.getSimpleName();
    private IFavoriteManager favoriteManager;

    @Inject
    FavoritePresenter(IFavoriteManager favoriteManager) {
        super();
        this.favoriteManager = favoriteManager;
    }


    @Override
    public void requestFavorites(int page) {
        QueryDeviceListReqDTO reqDTO = new QueryDeviceListReqDTO();
        reqDTO.setPage(page);
        reqDTO.setSize(Constant.PAGE_SIZE);
        // 查看收藏设备列表
        addObserver(favoriteManager.getFavorites(reqDTO), new NetworkObserver<ApiResult<QueryFavorDeviceRespDTO>>(false, true) {
            @Override
            public void onReady(ApiResult<QueryFavorDeviceRespDTO> result) {
                getMvpView().setRefreshComplete();
                getMvpView().setLoadMoreComplete();
                getMvpView().hideEmptyView();
                getMvpView().hideErrorView();
                if (null == result.getError()) {
                    if (null != result.getData().getDevices() && result.getData().getDevices().size() > 0) {
                        List<FavoriteAdaptor.FavoriteWrapper> wrappers = new ArrayList<>();
                        for (FavorDeviceDTO device : result.getData().getDevices()) {
                            wrappers.add(new FavoriteAdaptor.FavoriteWrapper(device.transform()));
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
    public void onDelete(final Long residenceId, int index, int type) {
        FavorDeviceReqDTO reqDTO = new FavorDeviceReqDTO();
        reqDTO.setId(residenceId);
        reqDTO.setType(type);
        // 查看收藏设备列表
        addObserver(favoriteManager.unFavorite(reqDTO), new NetworkObserver<ApiResult<SimpleRespDTO>>() {
            @Override
            public void onReady(ApiResult<SimpleRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().deleteOne(index);
                }
            }
        });
    }
}
