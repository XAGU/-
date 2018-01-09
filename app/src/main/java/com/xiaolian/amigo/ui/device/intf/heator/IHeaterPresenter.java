package com.xiaolian.amigo.ui.device.intf.heator;

import com.xiaolian.amigo.ui.device.intf.IWaterDeviceBasePresenter;
import com.xiaolian.amigo.ui.device.intf.IWaterDeviceBaseView;

/**
 * Created by caidong on 2017/9/22.
 */
public interface IHeaterPresenter<V extends IWaterDeviceBaseView> extends IWaterDeviceBasePresenter<V> {
    void notShowRemindAlert();
}
