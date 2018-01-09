package com.xiaolian.amigo.ui.device.dispenser;

import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
import com.xiaolian.amigo.data.manager.intf.IDeviceDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.device.FavorDeviceReqDTO;
import com.xiaolian.amigo.ui.device.WaterDeviceBasePresenter;
import com.xiaolian.amigo.ui.device.intf.dispenser.IDispenserPresenter;
import com.xiaolian.amigo.ui.device.intf.dispenser.IDispenserView;
import com.xiaolian.amigo.util.Constant;

import javax.inject.Inject;

/**
 * 饮水机
 * <p>
 * Created by zcd on 10/13/17.
 */

public class DispenserPresenter<V extends IDispenserView> extends WaterDeviceBasePresenter<V>
        implements IDispenserPresenter<V> {
    private IDeviceDataManager deviceDataManager;

    @Inject
    public DispenserPresenter(IBleDataManager bleDataManager,
                              IDeviceDataManager deviceDataManager) {
        super(bleDataManager, deviceDataManager);
        this.deviceDataManager = deviceDataManager;
    }

    @Override
    public void onAttach(V view) {
        super.onAttach(view);
        if (!deviceDataManager.isDispenserGuideDone()) {
            deviceDataManager.doneDispenserGuide();
            getMvpView().showGuide();
        }
    }

    @Override
    public void favorite(Long id) {
        FavorDeviceReqDTO reqDTO = new FavorDeviceReqDTO();
        reqDTO.setType(Device.DISPENSER.getType());
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
        reqDTO.setType(Device.DISPENSER.getType());
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
        deviceDataManager.setDispenserGuide(Constant.REMIND_ALERT_COUNT);
    }
}
