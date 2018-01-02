package com.xiaolian.amigo.ui.device.dryer;

import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
import com.xiaolian.amigo.data.manager.intf.IDeviceDataManager;
import com.xiaolian.amigo.ui.device.WaterDeviceBasePresenter;
import com.xiaolian.amigo.ui.device.intf.dryer.IDryerPresenter;
import com.xiaolian.amigo.ui.device.intf.dryer.IDryerView;

import javax.inject.Inject;

/**
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
    public void favorite(Long id) {

    }

    @Override
    public void unFavorite(Long id) {

    }
}
