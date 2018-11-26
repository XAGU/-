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
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.login.LoginReqDTO;
import com.xiaolian.amigo.data.network.model.login.LoginRespDTO;
import com.xiaolian.amigo.data.network.model.login.PasswordResetReqDTO;
import com.xiaolian.amigo.data.network.model.login.RegisterReqDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeCheckReqDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeGetReqDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.login.intf.ILoginPresenter;
import com.xiaolian.amigo.ui.login.intf.ILoginView;

import javax.inject.Inject;

/**
 * 登录页presenter
 *
 * @author caidong
 * @date 17/9/14
 */
public class LoginPresenter<V extends ILoginView> extends BasePresenter<V>
        implements ILoginPresenter<V> {

    private ILoginDataManager loginDataManager;

    @Inject
    LoginPresenter(ILoginDataManager loginDataManager) {
        super();
        this.loginDataManager = loginDataManager;
    }

    @Override
    public void onLoginClick(String mobile, String password, String androidId,
                             String brand, String model, String systemVersion,
                             String appVersion) {
        LoginReqDTO dto = new LoginReqDTO();
        dto.setMobile(mobile);
        dto.setPassword(password);
        dto.setBrand(brand);
        dto.setModel(model);
        dto.setUniqueId(androidId);
        dto.setSystemVersion(systemVersion);
        dto.setAppVersion(appVersion);
        // 1表示IOS 2表示Android
        dto.setSystem(2);
        addObserver(loginDataManager.login(dto), new NetworkObserver<ApiResult<LoginRespDTO>>() {

            @Override
            public void onReady(ApiResult<LoginRespDTO> result) {
                if (null == result.getError()) {
                    loginDataManager.setUserInfo(result.getData().getUser().transform());
                    loginDataManager.setAccessToken(result.getData().getAccessToken());
                    loginDataManager.setRefreshToken(result.getData().getRefreshToken());
                    loginDataManager.setRememberMobile(mobile);
                    loginDataManager.setIsFirstAfterLogin(true);
                    getMvpView().onSuccess(R.string.login_success);
                    getMvpView().gotoMainView();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void register(String code, String mobile,
                         String password, Long schoolId,
                         String androidId, String brand,
                         String model, String systemVersion,
                         String appVersion) {
        RegisterReqDTO dto = new RegisterReqDTO();
        dto.setCode(code);
        dto.setMobile(mobile);
        dto.setPassword(password);
        dto.setSchoolId(schoolId);
        dto.setUniqueId(androidId);
        dto.setBrand(brand);
        dto.setModel(model);
        dto.setAppVersion(appVersion);
        dto.setSystem(RegisterReqDTO.SYSTEM_CODE);
        dto.setSystemVersion(systemVersion);
        addObserver(loginDataManager.register(dto), new NetworkObserver<ApiResult<LoginRespDTO>>() {

            @Override
            public void onReady(ApiResult<LoginRespDTO> result) {
                if (null == result.getError()) {
                    loginDataManager.setUserInfo(result.getData().getUser().transform());
                    getMvpView().onSuccess(R.string.register_success);
                    loginDataManager.setRememberMobile(mobile);
                    getMvpView().gotoMainView();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void getVerification(String mobile) {
        VerificationCodeGetReqDTO dto = new VerificationCodeGetReqDTO();
        dto.setMobile(mobile);
        addObserver(loginDataManager.getVerification(dto), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isResult()) {
                        getMvpView().onSuccess("验证码发送成功");
                        getMvpView().startTimer();
                    } else {
                        getMvpView().onError("验证码发送失败，请重试");
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void checkVerification(String mobile, String code) {
        VerificationCodeCheckReqDTO dto = new VerificationCodeCheckReqDTO();
        dto.setMobile(mobile);
        dto.setCode(code);
        addObserver(loginDataManager.verificationCheck(dto), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isResult()) {
                        getMvpView().gotoRegisterStep2View();
                    } else {
                        getMvpView().onError("验证码校验失败,请重试");
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
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
        addObserver(loginDataManager.passwordReset(dto), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isResult()) {
                        getMvpView().onSuccess("密码重置成功");
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

    @Override
    public void logout() {
        loginDataManager.logout();
    }

    @Override
    public String getMobile() {
        return loginDataManager.getRememberMobile();
    }

    @Override
    public void deletePushToken() {
        loginDataManager.deletePushToken();
    }

    @Override
    public Long getSchoolId() {
        return loginDataManager.getSchoolId();
    }

    @Override
    public String getPushTag() {
        return loginDataManager.getPushTag();
    }

    @Override
    public void setPushTag(String pushTag) {
        loginDataManager.setPushTag(pushTag);
    }

    @Override
    public void setIsFirstAfterLogin(boolean b) {
        loginDataManager.setIsFirstAfterLogin(b);
    }
}
