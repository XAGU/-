package com.xiaolian.amigo.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.login.intf.ILoginPresenter;
import com.xiaolian.amigo.ui.login.intf.ILoginView;
import com.xiaolian.amigo.ui.main.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录页
 * Created by caidong on 2017/9/14.
 */

public class LoginActivity extends LoginBaseActivity implements ILoginView {
    private static final String FRAGMENT_TAG_LOGIN = "login";
    private static final String FRAGMENT_TAG_REGISTER_STEP_1 = "registerStep1";
    private static final String FRAGMENT_TAG_REGISTER_STEP_2 = "registerStep2";

    @Inject
    ILoginPresenter<ILoginView> presenter;
//    @BindView(R.id.et_mobile)
//    TextView et_mobile;
//    @BindView(R.id.et_userpwd)
//    TextView et_userpwd;

    @BindView(R.id.tv_login)
    TextView tv_login;

    @BindView(R.id.tv_registry)
    TextView tv_registry;

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

        tv_login.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTextGray));
        tv_registry.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDark2));

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
        tv_login.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDark2));
        tv_registry.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTextGray));
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
        presenter.register(this.code, this.mobile, password, schoolId);
    }

    public void login(String mobile, String password) {
        presenter.onLoginClick(mobile, password);
    }

    public String getMobile() {
        return presenter.getMobile();
    }

}
