

package com.xiaolian.amigo.ui.user;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.user.intf.IPasswordVerifyPresenter;
import com.xiaolian.amigo.ui.user.intf.IPasswordVerifyView;
import com.xiaolian.amigo.ui.widget.dialog.AvailabilityDialog;
import com.xiaolian.amigo.util.ViewUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PasswordVerifyActivity extends UserBaseActivity implements IPasswordVerifyView {
    public static final int TYPE_CHANGE_PHONE = 0;
    public static final int TYPE_MONEY_RETURN = 1;

    @BindView(R.id.phone_title)
    TextView tv_phone_number;

    @BindView(R.id.et_old_password_pn)
    com.xiaolian.amigo.ui.widget.PasswordEditText login_password;

    @BindView(R.id.bt_submit)
    Button submit;

    private String oldPhoneNumber;

    AvailabilityDialog availabilityDialog;
    //定义进入密码验证界面类型 0 修改手机号验证密码  1 退款验证密码
    int type = -1;

    @Inject
    IPasswordVerifyPresenter<IPasswordVerifyView> presenter;


    @Override
    protected void initView() {
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        setUnBinder(ButterKnife.bind(this));
        ViewUtil.setEditPasswordInputFilter(login_password);

        switch (type) {
            case TYPE_CHANGE_PHONE:
                if(!TextUtils.isEmpty(oldPhoneNumber)){
                    showOldPhoneNumber(oldPhoneNumber);
                }
                setToolBarTitle(R.string.change_phone_number);
                break;
            case TYPE_MONEY_RETURN:
                tv_phone_number.setVisibility(View.GONE);
                setToolBarTitle(R.string.password_verify);
                break;

             default:
             break;
        }
        login_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s) && s.toString().length() > 0){
                    submit.setEnabled(true);
                }else{
                    submit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected int setTitle() {
        return  R.string.password_verify;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_verify_passoword;
    }

    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null) {
            oldPhoneNumber = getIntent().getStringExtra("phoneNumber");
            type = getIntent().getIntExtra("type",-1);
        }
    }


    private void showOldPhoneNumber(String str) {
        // String sFinalAge=getResources().getString(R.string.alert,nAge);
        String hide = "****";
        StringBuilder sb = new StringBuilder(str);
        sb.replace(3, 7, hide);

        String num = "当前绑定手机号: " + sb.toString();
        tv_phone_number.setText(num);
    }

    @OnClick(R.id.bt_submit)
    void verify_password(View v){
        submit.setEnabled(false);
        if (!TextUtils.isEmpty(login_password.getText().toString().trim())){
            presenter.verifyPassword(login_password.getText().toString().trim() , submit);
        } else {
            onError("请输入登录密码");
            submit.setEnabled(true);
        }

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
    public void goToChangeView() {
        switch (type) {
            case TYPE_CHANGE_PHONE:
                Intent intent = new Intent(this,ChangePhoneActivity.class);
                startActivity(intent);
                break;
            case TYPE_MONEY_RETURN:
                setResult(RESULT_OK);
                break;
             default:
                 break;
        }
        this.finish();
    }
}
