package com.xiaolian.amigo.ui.user;

import android.text.TextUtils;
import android.widget.Button;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.user.intf.IEditPasswordPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditPasswordView;
import com.xiaolian.amigo.ui.widget.ClearableEditText;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.ViewUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改密码页面
 *
 * @author zcd
 * @date 17/9/15
 */

public class EditPasswordActivity extends UserBaseActivity implements IEditPasswordView {

    @Inject
    IEditPasswordPresenter<IEditPasswordView> presenter;

    @BindView(R.id.bt_submit)
    Button btSubmit;

    @BindView(R.id.et_new_password)
    ClearableEditText etNewPassword;

    @BindView(R.id.et_old_password)
    ClearableEditText etOldPassword;

    @BindView(R.id.et_new_password_again)
    ClearableEditText etNewPasswordAgain;

    @OnClick(R.id.bt_submit)
    void onSubmitClick() {
        if (TextUtils.equals(etNewPassword.getText(), etNewPasswordAgain.getText())) {
            presenter.updatePassword(etNewPassword.getText().toString().trim(),
                    etOldPassword.getText().toString().trim());
        } else {
            showMessage("两次输入的密码不一致");
        }
    }


    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);
        setMainBackground(R.color.colorBackgroundGray);

        presenter.onAttach(EditPasswordActivity.this);

        ViewUtil.setEditHintAndSize(getString(R.string.please_enter_old_password),
                14, etOldPassword);
        ViewUtil.setEditHintAndSize(getString(R.string.please_enter_new_password),
                14, etNewPassword);
        ViewUtil.setEditHintAndSize(getString(R.string.please_enter_new_password_again),
                14, etNewPasswordAgain);

        ViewUtil.setEditPasswordInputFilter(etOldPassword);
        ViewUtil.setEditPasswordInputFilter(etNewPassword);
        ViewUtil.setEditPasswordInputFilter(etNewPasswordAgain);
        CommonUtil.showSoftInput(this, etOldPassword);
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
