package com.xiaolian.amigo.ui.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ObjectsCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.android.tpush.XGPushManager;
import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.login.intf.ILoginPresenter;
import com.xiaolian.amigo.ui.login.intf.ILoginView;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.user.EditProfileActivity;
import com.xiaolian.amigo.ui.widget.dialog.AvailabilityDialog;
import com.xiaolian.amigo.util.AppUtils;
import com.xiaolian.amigo.util.MD5Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiaolian.amigo.util.Log.getContext;

/**
 * 登录页
 *
 * @author caidong
 * @date 17/9/14
 */

public class LoginActivity extends LoginBaseActivity implements ILoginView {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final String FRAGMENT_TAG_LOGIN = "login";
    private static final String FRAGMENT_TAG_REGISTER_STEP_1 = "registerStep1";
    private static final String FRAGMENT_TAG_REGISTER_STEP_2 = "registerStep2";

    public static final int THIRD_LOGIN_ALIPAY = 0;
    public static final int THIRD_LOGIN_WECHAT = 1;
    //正常账号登录
    public static final int LOGIN_NORMAL = 2;

    private int currentLoginType = -1;

    private static final int SDK_AUTH_FLAG = 2;
    @Inject
    ILoginPresenter<ILoginView> presenter;
//    @BindView(R.id.etMobile)
//    TextView etMobile;
//    @BindView(R.id.etUserpwd)
//    TextView etUserpwd;

    @BindView(R.id.tv_login)
    TextView tvLogin;

    @BindView(R.id.tv_registry)
    TextView tvRegistry;

    @BindView(R.id.view_line)
    View view;

    @BindView(R.id.third_login)
    RelativeLayout thirdLogin;

    @BindView(R.id.bt_apay_login)
    ImageView apay_login;

    @BindView(R.id.bt_wechat)
    ImageView wechat;




    LoginFragment loginFragment;
    RegisterStep1Fragment registerStep1Fragment;
    RegisterStep2Fragment registerStep2Fragment;

    AvailabilityDialog availabilityDialog;

    //如果是登录后，不对EventBus事件进行处理
    private boolean isLogined;

    private String mobile;
    private String code;

    private boolean isThirdLogin;

    public boolean isChooseSchool() {
        return chooseSchool;
    }

    public void setChooseSchool(boolean chooseSchool) {
        this.chooseSchool = chooseSchool;
    }

    private boolean chooseSchool;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registry_group);

        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);

        presenter.onAttach(LoginActivity.this);

        Long schoolId = presenter.getSchoolId();
        if (!ObjectsCompat.equals(schoolId, -1)) {
            String pushTag = MD5Util.md5(schoolId + "_MTxQd1buFokZayzT");
            XGPushManager.deleteTag(getApplicationContext(), pushTag);
        }
        String pushTag = presenter.getPushTag();
        if (!TextUtils.isEmpty(pushTag)) {
            Log.d(TAG, "删除tag" + pushTag);
            XGPushManager.deleteTag(getApplicationContext(), pushTag);
            presenter.setPushTag("");
        }

        presenter.deletePushToken();
        presenter.logout();

        if (savedInstanceState == null) {
            loginFragment = new LoginFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.ll_main, loginFragment).commit();
        } else {
            LoginFragment login = (LoginFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_LOGIN);
            RegisterStep1Fragment registerStep1 =
                    (RegisterStep1Fragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_REGISTER_STEP_1);
            RegisterStep2Fragment registerStep2 =
                    (RegisterStep2Fragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_REGISTER_STEP_2);
            if (login != null && registerStep1 != null && registerStep2 != null) {
                getSupportFragmentManager().beginTransaction()
                        .show(login)
                        .hide(registerStep1)
                        .hide(registerStep2)
                        .commit();
            }
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }
    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.tv_registry)
    void gotoRegisterStep1() {

        if(isThirdLogin){
            tvRegistry.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            Log.e(TAG, "gotoRegisterStep1: set" );
            tvLogin.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDark2));
            tvLogin.setText("绑定手机号");
        }else{
            tvLogin.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTextGray));
            tvRegistry.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDark2));
        }

        if (registerStep1Fragment == null) {
            registerStep1Fragment = new RegisterStep1Fragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (loginFragment != null && loginFragment.isAdded()) {
            transaction = transaction.hide(loginFragment);
        }
        if (registerStep2Fragment != null && registerStep2Fragment.isAdded()) {
            transaction = transaction.hide(registerStep2Fragment);
        }
        if (!registerStep1Fragment.isAdded()) {
            transaction.add(R.id.ll_main, registerStep1Fragment, FRAGMENT_TAG_REGISTER_STEP_1).commit();
        } else {
            transaction.show(registerStep1Fragment).commit();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.tv_login)
    void gotoLogin() {

        showThirdLoginView(true);

        if(isThirdLogin)
            return;

        if (loginFragment == null) {
            loginFragment = new LoginFragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (registerStep1Fragment != null && registerStep1Fragment.isAdded()) {
            transaction = transaction.hide(registerStep1Fragment);
        }
        if (registerStep2Fragment != null && registerStep2Fragment.isAdded()) {
            transaction = transaction.hide(registerStep2Fragment);
        }

        if (!loginFragment.isAdded()) {
            transaction.add(R.id.ll_main, loginFragment, FRAGMENT_TAG_LOGIN).commit();
        } else {
            transaction.show(loginFragment).commit();
        }
        tvLogin.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDark2));
        tvRegistry.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTextGray));
    }

    public void gotoRegisterStep2() {
        if(isThirdLogin){
            tvRegistry.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            tvLogin.setText("设置密码");
            tvLogin.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDark2));
        }

        if (registerStep2Fragment == null) {
            registerStep2Fragment = new RegisterStep2Fragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (loginFragment != null && loginFragment.isAdded()) {
            transaction = transaction.hide(loginFragment);
        }
        if (registerStep1Fragment != null && registerStep1Fragment.isAdded()) {
            transaction = transaction.hide(registerStep1Fragment);
        }
        if (!registerStep2Fragment.isAdded()) {
            transaction.add(R.id.ll_main, registerStep2Fragment, FRAGMENT_TAG_REGISTER_STEP_2).commit();
        } else {
            transaction.show(registerStep2Fragment).commit();
        }
    }

    @Override
    public void gotoLoginView() {
        gotoLogin();
    }

    @Override
    public void gotoRegisterStep2View() {
        gotoRegisterStep2();
    }

    @Override
    public void gotoRegisterStep1View() {
        gotoRegisterStep1();
    }

    @Override
    public void startTimer() {
        if (registerStep1Fragment != null) {
            registerStep1Fragment.startTimer();
        }
    }

    @Override
    public void gotoMainView() {
        presenter.setIsFirstAfterLogin(true);
        isLogined = true;
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void sendVerificationCode(String mobile) {
        presenter.getVerification(mobile);
    }

    public void checkVerificationCode(String mobile, String code) {
        presenter.checkVerification(mobile, code);
    }

    public void registerForThirdAccount(String password,Long schoolId){
        switch (currentLoginType){
            case THIRD_LOGIN_ALIPAY :
                presenter.registerAlipay(password, schoolId);
                break;
            case THIRD_LOGIN_WECHAT :
                presenter.registerWeChat(password, schoolId);
                break;
            default:
                break;

        }
    }

    public void setMobileAndCode(String mobile, String code) {
        this.mobile = mobile;
        this.code = code;
    }

    public void register(String password, Long schoolId) {
        @SuppressLint("HardwareIds")
        String androidId = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String model = Build.MODEL;
        String brand = Build.BRAND;
        int systemVersion = Build.VERSION.SDK_INT;
        String appVersion = AppUtils.getVersionName(this);
        presenter.register(this.code, this.mobile, password, schoolId,
                androidId, brand, model, String.valueOf(systemVersion), appVersion);
    }

    public void login(String mobile, String password) {
        currentLoginType = LOGIN_NORMAL;
        @SuppressLint("HardwareIds")
        String androidId = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String model = Build.MODEL;
        String brand = Build.BRAND;
        int systemVersion = Build.VERSION.SDK_INT;
        String appVersion = AppUtils.getVersionName(this);
        presenter.onLoginClick(mobile, password, androidId, brand,
                model, String.valueOf(systemVersion), appVersion);
    }

    private void weChatLogin(){
        currentLoginType = THIRD_LOGIN_WECHAT;
        presenter.getWeChatCode();
    }

    @Override
    public void alipayLogin(String authCode){
        currentLoginType = THIRD_LOGIN_ALIPAY;

        @SuppressLint("HardwareIds")
        String androidId = Settings.Secure.getString(getContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);

        String model = Build.MODEL;
        String brand = Build.BRAND;
        int systemVersion = Build.VERSION.SDK_INT;
        String appVersion = AppUtils.getVersionName(this);
        presenter.alipayLogin(authCode,androidId,brand,model,String.valueOf(systemVersion), appVersion);

    }

    public void ThirdLoginPhoneBind(String mobile,String code){
        switch (currentLoginType){
            case THIRD_LOGIN_ALIPAY :
                presenter.checkAlipayPhoneBind(mobile, code);
                break;
            case THIRD_LOGIN_WECHAT :
                presenter.checkWechatPhoneBind(mobile,code);
                break;
             default:
               break;

        }
    }

    public String getMobile() {
        return presenter.getMobile();
    }

    public void showLoginAndRegister(){
        tvRegistry.setVisibility(View.VISIBLE);
        view.setVisibility(View.VISIBLE);
        tvLogin.setText("密码登录");
        tvRegistry.setText("账号注册");

    }

    @Override
    public void showTipDialog(String title,String content) {
        if (null == availabilityDialog) {
            availabilityDialog = new AvailabilityDialog(this);
        }
        if (availabilityDialog.isShowing()) {
            return;
        }
        availabilityDialog.setCancelVisible(false);
        availabilityDialog.setOkText(getString(R.string.confirm));
        availabilityDialog.setTitle(title);
        availabilityDialog.setTip(content);
        availabilityDialog.setOnOkClickListener(dialog1 -> {

        });
        availabilityDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EditProfileActivity.Event event) {
        if (isLogined) return;

        switch (event.getType()){
            case WECHAT_CODE :
                String weChatCode = (String)event.getMsg();
                presenter.weChatLogin(weChatCode);
                break;
            default:
                break;
        }

    }

    public void showThirdLoginView(boolean enable){
            thirdLogin.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    public void setThirdLogin(boolean login){
        this.isThirdLogin = login;

    }

    public boolean isThirdLogin(){
        return isThirdLogin;
    }

    @OnClick(R.id.bt_wechat)
    void weCharLogin(){
        if (!MvpApp.mWxApi.isWXAppInstalled()) {
            onError("未安装微信，请安装");
            return;
        }
        weChatLogin();
    }

    @OnClick(R.id.bt_apay_login)
    void apayLogin(){
        presenter.getAlipayAuthInfo();
    }

}
