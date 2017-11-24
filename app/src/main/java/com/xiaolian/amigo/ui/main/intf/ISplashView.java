package com.xiaolian.amigo.ui.main.intf;

import com.xiaolian.amigo.data.network.model.dto.response.BannerDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * 闪屏页面
 * <p>
 * Created by zcd on 17/11/13.
 */

public interface ISplashView extends IBaseView {
    void startMain(ArrayList<BannerDTO> banners);
    void startMainServerNoResponse(ArrayList<BannerDTO> banners);
}
