package com.xiaolian.amigo.ui.user;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.user.intf.IEditPasswordPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditPasswordView;
import com.xiaolian.amigo.ui.widget.ClearableEditText;
import com.xiaolian.amigo.ui.widget.dialog.AvailabilityDialog;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.ViewUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

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

    AvailabilityDialog availabilityDialog;

    @OnClick(R.id.bt_submit)
    void onSubmitClick() {
        btSubmit.setEnabled(false);
        if (TextUtils.equals(etNewPassword.getText(), etNewPasswordAgain.getText())) {
            if (TextUtils.equals(etNewPassword.getText() ,etOldPassword.getText())){
                showMessage("新密码与原密码一致");
                btSubmit.setEnabled(true);
            }else {
                presenter.updatePassword(etNewPassword.getText().toString().trim(),
                        etOldPassword.getText().toString().trim(), btSubmit);
            }
        } else {
            showMessage("两次输入的密码不一致");
            btSubmit.setEnabled(true);
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


    @OnTextChanged({ R.id.et_new_password , R.id.et_new_password_again , R.id.et_old_password} )
    public void textChange(){
        btSubmit.setEnabled(editLengthCheckout(etNewPassword) && editLengthCheckout(etNewPasswordAgain)
        && etOldPassword.length() > 1);
    }

    /**
     * 新密码长度验证
     * @return
     */
    private boolean editLengthCheckout(EditText editText){
        if (editText == null) return false ;

        return editText.getText().length()>= 6;
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
}
