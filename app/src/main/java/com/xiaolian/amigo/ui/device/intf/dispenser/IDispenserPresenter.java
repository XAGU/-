package com.xiaolian.amigo.ui.device.intf.dispenser;

import com.xiaolian.amigo.ui.device.intf.IWaterDeviceBasePresenter;

/**
 * 饮水机j
 * <p>
 * Created by zcd on 10/13/17.
 */

public interface IDispenserPresenter<V extends IDispenserView> extends IWaterDeviceBasePresenter<V> {
    void favorite(Long id);
    void unFavorite(Long id);
    void notShowRemindAlert();
}
