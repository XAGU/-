package com.xiaolian.amigo.ui.device.heater;

import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
import com.xiaolian.amigo.data.manager.intf.IDeviceDataManager;
import com.xiaolian.amigo.ui.device.WaterDeviceBasePresenter;
import com.xiaolian.amigo.ui.device.intf.heator.IHeaterPresenter;
import com.xiaolian.amigo.ui.device.intf.heator.IHeaterView;

import javax.inject.Inject;

/**
 * Created by caidong on 2017/9/22.
 */
public class HeaterPresenter<V extends IHeaterView> extends WaterDeviceBasePresenter<V>
        implements IHeaterPresenter<V> {

    private IDeviceDataManager deviceDataManager;

    @Inject
    HeaterPresenter(IBleDataManager bleDataManager,
                    IDeviceDataManager deviceDataManager) {
        super(bleDataManager, deviceDataManager);
        this.deviceDataManager = deviceDataManager;
    }

    @Override
    public void onAttach(V view) {
        super.onAttach(view);
        if (!deviceDataManager.isHeaterGuideDone()) {
            getMvpView().showGuide();
            deviceDataManager.doneHeaterGuide();
        }
    }

}
