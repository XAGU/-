package com.xiaolian.amigo.ui.device.intf.heator;

import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
import com.xiaolian.amigo.ui.device.DeviceBasePresenter;

import javax.inject.Inject;

/**
 * Created by caidong on 2017/9/22.
 */
public class HeaterPresenter<V extends IHeaterView> extends DeviceBasePresenter<V>
        implements IHeaterPresenter<V> {


    @Inject
    HeaterPresenter(IBleDataManager manager) {
        super(manager);
    }

}
