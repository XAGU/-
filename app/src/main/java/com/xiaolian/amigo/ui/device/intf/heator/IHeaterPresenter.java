package com.xiaolian.amigo.ui.device.intf.heator;

import com.xiaolian.amigo.ui.device.intf.IDevicePresenter;
import com.xiaolian.amigo.ui.device.intf.IDeviceView;

/**
 * Created by caidong on 2017/9/22.
 */
public interface IHeaterPresenter<V extends IDeviceView> extends IDevicePresenter<V> {
    void setBonusAmount(int amount);

    int getBonusAmount();

    void queryWallet(int amount);

}
