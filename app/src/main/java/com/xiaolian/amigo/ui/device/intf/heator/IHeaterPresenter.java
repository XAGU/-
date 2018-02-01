package com.xiaolian.amigo.ui.device.intf.heator;

import com.xiaolian.amigo.ui.device.intf.IWaterDeviceBasePresenter;
import com.xiaolian.amigo.ui.device.intf.IWaterDeviceBaseView;

/**
 * 热水澡
 *
 * @author zcd
 * @date 17/9/22
 */
public interface IHeaterPresenter<V extends IWaterDeviceBaseView> extends IWaterDeviceBasePresenter<V> {
    /**
     * 不显示提示
     */
    void notShowRemindAlert();
}
