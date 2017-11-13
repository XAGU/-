package com.xiaolian.amigo.ui.main;

import com.xiaolian.amigo.data.manager.intf.IMainDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.response.PersonalExtraInfoDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.main.intf.ISplashPresenter;
import com.xiaolian.amigo.ui.main.intf.ISplashView;

import javax.inject.Inject;

/**
 * 闪屏页
 * <p>
 * Created by zcd on 17/11/13.
 */

public class SplashPresenter<V extends ISplashView> extends BasePresenter<V>
        implements ISplashPresenter<V> {
    private static final String TAG = SplashPresenter.class.getSimpleName();
    private IMainDataManager mainDataManager;

    @Inject
    public SplashPresenter(IMainDataManager mainDataManager) {
        this.mainDataManager = mainDataManager;
    }

    @Override
    public void getNoticeAmount() {
        addObserver(mainDataManager.getExtraInfo(), new NetworkObserver<ApiResult<PersonalExtraInfoDTO>>(false) {

            @Override
            public void onReady(ApiResult<PersonalExtraInfoDTO> result) {
                getMvpView().startMain();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().startMainServerNoResponse();
            }
        });
    }
}
