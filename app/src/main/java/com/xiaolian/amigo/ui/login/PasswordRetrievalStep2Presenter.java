package com.xiaolian.amigo.ui.login;

import com.xiaolian.amigo.data.manager.intf.ILoginDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.PasswordResetReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.login.intf.IPasswordRetrievalStep2Presenter;
import com.xiaolian.amigo.ui.login.intf.IPasswordRetrievalStep2View;

import javax.inject.Inject;

/**
 * 找回密码2
 * <p>
 * Created by zcd on 9/20/17.
 */

public class PasswordRetrievalStep2Presenter<V extends IPasswordRetrievalStep2View> extends BasePresenter<V>
        implements IPasswordRetrievalStep2Presenter<V> {

    private ILoginDataManager manager;

    @Inject
    public PasswordRetrievalStep2Presenter(ILoginDataManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void resetPassword(String code, String mobile, String password) {
        PasswordResetReqDTO dto = new PasswordResetReqDTO();
        dto.setCode(code);
        dto.setMobile(mobile);
        dto.setPassword(password);
        addObserver(manager.passwordReset(dto), new NetworkObserver<ApiResult<BooleanRespDTO>>() {
            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isResult()) {
                        getMvpView().onSuccess("密码重置成功，请登录");
                        getMvpView().gotoLoginView();
                    } else {
                        getMvpView().onError("密码重置失败，请重试");
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
