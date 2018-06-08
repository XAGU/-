package com.xiaolian.amigo.ui.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ObjectsCompat;
import android.widget.TextView;

import com.tencent.android.tpush.XGPushManager;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.login.intf.ILoginPresenter;
import com.xiaolian.amigo.ui.login.intf.ILoginView;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.util.AppUtils;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.MD5Util;

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
    private static final String FRAGMENT_TAG_LOGIN = "login";
    private static final String FRAGMENT_TAG_REGISTER_STEP_1 = "registerStep1";
    private static final String FRAGMENT_TAG_REGISTER_STEP_2 = "registerStep2";

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

    LoginFragment loginFragment;
    RegisterStep1Fragment registerStep1Fragment;
    RegisterStep2Fragment registerStep2Fragment;

    private String mobile;
    private String code;

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
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @OnClick(R.id.tv_registry)
    void gotoRegisterStep1() {
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

        tvLogin.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTextGray));
        tvRegistry.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDark2));

    }

    @OnClick(R.id.tv_login)
    void gotoLogin() {
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
    public void startTimer() {
        if (registerStep1Fragment != null) {
            registerStep1Fragment.startTimer();
        }
    }

    @Override
    public void gotoMainView() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void sendVerificationCode(String mobile) {
        presenter.getVerification(mobile);
    }

    public void checkVerificationCode(String mobile, String code) {
        presenter.checkVerification(mobile, code);
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

    public String getMobile() {
        return presenter.getMobile();
    }

}
