package com.xiaolian.amigo.ui.user;

import android.widget.Button;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.login.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeCheckReqDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeGetReqDTO;
import com.xiaolian.amigo.data.network.model.user.PasswordCheckReqDTO;
import com.xiaolian.amigo.data.network.model.user.PasswordVerifyRespDTO;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IChangePhoneView;
import com.xiaolian.amigo.ui.user.intf.IChangePhonePresenter;

import javax.inject.Inject;

public class ChangePhonePresenter <v extends IChangePhoneView> extends BasePresenter<v>
        implements IChangePhonePresenter<v> {
    private IUserDataManager userDataManager;

    @Inject
    ChangePhonePresenter(IUserDataManager userDataManager) {
        super();
        this.userDataManager = userDataManager;
    }

    @Override
    public void getVerification(String mobile , Button button) {
        VerificationCodeGetReqDTO dto = new VerificationCodeGetReqDTO();
        dto.setMobile(mobile);
        addObserver(userDataManager.getVerification(dto), new NetworkObserver<ApiResult<BooleanRespDTO>>() {
            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    button.setEnabled(true);
                    if (result.getData().isResult()) {
                        getMvpView().onSuccess("验证码发送成功");
                        getMvpView().startTimer();
                    } else {
                        getMvpView().onError("验证码发送失败，请重试");
                    }
                } else {
                    button.setEnabled(true);
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                button.setEnabled(true);
            }
        });
    }

    @Override
    public void changePhoneNumber(String mobile, String code , Button button) {
        VerificationCodeCheckReqDTO dto = new VerificationCodeCheckReqDTO();
        dto.setMobile(mobile);
        dto.setCode(code);
        addObserver(userDataManager.checkChangePhoneVerification(dto), new NetworkObserver<ApiResult<EntireUserDTO>>() {
            @Override
            public void onReady(ApiResult<EntireUserDTO> result) {
                if (null == result.getError()) {
                    button.setEnabled(true);
                    getMvpView().onSuccess("修改手机号成功");
                    getMvpView().finishView();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                    button.setEnabled(true);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                button.setEnabled(true);
            }
        });
    }
}


