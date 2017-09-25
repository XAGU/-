package com.xiaolian.amigo.ui.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.component.ClearableEditText;
import com.xiaolian.amigo.ui.user.intf.IEditPasswordPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditPasswordView;
import com.xiaolian.amigo.util.ViewUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改密码页面
 * @author zcd
 */

public class EditPasswordActivity extends UserBaseActivity implements IEditPasswordView {

    @Inject
    IEditPasswordPresenter<IEditPasswordView> presenter;

    @BindView(R.id.bt_submit)
    Button bt_submit;

    @BindView(R.id.et_new_password)
    ClearableEditText et_new_password;

    @BindView(R.id.et_old_password)
    ClearableEditText et_old_password;

    @BindView(R.id.et_new_password_again)
    ClearableEditText et_new_password_again;

    @OnClick(R.id.bt_submit)
    void onSubmitClick() {
        if (TextUtils.equals(et_new_password.getText(), et_new_password_again.getText())) {
            presenter.updatePassword(et_new_password.getText().toString(), et_old_password.getText().toString());
        } else {
            showMessage("两次输入的密码不一致");
        }
    }


    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);

        presenter.onAttach(EditPasswordActivity.this);

        ViewUtil.setEditHintAndSize(getString(R.string.please_enter_old_password),
                14, et_old_password);
        ViewUtil.setEditHintAndSize(getString(R.string.please_enter_new_password),
                14, et_new_password);
        ViewUtil.setEditHintAndSize(getString(R.string.please_enter_new_password_again),
                14, et_new_password_again);

    }

    @Override
    protected int setTitle() {
        return R.string.edit_password;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_edit_password;
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
    @Override
    protected void setUp() {

    }

    @Override
    public void finishView() {
        finish();
    }
}
