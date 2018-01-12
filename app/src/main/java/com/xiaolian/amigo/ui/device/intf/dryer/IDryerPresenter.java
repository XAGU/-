package com.xiaolian.amigo.ui.device.intf.dryer;

import com.xiaolian.amigo.ui.device.intf.IWaterDeviceBasePresenter;

/**
 * 吹风机
 * <p>
 * Created by zcd on 18/1/2.
 */

public interface IDryerPresenter<V extends IDryerView> extends IWaterDeviceBasePresenter<V> {
    void favorite(Long id);
    void unFavorite(Long id);

    void notShowRemindAlert();
}
