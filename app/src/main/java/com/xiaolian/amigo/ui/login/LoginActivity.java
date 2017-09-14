package com.xiaolian.amigo.ui.login;

import android.content.Intent;
import android.os.Bundle;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by caidong on 2017/9/14.
 */

public class LoginActivity extends BaseActivity implements LoginMvpView {

    @Inject
    LoginMvpPresenter<LoginMvpView> mPresenter;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        setUnBinder(ButterKnife.bind(this));

//        getActivityComponent().inject(this);
//
//        mPresenter.onAttach(LoginActivity.this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }
}
