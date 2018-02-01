package com.xiaolian.amigo.ui.main.intf;

import com.xiaolian.amigo.data.network.model.system.BannerDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

import java.util.ArrayList;

/**
 * 闪屏页面
 *
 * @author zcd
 * @date 17/11/13
 */

public interface ISplashView extends IBaseView {
    /**
     * 跳转到首页
     *
     * @param banners banner
     */
    void startMain(ArrayList<BannerDTO> banners);

    /**
     * 服务器未响应状态跳转到首页
     *
     * @param banners banner
     */
    void startMainServerNoResponse(ArrayList<BannerDTO> banners);
}
