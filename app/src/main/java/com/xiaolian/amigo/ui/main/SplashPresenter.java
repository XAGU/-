package com.xiaolian.amigo.ui.main;

import com.xiaolian.amigo.data.manager.intf.IMainDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.system.BannerDTO;
import com.xiaolian.amigo.data.network.model.system.BaseInfoDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.main.intf.ISplashPresenter;
import com.xiaolian.amigo.ui.main.intf.ISplashView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * 闪屏页
 *
 * @author zcd
 * @date 17/11/13
 */

public class SplashPresenter<V extends ISplashView> extends BasePresenter<V>
        implements ISplashPresenter<V> {
    private static final String TAG = SplashPresenter.class.getSimpleName();
    private IMainDataManager mainDataManager;

    @Inject
    SplashPresenter(IMainDataManager mainDataManager) {
        this.mainDataManager = mainDataManager;
    }

    @Override
    public void getSystemBaseInfo() {
        addObserver(mainDataManager.getSystemBaseInfo(), new NetworkObserver<ApiResult<BaseInfoDTO>>(false) {

            @Override
            public void onReady(ApiResult<BaseInfoDTO> result) {
                getMvpView().startMain(result.getData().getBanners());
                mainDataManager.setBathroomPasswordDesc(result.getData().getBathPasswordDescription());
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().startMainServerNoResponse(getDefaultBanners());
            }
        });
    }

    @Override
    public ArrayList<BannerDTO> getDefaultBanners() {
        ArrayList<BannerDTO> banners = new ArrayList<>();
        banners.add(new BannerDTO(Constant.DEFAULT_BANNER_TYPE,
                Constant.DEFAULT_BANNER_IMAGE, Constant.DEFAULT_BANNER_LINK));
        return banners;
    }


}
