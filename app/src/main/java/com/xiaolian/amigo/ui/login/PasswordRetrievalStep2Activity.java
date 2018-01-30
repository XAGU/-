package com.xiaolian.amigo.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.login.intf.IPasswordRetrievalStep2Presenter;
import com.xiaolian.amigo.ui.login.intf.IPasswordRetrievalStep2View;
import com.xiaolian.amigo.util.ViewUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 找回密码步骤2
 *
 * @author caidong
 * @date 17/9/1
 */

public class PasswordRetrievalStep2Activity extends LoginBaseActivity implements IPasswordRetrievalStep2View {
    public static final String INTENT_KEY_PASSWORD_RETRIEVAL_CODE = "intent_key_password_retrieval_code";
    public static final String INTENT_KEY_PASSWORD_RETRIEVAL_MOBILE = "intent_key_password_retrieval_mobile";
    @Inject
    IPasswordRetrievalStep2Presenter<IPasswordRetrievalStep2View> presenter;

    @BindView(R.id.et_userpwd)
    TextView etUserpwd;
    @BindView(R.id.et_confirm_userpwd)
    TextView etConfirmUserpwd;
    @BindView(R.id.bt_submit)
    Button btSubmit;
    private String mobile;
    private String code;

    @OnClick(R.id.bt_submit)
    void resetPassword() {
        presenter.resetPassword(code, mobile, etUserpwd.getText().toString());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_retrieval_step2);

        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);

        presenter.onAttach(PasswordRetrievalStep2Activity.this);
        // 提交按钮初始化时禁用
        btSubmit.getBackground().setAlpha(100);
        btSubmit.setEnabled(false);

        ViewUtil.setEditHintAndSize(getString(R.string.password_hint), 14, etUserpwd);
        ViewUtil.setEditHintAndSize(getString(R.string.password_hint), 14, etConfirmUserpwd);

        if (getIntent() != null) {
            code = getIntent().getStringExtra(INTENT_KEY_PASSWORD_RETRIEVAL_CODE);
            mobile = getIntent().getStringExtra(INTENT_KEY_PASSWORD_RETRIEVAL_MOBILE);
        }
    }

    @Override
    protected void setUp() {

    }

    @OnTextChanged(R.id.et_userpwd)
    void inputPassword() {
        toggleLoginBtnStatus();
    }

    @OnTextChanged(R.id.et_confirm_userpwd)
    void confirmPassword() {
        toggleLoginBtnStatus();
    }

    /**
     * 触发提交按钮状态更新
     */
    void toggleLoginBtnStatus() {
        // 两次密码输入一致时才会触发状态变更
        boolean condition = etUserpwd.length() >= 6 && !TextUtils.isEmpty(etUserpwd.getText()) && !TextUtils.isEmpty(etConfirmUserpwd.getText())
                && etUserpwd.getText().toString().equals(etConfirmUserpwd.getText().toString());
        btSubmit.setEnabled(condition);
        btSubmit.getBackground().setAlpha(condition ? 255 : 100);
    }

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }

    @Override
    public void gotoLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (etUserpwd != null) {
            etUserpwd.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(etUserpwd, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
