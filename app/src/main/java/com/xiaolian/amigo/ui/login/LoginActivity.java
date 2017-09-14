package com.xiaolian.amigo.ui.login;

import android.os.Bundle;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.login.intf.ILoginPresenter;
import com.xiaolian.amigo.ui.login.intf.ILoginView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;

/**
 * Created by caidong on 2017/9/14.
 */

public class LoginActivity extends LoginBaseActivity implements ILoginView {

    @Inject
    ILoginPresenter<ILoginView> mPresenter;
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
