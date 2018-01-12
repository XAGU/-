package com.xiaolian.amigo.ui.device.dryer;

import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
import com.xiaolian.amigo.data.manager.intf.IDeviceDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.device.FavorDeviceReqDTO;
import com.xiaolian.amigo.ui.device.WaterDeviceBasePresenter;
import com.xiaolian.amigo.ui.device.intf.dryer.IDryerPresenter;
import com.xiaolian.amigo.ui.device.intf.dryer.IDryerView;
import com.xiaolian.amigo.util.Constant;

import javax.inject.Inject;

/**
 * 吹风机
 * <p>
 * Created by zcd on 18/1/2.
 */

public class DryerPresenter <V extends IDryerView> extends WaterDeviceBasePresenter<V>
    implements IDryerPresenter<V> {
    private IDeviceDataManager deviceDataManager;

    @Inject
    public DryerPresenter(IBleDataManager bleDataManager, IDeviceDataManager deviceDataManager) {
        super(bleDataManager, deviceDataManager);
        this.deviceDataManager = deviceDataManager;
    }

    @Override
    public void onAttach(V view) {
        super.onAttach(view);
        if (!deviceDataManager.isDryerGuideDone()) {
            deviceDataManager.doneDryerGuide();
            getMvpView().showGuide();
        }
    }

    @Override
    public void favorite(Long id) {
        FavorDeviceReqDTO reqDTO = new FavorDeviceReqDTO();
        reqDTO.setType(Device.DRYER.getType());
        reqDTO.setId(id);
        addObserver(deviceDataManager.favorite(reqDTO), new NetworkObserver<ApiResult<SimpleRespDTO>>() {

            @Override
            public void onReady(ApiResult<SimpleRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().onSuccess("设备收藏成功");
                    getMvpView().setFavoriteIcon();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void unFavorite(Long id) {
        FavorDeviceReqDTO reqDTO = new FavorDeviceReqDTO();
        reqDTO.setId(id);
        reqDTO.setType(Device.DRYER.getType());
        addObserver(deviceDataManager.unFavorite(reqDTO), new NetworkObserver<ApiResult<SimpleRespDTO>>() {

            @Override
            public void onReady(ApiResult<SimpleRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().setUnFavoriteIcon();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void notShowRemindAlert() {
        deviceDataManager.setDryerGuide(Constant.REMIND_ALERT_COUNT);
    }
}
