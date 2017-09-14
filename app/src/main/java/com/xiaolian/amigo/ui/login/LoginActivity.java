package com.xiaolian.amigo.ui.login;

import android.os.Bundle;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.di.componet.LoginActivityComponent;
import com.xiaolian.amigo.ui.base.BaseActivity;
import com.xiaolian.amigo.ui.login.intf.LoginPresenterIntf;
import com.xiaolian.amigo.ui.login.intf.LoginViewIntf;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by caidong on 2017/9/14.
 */

public class LoginActivity extends LoginBaseActivity implements LoginViewIntf {

    @Inject
    LoginPresenterIntf<LoginViewIntf> mPresenter;
    @BindView(R.id.et_mobile)
    TextView et_mobile;
    @BindView(R.id.et_userpwd)
    TextView et_userpwd;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);

        mPresenter.onAttach(LoginActivity.this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    // 登录
    @OnClick(R.id.bt_submit)
    void login() {
        mPresenter.onLoginClick(et_mobile.getText().toString(), et_userpwd.getText().toString());
    }
}
