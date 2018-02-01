package com.xiaolian.amigo.ui.device.intf.dispenser;

import com.xiaolian.amigo.ui.device.intf.IWaterDeviceBaseView;

/**
 * 饮水机
 *
 * @author zcd
 * @date 17/10/13
 * Created by zcd on 10/13/17.
 */

public interface IDispenserView extends IWaterDeviceBaseView {
    /**
     * 显示已收藏图标
     */
    void setFavoriteIcon();

    /**
     * 显示未收藏图标
     */
    void setUnFavoriteIcon();
}
