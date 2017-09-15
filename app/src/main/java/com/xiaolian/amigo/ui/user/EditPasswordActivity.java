package com.xiaolian.amigo.ui.user;

import android.os.Bundle;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.base.BaseActivity;
import com.xiaolian.amigo.ui.user.intf.IEditPasswordPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditPasswordView;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 修改密码页面
 * @author zcd
 */

public class EditPasswordActivity extends UserBaseActivity implements IEditPasswordView {

    @Inject
    IEditPasswordPresenter<IEditPasswordView> mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);

        mPresenter.onAttach(EditPasswordActivity.this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }
    @Override
    protected void setUp() {

    }
}
