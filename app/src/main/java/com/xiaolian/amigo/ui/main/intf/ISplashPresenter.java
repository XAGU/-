package com.xiaolian.amigo.ui.main.intf;

import com.xiaolian.amigo.data.network.model.system.BannerDTO;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

import java.util.ArrayList;

/**
 * 闪屏页
 *
 * @author zcd
 * @date 17/11/13
 */

public interface ISplashPresenter<V extends ISplashView> extends IBasePresenter<V> {

    /**
     * 获取系统基础信息
     */
    void getSystemBaseInfo();

    /**
     * 获取默认banner
     *
     * @return 默认banner
     */
    ArrayList<BannerDTO> getDefaultBanners();
}
