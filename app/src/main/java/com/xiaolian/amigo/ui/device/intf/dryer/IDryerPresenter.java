package com.xiaolian.amigo.ui.device.intf.dryer;

import com.xiaolian.amigo.ui.device.intf.IWaterDeviceBasePresenter;

/**
 * 吹风机
 *
 * @author zcd
 * @date 18/1/2
 */

public interface IDryerPresenter<V extends IDryerView> extends IWaterDeviceBasePresenter<V> {
    /**
     * 收藏设备
     *
     * @param id 设备id
     */
    void favorite(Long id);

    /**
     * 取消收藏设备
     *
     * @param id 设备id
     */
    void unFavorite(Long id);

    /**
     * 不显示提示对话框
     */
    void notShowRemindAlert();
}
