package com.xiaolian.amigo.ui.login;

import com.xiaolian.amigo.data.manager.intf.ILoginDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.VerificationCodeCheckReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.VerificationCodeGetReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.login.intf.IPasswordRetrievalStep1Presenter;
import com.xiaolian.amigo.ui.login.intf.IPasswordRetrievalStep1View;

import javax.inject.Inject;

/**
 * 找回密码
 * <p>
 * Created by zcd on 9/20/17.
 */

public class PasswordRetrievalStep1Presenter<V extends IPasswordRetrievalStep1View> extends BasePresenter<V>
        implements IPasswordRetrievalStep1Presenter<V> {
    private ILoginDataManager manager;

    @Inject
    public PasswordRetrievalStep1Presenter(ILoginDataManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void getVerification(String mobile) {
        VerificationCodeGetReqDTO dto = new VerificationCodeGetReqDTO();
        dto.setMobile(mobile);
        addObserver(manager.getVerification(dto), new NetworkObserver<ApiResult<BooleanRespDTO>>(){

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().onSuccess("验证码发送成功");
                    getMvpView().startTimer();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void checkVerification(String mobile, String code) {
        VerificationCodeCheckReqDTO reqDTO = new VerificationCodeCheckReqDTO();
        reqDTO.setCode(code);
        reqDTO.setMobile(mobile);
        addObserver(manager.verificationResetCheck(reqDTO), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().next();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
