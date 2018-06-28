package com.xiaolian.amigo.ui.more;

import com.xiaolian.amigo.data.base.LogInterceptor;
import com.xiaolian.amigo.data.manager.intf.IMainDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.version.CheckVersionUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.version.CheckVersionUpdateRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.more.intf.IAboutUsPresenter;
import com.xiaolian.amigo.ui.more.intf.IAboutUsView;

import javax.inject.Inject;

/**
 * 关于我们presenter
 *
 * @author zcd
 * @date 17/11/9
 */

public class AboutUsPresenter<V extends IAboutUsView> extends BasePresenter<V>
        implements IAboutUsPresenter<V> {
    @SuppressWarnings("unused")
    private static final String TAG = AboutUsPresenter.class.getSimpleName();
    private IMainDataManager mainDataManager;
    private LogInterceptor interceptor;

    @Inject
    AboutUsPresenter(IMainDataManager manager, LogInterceptor interceptor) {
        this.mainDataManager = manager;
        this.interceptor = interceptor;
    }

    @Override
    public void checkUpdate(Integer code, String versionNo, boolean click) {
        CheckVersionUpdateReqDTO reqDTO = new CheckVersionUpdateReqDTO();
        reqDTO.setCode(code);
        reqDTO.setVersionNo(versionNo);
        addObserver(mainDataManager.checkUpdate(reqDTO),
                new NetworkObserver<ApiResult<CheckVersionUpdateRespDTO>>(false) {

                    @Override
                    public void onReady(ApiResult<CheckVersionUpdateRespDTO> result) {
                        if (null == result.getError()) {
                            if (result.getData().getResult()) {
                                // 是否是用户点击
                                if (click) {
                                    getMvpView().showUpdateDialog(result.getData().getVersion());
                                } else {
                                    getMvpView().showUpdateButton(result.getData().getVersion());
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void changeHost(String server, String h5Server) {
        interceptor.setServer(server);
        interceptor.setH5Server(h5Server);
    }
}
