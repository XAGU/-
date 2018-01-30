package com.xiaolian.amigo.ui.device.intf.dryer;

import com.xiaolian.amigo.ui.device.intf.IWaterDeviceBaseView;

/**
 * 吹风机
 *
 * @author zcd
 * @date 18/1/2
 */

public interface IDryerView extends IWaterDeviceBaseView {
    /**
     * 显示已收藏图标
     */
    void setFavoriteIcon();

    /**
     * 显示未收藏图标
     */
    void setUnFavoriteIcon();
}
