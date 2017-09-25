/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.xiaolian.amigo.ui.login;


import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.ILoginDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.LoginReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.PasswordResetReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.RegisterReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.VerificationCodeCheckReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.VerificationCodeGetReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.LoginRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.login.intf.ILoginPresenter;
import com.xiaolian.amigo.ui.login.intf.ILoginView;

import javax.inject.Inject;

public class LoginPresenter<V extends ILoginView> extends BasePresenter<V>
        implements ILoginPresenter<V> {

    private ILoginDataManager manager;

    @Inject
    public LoginPresenter(ILoginDataManager manager) {
        super();
        this.manager = manager;
    }


    @Override
    public void onLoginClick(String mobile, String password) {
        LoginReqDTO dto = new LoginReqDTO();
        dto.setMobile(mobile);
        dto.setPassword(password);
        addObserver(manager.login(dto), new NetworkObserver<ApiResult<LoginRespDTO>>() {

            @Override
            public void onReady(ApiResult<LoginRespDTO> result) {
                if (null == result.getError()) {
                    manager.setUserInfo(result.getData().getUser());
                    manager.setToken(result.getData().getToken());
                    getMvpView().showMessage("登录成功");
                    getMvpView().gotoMainView();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void register(String code, String mobile, String password, Long schoolId) {
        RegisterReqDTO dto = new RegisterReqDTO();
        dto.setCode(code);
        dto.setMobile(mobile);
        dto.setPassword(password);
        dto.setSchoolId(schoolId);
        addObserver(manager.register(dto), new NetworkObserver<ApiResult<LoginRespDTO>>() {

            @Override
            public void onReady(ApiResult<LoginRespDTO> result) {
                if (null == result.getError()) {
                    manager.setUserInfo(result.getData().getUser());
                    manager.setToken(result.getData().getToken());
                    getMvpView().showMessage("注册成功");
                    getMvpView().gotoMainView();
//                    getMvpView().gotoLoginView();
                } else {
                    getMvpView().showMessage(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void getVerification(String mobile) {
        VerificationCodeGetReqDTO dto = new VerificationCodeGetReqDTO();
        dto.setMobile(mobile);
        addObserver(manager.getVerification(dto), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isResult()) {
                        getMvpView().showMessage("验证码发送成功");
                        getMvpView().startTimer();
                    } else {
                        getMvpView().showMessage("验证码发送失败，请重试");
                    }
                } else {
                    getMvpView().showMessage(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void checkVerification(String mobile, String code) {
        VerificationCodeCheckReqDTO dto = new VerificationCodeCheckReqDTO();
        dto.setMobile(mobile);
        dto.setCode(code);
        addObserver(manager.verificationCheck(dto), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isResult()) {
                        getMvpView().gotoRegisterStep2View();
                    } else {
                        getMvpView().showMessage("验证码校验失败,请重试");
                    }
                } else {
                    getMvpView().showMessage(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void resetPassword(String mobile, String password, String code) {
        PasswordResetReqDTO dto = new PasswordResetReqDTO();
        dto.setCode(code);
        dto.setMobile(mobile);
        dto.setPassword(password);
        addObserver(manager.passwordReset(dto), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isResult()) {
                        getMvpView().showMessage("密码重置成功");
                        getMvpView().gotoLoginView();
                    } else {
                        getMvpView().showMessage("密码重置失败，请重试");
                    }
                } else {
                    getMvpView().showMessage(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
