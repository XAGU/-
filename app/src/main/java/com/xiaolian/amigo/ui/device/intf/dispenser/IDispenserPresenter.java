package com.xiaolian.amigo.ui.device.intf.dispenser;

import com.xiaolian.amigo.ui.device.intf.IWaterDeviceBasePresenter;

/**
 * 饮水机
 *
 * @author zcd
 * @date 17/10/13
 */

public interface IDispenserPresenter<V extends IDispenserView> extends IWaterDeviceBasePresenter<V> {
    /**
     * 收藏饮水机
     *
     * @param id 设备id
     */
    void favorite(Long id);

    /**
     * 取消收藏饮水机
     *
     * @param id 设备id
     */
    void unFavorite(Long id);

    /**
     * 不显示提示对话框
     */
    void notShowRemindAlert();
}
