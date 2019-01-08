package com.xiaolian.amigo.ui.user;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.AuthTask;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.alipay.AliPayBindInAppReq;
import com.xiaolian.amigo.data.network.model.alipay.AlipayAuthInfoRespDTO;
import com.xiaolian.amigo.data.network.model.alipay.AuthResult;
import com.xiaolian.amigo.data.network.model.common.ApplySchoolCheckRespDTO;
import com.xiaolian.amigo.data.network.model.login.EntireUserDTO;
import com.xiaolian.amigo.data.network.model.login.WeChatBindRespDTO;
import com.xiaolian.amigo.data.network.model.login.WechatLoginReqDTO;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditProfilePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditProfileView;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * EditProfilePresenter实现类
 *
 * @author zcd
 * @date 17/9/15
 */

public class EditProfilePresenter<V extends IEditProfileView> extends BasePresenter<V>
        implements IEditProfilePresenter<V> {
    @SuppressWarnings("unused")
    private static final String TAG = EditProfilePresenter.class.getSimpleName();
    private IUserDataManager userDataManager;
    private boolean isHadSetBathPassword;

    @Inject
    EditProfilePresenter(IUserDataManager userDataManager) {
        super();
        this.userDataManager = userDataManager;
    }

    @Override
    public void getPersonProfile() {
        addObserver(userDataManager.getUserInfo(), new NetworkObserver<ApiResult<EntireUserDTO>>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onReady(ApiResult<EntireUserDTO> result) {
                if (null == result.getError()) {
                    getMvpView().setAvatar(result.getData().getPictureUrl());
                    getMvpView().setMobile(result.getData().getMobile());
                    getMvpView().setNickName(result.getData().getNickName());
                    getMvpView().setSchoolName(result.getData().getSchoolName());
                    getMvpView().setResidenceName(result.getData().getResidenceName());
                    getMvpView().showBathroomPassword(userDataManager.isExistBathroomBiz(), result.getData().isHadSetBathPassword());
                    if (result.getData().getAlipayBind() != null && result.getData().getAlipayBind().isIsBinding()){
                        String nick_name ;
                        if (null == result.getData().getAlipayBind().getAlipayNickName()){
                            nick_name = "未设置昵称";
                        }else{
                            nick_name = result.getData().getAlipayBind().getAlipayNickName();
                        }
                        getMvpView().showAliPayBind(nick_name);

                    }else{
                        getMvpView().showAliPayBind(null);
                    }

                    if(result.getData().getWechatBind() != null && result.getData().getWechatBind().getResult()) {
                        String nick_name ;
                        if (null == result.getData().getWechatBind().getNickname()){
                            nick_name = "未设置昵称";
                        }else{
                            nick_name = result.getData().getWechatBind().getNickname();
                        }
                        getMvpView().showWechatBind(nick_name);
                    }else{
                        getMvpView().showWechatBind(null);
                    }

                    isHadSetBathPassword = result.getData().isHadSetBathPassword();
                    userDataManager.setBathroomPassword();
                    if (result.getData().getSex() != null) {
                        getMvpView().setSex(result.getData().getSex());
                    }

                    User user = new User(result.getData());
                    userDataManager.setUser(user);
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    @Override
    public void checkChangeSchool() {
        addObserver(userDataManager.applySchoolCheck(), new NetworkObserver<ApiResult<ApplySchoolCheckRespDTO>>() {

            @Override
            public void onReady(ApiResult<ApplySchoolCheckRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isHasApply()) {
                        //提交过学校审核，直接进入审核页面
                        getMvpView().gotoChangeSchool(result.getData());

                    } else {
                        //没有提交过更改学校审核, 进入选择学校界面
                        getMvpView().gotoChooseSchool();
                    }

                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

   @Override
   public void getAlipayAuthInfo(){
       addObserver(userDataManager.getApayAuth(), new NetworkObserver<ApiResult<AlipayAuthInfoRespDTO>>() {

           @Override
           public void onReady(ApiResult<AlipayAuthInfoRespDTO> result) {
               if (result.getError() == null){
                   apayAuth(result.getData().getAuthInfo(),(EditProfileActivity)getMvpView());
               }else{
                   getMvpView().onError(result.getError().getDisplayMessage());
               }

           }

           @Override
           public void onError(Throwable e) {
               super.onError(e);
               getMvpView().onError("网络错误");
           }
       });
   }

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
    public void bindWeChat(String weChatCode) {
        WechatLoginReqDTO dto = new WechatLoginReqDTO();
        dto.setAppSource(1);
        dto.setCode(weChatCode);
        addObserver(userDataManager.bindWechatInApp(dto),new NetworkObserver<ApiResult<WeChatBindRespDTO>>() {

            @Override
            public void onReady(ApiResult<WeChatBindRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getResult()) {
                        WeChatBindRespDTO data = result.getData();
                        EditProfileActivity.Event event = new EditProfileActivity.Event(EditProfileActivity.Event.EventType.BIND_WECHAT);
                        if (null == data.getNickname()){
                            data.setNickname("未设置昵称");
                        }
                        event.setMsg(data);
                        EventBus.getDefault().post(event);
                    }else{
                        getMvpView().onError("绑定微信失败，请重试");
                    }

                }else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });

    }

    private void apayAuth(final String authInfo, final Activity activity){
        Log.e(TAG, "apayAuth: "+authInfo );
        Observable.just(authInfo)
                .map(s -> {
                    AuthTask authTask = new AuthTask(activity);
                    Map<String, String> result = authTask.authV2(authInfo, true);
                    AuthResult authResult = new AuthResult(result, true);
                    return authResult ;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authResult -> {
                    String resultStatus = authResult.getResultStatus();
                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        String authCode = authResult.getAuthCode();
                        //传递给服务端获取其他信息
                        bindAlipay(authCode);

                    } else {
                        // 其他状态值则为授权失败
                        getMvpView().onError("已退出支付宝授权");

                    }
                });
//        Observable.create((Observable.OnSubscribe<AuthResult>) subscriber -> {
//            AuthTask authTask = new AuthTask(activity);
//            // 调用授权接口，获取授权结果
//
//            subscriber.onNext(authResult);
//            Log.e(TAG, "apayAuth call: " );
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe((authResult) -> {
//
//                );
    }

    private void bindAlipay(String authCode){
        AliPayBindInAppReq req = new AliPayBindInAppReq();
        req.setAuthCode(authCode);
        addObserver(userDataManager.bindAlipayInApp(req),new NetworkObserver<ApiResult<User.AlipayBindBean>>(){

            @Override
            public void onReady(ApiResult<User.AlipayBindBean> result) {
                if(null == result.getError()){
                    if (result.getData().isIsBinding()) {
                        User.AlipayBindBean bean = result.getData();
                        EditProfileActivity.Event event = new EditProfileActivity.Event(EditProfileActivity.Event.EventType.BIND_ALIPAY);
                        if (null == bean.getAlipayNickName()){
                            bean.setAlipayNickName("未设置昵称");
                        }
                        event.setMsg(bean);
                        EventBus.getDefault().post(event);
                    }else {
                        getMvpView().onError("绑定支付宝失败请重试");
                    }
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().onError("网络错误");
            }
        });
    }

    @Override
    public boolean isHadSetBathPassword() {
        return isHadSetBathPassword;
    }

    @Override
    public int getCertificationStatus() {
       return  userDataManager.getCertifyStatus();
    }

}
