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


import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;

import com.alipay.sdk.app.AuthTask;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.ILoginDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.alipay.AlipayAuthInfoRespDTO;
import com.xiaolian.amigo.data.network.model.alipay.AlipayBindReq;
import com.xiaolian.amigo.data.network.model.alipay.AuthResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.login.LoginReqDTO;
import com.xiaolian.amigo.data.network.model.login.LoginRespDTO;
import com.xiaolian.amigo.data.network.model.login.PasswordResetReqDTO;
import com.xiaolian.amigo.data.network.model.login.RegisterReqDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeCheckReqDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeGetReqDTO;
import com.xiaolian.amigo.data.network.model.login.WeChatBindPhoneReqDTO;
import com.xiaolian.amigo.data.network.model.login.WeChatResiterReqDTO;
import com.xiaolian.amigo.data.network.model.login.WechatLoginReqDTO;
import com.xiaolian.amigo.data.network.model.version.CheckVersionUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.version.CheckVersionUpdateRespDTO;
import com.xiaolian.amigo.data.network.model.version.VersionDialogTime;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.login.intf.ILoginPresenter;
import com.xiaolian.amigo.ui.login.intf.ILoginView;
import com.xiaolian.amigo.util.CommonUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.xiaolian.amigo.util.Constant.UPDATE_REMIND_INTERVAL;

/**
 * 登录页presenter
 *
 * @author caidong
 * @date 17/9/14
 */
public class LoginPresenter<V extends ILoginView> extends BasePresenter<V>
        implements ILoginPresenter<V> {

    private ILoginDataManager loginDataManager;
    private long alipayUserId = -1;
    private String moblie;
    private String code;
    private String weChatOpenId;
    private String weChatUnionId;

    @Inject
    LoginPresenter(ILoginDataManager loginDataManager) {
        super();
        this.loginDataManager = loginDataManager;
    }

    @Override
    public void onLoginClick(String mobile, String password, String androidId,
                             String brand, String model, String systemVersion,
                             String appVersion , Button button) {
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
                    button.setEnabled(true);
                    if(result.getData().getResult()) {
                       loginDataManager.setUserInfo(result.getData().getUser().transform());
                       loginDataManager.setAccessToken(result.getData().getAccessToken());
                       loginDataManager.setRefreshToken(result.getData().getRefreshToken());

                       loginDataManager.setRememberMobile(mobile);
                       loginDataManager.setIsFirstAfterLogin(true);
                       getMvpView().onSuccess(R.string.login_success);
                       getMvpView().gotoMainView();
                     }else if(!result.getData().getResult() && result.getData().getRemaining() != null){
                        Integer remain = result.getData().getRemaining();
                        //检查密码错误剩余次数
                        if (1 == remain || 2 == remain) {
                            LoginActivity activity = (LoginActivity) getMvpView();
                            String title =activity.getResources().getString(R.string.verify_password_only_one_titile,remain);
                            getMvpView().showTipDialog(title,activity.getString(R.string.verify_password_tip));
                        }
                     }else if(!result.getData().getResult() && result.getData().getProtectInMinutes() != null) {
                         //检查剩余分钟数
                        LoginActivity activity = (LoginActivity) getMvpView();
                        int rest = result.getData().getProtectInMinutes();
                        String title =activity.getResources().getString(R.string.verify_password_failed_title,rest);
                        getMvpView().showTipDialog(title,activity.getString(R.string.verify_password_failed_stop));
                     }else{
                        getMvpView().onError("请输入正确的登录密码");
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                    button.setEnabled(true);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().onError("网络错误");
                button.setEnabled(true);
            }
        });
    }

    @Override
    public void register(String code, String mobile,
                         String password, Long schoolId,
                         String androidId, String brand,
                         String model, String systemVersion,
                         String appVersion , Button button) {
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
                    loginDataManager.setAccessToken(result.getData().getAccessToken());
                    loginDataManager.setRefreshToken(result.getData().getRefreshToken());
                    loginDataManager.setUserInfo(result.getData().getUser().transform());
                    getMvpView().onSuccess(R.string.register_success);
                    loginDataManager.setRememberMobile(mobile);
                    getMvpView().gotoMainView();
                    button.setEnabled(true);
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

    @Override
    public void getVerification(String mobile , Button button) {
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
                        button.setEnabled(true);
                    }
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
                }
        );
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
    public void checkAlipayPhoneBind(final String mobile, String code) {
        this.moblie = mobile;
        this.code = code;
        AlipayBindReq req = new AlipayBindReq();
        req.setMobile(mobile);
        req.setCode(code);
        req.setAlipayUserId(alipayUserId);
        req.setEdition(1);
        addObserver(loginDataManager.alipayCheckPhoneBind(req),new NetworkObserver<ApiResult<LoginRespDTO>>() {

            @Override
            public void onReady(ApiResult<LoginRespDTO> result) {
                //手机注册过，绑定成功直接登录
                if (result.getError() == null && result.getData().getResult()){
                    loginDataManager.setUserInfo(result.getData().getUser().transform());
                    loginDataManager.setAccessToken(result.getData().getAccessToken());
                    loginDataManager.setRefreshToken(result.getData().getRefreshToken());
                    loginDataManager.setRememberMobile(result.getData().getUser().getMobile());
                    loginDataManager.setIsFirstAfterLogin(true);
                    ((LoginActivity) getMvpView()).setThirdLogin(false);
                    getMvpView().onSuccess(R.string.login_success);
                    getMvpView().gotoMainView();
                }else if(result.getError() == null && !result.getData().getResult()){
                    //手机没有注册过，跳转到注册页面
                    getMvpView().gotoRegisterStep2View();
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().onError("网络出错");
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

    @Override
    public String getAlipayAuthInfo() {
       addObserver(loginDataManager.getApayAuth(), new NetworkObserver<ApiResult<AlipayAuthInfoRespDTO>>() {

            @Override
            public void onReady(ApiResult<AlipayAuthInfoRespDTO> result) {
                if (result.getError() == null){
                    alipayAuth(result.getData().getAuthInfo(),(LoginActivity)getMvpView());
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().onError("网络出错");
            }
        });

        return null;
    }

    private void alipayAuth(final String authInfo, final Activity activity){
        Log.e(TAG, "apayAuth: " + authInfo );
        Observable.create(new Observable.OnSubscribe<AuthResult>(){

            @Override
            public void call(Subscriber<? super AuthResult> subscriber) {
                AuthTask authTask = new AuthTask(activity);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);
                AuthResult authResult = new AuthResult(result, true);
                subscriber.onNext(authResult);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((authResult) -> {
                        String resultStatus = authResult.getResultStatus();
                        // 判断resultStatus 为“9000”且result_code
                        // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                        if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                            String authCode = authResult.getAuthCode();
                            //传递给服务端获取其他信息
                            getMvpView().alipayLogin(authCode);

                        }else if(TextUtils.equals(resultStatus, "6001") && null == authResult.getResultCode()){
                            getMvpView().onError("已退出支付宝授权");
                        } else {
                            // 其他状态值则为授权失败
                            getMvpView().onError("支付宝授权失败");

                        }
                    }
                );
    }

    @Override
    public void alipayLogin(String authCode , String androidId,
                               String brand, String model,
                               String systemVersion, String appVersion) {
        AlipayBindReq req = new AlipayBindReq();
        req.setAuthCode(authCode);
        req.setEdition(1);

        addObserver(loginDataManager.apayLogin(req), new NetworkObserver<ApiResult<LoginRespDTO>>() {

            @Override
            public void onReady(ApiResult<LoginRespDTO> result) {
                if (null == result.getError() && result.getData().isBinding()) {
                    //该支付宝账号之前已经绑定过，直接登录
                    loginDataManager.setUserInfo(result.getData().getUser().transform());
                    loginDataManager.setRefreshToken(result.getData().getRefreshToken());
                    loginDataManager.setAccessToken(result.getData().getAccessToken());
                    loginDataManager.setRememberMobile(result.getData().getUser().getMobile());
                    loginDataManager.setIsFirstAfterLogin(true);
                    getMvpView().onSuccess(R.string.login_success);
                    getMvpView().gotoMainView();
                } else if (null == result.getError() && !result.getData().isBinding()){
                    //绑定手机号，支付宝没有绑定过，跳转到绑定手机页面
                    ((LoginActivity) getMvpView()).setThirdLogin(true);
                    alipayUserId = result.getData().getAlipayUserId();
                    getMvpView().gotoRegisterStep1View();
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().onError("网络出错");
            }
        });
    }

    public void registerAlipay(String password,Long schoolId , Button button){
        AlipayBindReq req = new AlipayBindReq();
        req.setPassword(password);
        req.setSchoolId(schoolId.intValue());
        req.setMobile(moblie);
        req.setCode(code);
        req.setEdition(1);
        req.setAlipayUserId(alipayUserId);
        addObserver(loginDataManager.registerAlipay(req),new NetworkObserver<ApiResult<LoginRespDTO>>(){

            @Override
            public void onReady(ApiResult<LoginRespDTO> result) {
                if (null == result.getError() && result.getData().isBinding()) {
                    //该支付宝账号之前已经绑定过，直接登录
                    loginDataManager.setUserInfo(result.getData().getUser().transform());
                    loginDataManager.setAccessToken(result.getData().getAccessToken());
                    loginDataManager.setRefreshToken(result.getData().getRefreshToken());
                    loginDataManager.setRememberMobile(result.getData().getUser().getMobile());
                    loginDataManager.setIsFirstAfterLogin(true);
                    ((LoginActivity) getMvpView()).setThirdLogin(false);
                    getMvpView().onSuccess(R.string.login_success);
                    getMvpView().gotoMainView();
                    button.setEnabled(true);
                } else if (null == result.getError() && !result.getData().isBinding()){
                    getMvpView().onError("注册失败请重试");
                    button.setEnabled(true);
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                    button.setEnabled(true);
                }

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().onError("网络出错");
                button.setEnabled(true);
            }
        });

    }
 //wechat login begin

    @Override
    public void getWeChatCode() {
        new Thread(()->{
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "amigo_wx_login";
            MvpApp.mWxApi.sendReq(req);
        }).start();
    }

    @Override
    public void weChatLogin(String code) {
        WechatLoginReqDTO dto = new WechatLoginReqDTO();
        dto.setCode(code);
        dto.setAppSource(1);
        dto.setEdition(1);

        addObserver(loginDataManager.weChatLogin(dto),new NetworkObserver<ApiResult<LoginRespDTO>>() {

            @Override
            public void onReady(ApiResult<LoginRespDTO> result) {
                if(null == result.getError()){
                    //微信和账号已经绑定过，直接登录
                    if (result.getData().getBound()){
                        loginDataManager.setUserInfo(result.getData().getUser().transform());
                        loginDataManager.setAccessToken(result.getData().getAccessToken());
                        loginDataManager.setRefreshToken(result.getData().getRefreshToken());
                        loginDataManager.setRememberMobile(result.getData().getUser().getMobile());
                        loginDataManager.setIsFirstAfterLogin(true);
                        ((LoginActivity) getMvpView()).setThirdLogin(false);
                        getMvpView().onSuccess(R.string.login_success);
                        getMvpView().gotoMainView();
                    }else{//否则绑定手机账号
                        ((LoginActivity) getMvpView()).setThirdLogin(true);
                        weChatOpenId = result.getData().getOpenId();
                        weChatUnionId = result.getData().getUnionId();
                        getMvpView().gotoRegisterStep1View();
                    }
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

        } );
    }


 //we chat login end
   public void checkWechatPhoneBind(String mobile,String code){
       this.moblie = mobile;
       this.code = code;

       WeChatBindPhoneReqDTO dto = new WeChatBindPhoneReqDTO();
       dto.setCode(code);
       dto.setMobile(mobile);
       dto.setOpenId(weChatOpenId);
       dto.setUnionId(weChatUnionId);
       dto.setAppSource(1);
       dto.setEdition(1);

       addObserver(loginDataManager.weChatCheckPhoneBind(dto),new NetworkObserver<ApiResult<LoginRespDTO>>() {

           @Override
           public void onReady(ApiResult<LoginRespDTO> result) {
               if(null == result.getError()){
                   //绑定电话成功直接登录
                   if (null != result.getData().getResult() && result.getData().getResult()){
                       loginDataManager.setUserInfo(result.getData().getUser().transform());
                       loginDataManager.setAccessToken(result.getData().getAccessToken());
                       loginDataManager.setRefreshToken(result.getData().getRefreshToken());
                       loginDataManager.setRememberMobile(result.getData().getUser().getMobile());
                       loginDataManager.setIsFirstAfterLogin(true);
                       ((LoginActivity) getMvpView()).setThirdLogin(false);
                       getMvpView().onSuccess(R.string.login_success);
                       getMvpView().gotoMainView();

                   }else {
                       //验证码有效，但是电话号码没有注册，进入注册流程
                       getMvpView().gotoRegisterStep2View();
                   }

               }else{
                   getMvpView().onError(result.getError().getDisplayMessage());
               }
           }
       });
   }

    @Override
    public void registerWeChat(String password, Long schoolId , Button button) {
        WeChatResiterReqDTO req = new WeChatResiterReqDTO();
        req.setPassword(password);
        req.setSchoolId(schoolId.intValue());
        req.setMobile(moblie);
        req.setCode(code);
        req.setUnionId(weChatUnionId);
        req.setOpenId(weChatOpenId);
        req.setAppSource(1);
        req.setEdition(1);

        addObserver(loginDataManager.registerWeChat(req),new NetworkObserver<ApiResult<LoginRespDTO>>(){

            @Override
            public void onReady(ApiResult<LoginRespDTO> result) {
                if (null == result.getError()){
                    if (result.getData().getWechatBound()){
                        loginDataManager.setUserInfo(result.getData().getUser().transform());
                        loginDataManager.setAccessToken(result.getData().getAccessToken());
                        loginDataManager.setRefreshToken(result.getData().getRefreshToken());
                        loginDataManager.setRememberMobile(result.getData().getUser().getMobile());
                        loginDataManager.setIsFirstAfterLogin(true);
                        ((LoginActivity) getMvpView()).setThirdLogin(false);
                        getMvpView().onSuccess(R.string.login_success);
                        getMvpView().gotoMainView();
                        button.setEnabled(true);
                    }else{
                        getMvpView().onError("注册失败请重试");
                        button.setEnabled(true);
                    }
                }else{
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

    @Override
    public String getRemindMobile() {
        return loginDataManager.getRememberMobile();
    }

    @Override
    public void checkUpdate(int code, String versionNo, String remindMobile) {
        CheckVersionUpdateReqDTO reqDTO = new CheckVersionUpdateReqDTO();
        reqDTO.setCode(code);
        reqDTO.setVersionNo(versionNo);
        reqDTO.setMobile(remindMobile);
        addObserver(loginDataManager.checkUpdate(reqDTO),
                new NetworkObserver<ApiResult<CheckVersionUpdateRespDTO>>(false) {


                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onReady(ApiResult<CheckVersionUpdateRespDTO> result) {
                        if (null == result.getError()) {
                            if (result.getData().getResult()) {
                                if (result.getData().getVersion().isMustUpdate()) {
                                    getMvpView().showUpdateDialog(result.getData().getVersion());

                                    // 清除所有记录的更新时间，目的是为了强更之后的下一个普通更新必弹窗
                                    loginDataManager.clearUpdateRemindTime();
                                } else {
                                    // 小于6小时不再提醒
                                    VersionDialogTime versionDialogTime = loginDataManager.getLastUpdateRemindTime() ;
                                    if (CommonUtil.canShowUpdateDialog(versionDialogTime ,remindMobile)){
                                        getMvpView().showUpdateDialog(result.getData().getVersion());
                                        loginDataManager.setLastUpdateRemindTime(remindMobile);
                                    }
                                }

                            }

                        }
                    }
                });
    }



}
