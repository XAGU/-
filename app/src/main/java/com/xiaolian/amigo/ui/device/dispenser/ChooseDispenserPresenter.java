package com.xiaolian.amigo.ui.device.dispenser;

import com.xiaolian.amigo.data.manager.intf.IFavoriteManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.device.Device;
import com.xiaolian.amigo.data.network.model.dto.request.FavoriteReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.FavoriteRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.intf.dispenser.IChooseDispenerView;
import com.xiaolian.amigo.ui.device.intf.dispenser.IChooseDispenserPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 选择饮水机
 * <p>
 * Created by zcd on 10/13/17.
 */

public class ChooseDispenserPresenter<V extends IChooseDispenerView> extends BasePresenter<V>
        implements IChooseDispenserPresenter<V> {

    private static final String TAG = ChooseDispenserPresenter.class.getSimpleName();
    private IFavoriteManager manager;

    @Inject
    public ChooseDispenserPresenter(IFavoriteManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void requestFavorites() {
        FavoriteReqDTO reqDTO = new FavoriteReqDTO();
        // 查看收藏设备列表
        addObserver(manager.queryFavorites(reqDTO), new NetworkObserver<ApiResult<FavoriteRespDTO>>(false) {
            @Override
            public void onReady(ApiResult<FavoriteRespDTO> result) {
                getMvpView().hideEmptyView();
                getMvpView().hideErrorView();
                if (null == result.getError()) {
                    if (null != result.getData().getDevices() && result.getData().getDevices().size() > 0) {
                        List<ChooseDispenserAdaptor.DispenserWapper> wrappers = new ArrayList<>();
                        for (Device device : result.getData().getDevices()) {
                            wrappers.add(new ChooseDispenserAdaptor.DispenserWapper(device));
                        }
                        getMvpView().addMore(wrappers);
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
                getMvpView().showErrorView();
            }
        });
    }
}
