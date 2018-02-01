package com.xiaolian.amigo.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.login.intf.IPasswordRetrievalStep1Presenter;
import com.xiaolian.amigo.ui.login.intf.IPasswordRetrievalStep1View;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;
import com.xiaolian.amigo.util.ViewUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 找回密码步骤1
 *
 * @author caidong
 * @date 17/9/1
 */

public class PasswordRetrievalStep1Activity extends LoginBaseActivity implements IPasswordRetrievalStep1View {

    @Inject
    IPasswordRetrievalStep1Presenter<IPasswordRetrievalStep1View> presenter;

    @BindView(R.id.et_mobile)
    TextView etMobile;
    @BindView(R.id.et_verification_code)
    TextView etVerificationCode;
    @BindView(R.id.bt_send_verification_code)
    Button btSendVerificationCode;
    @BindView(R.id.bt_submit)
    Button btSubmit;
    CountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_retrieval_step1);

        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);

        presenter.onAttach(PasswordRetrievalStep1Activity.this);
        // 提交按钮初始化时禁用
        btSubmit.getBackground().setAlpha(100);
        btSubmit.setEnabled(false);

        ViewUtil.setEditHintAndSize(getString(R.string.mobile_hint), 14, etMobile);
        ViewUtil.setEditHintAndSize(getString(R.string.verification_code_hint), 14, etVerificationCode);
    }

    @Override
    protected void setUp() {

    }

    @OnTextChanged(R.id.et_mobile)
    void inputMobile() {
        toggleSubmitBtnStatus();
    }

    @OnTextChanged(R.id.et_verification_code)
    void inputVerification() {
        toggleSubmitBtnStatus();
    }

    @OnClick(R.id.et_mobile)
    void testClickText() {
        Log.e("===========>", "111");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnClick(R.id.bt_send_verification_code)
    void sendVerificationCode() {
        if (null == timer) {
            timer = new CountDownTimer(30000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    btSendVerificationCode.setText(millisUntilFinished / 1000 + "秒");
                }

                @Override
                public void onFinish() {
                    btSendVerificationCode.setEnabled(true);
                    btSendVerificationCode.setText("重新获取");
                    btSendVerificationCode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorFullRed));
                    btSendVerificationCode.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.verification_code_1));
                }
            };
        }
        presenter.getVerification(etMobile.getText().toString());
    }


    /**
     * 触发提交按钮状态更新
     */
    void toggleSubmitBtnStatus() {
        // 两次密码输入一致时才会触发状态变更
        boolean condition = !TextUtils.isEmpty(etMobile.getText()) && !TextUtils.isEmpty(etVerificationCode.getText());
        btSubmit.setEnabled(condition);
        btSubmit.getBackground().setAlpha(condition ? 255 : 100);
    }

    /**
     * 跳转至找回密码第二步
     */
    @OnClick(R.id.bt_submit)
    void jump2Step2() {
        presenter.checkVerification(etMobile.getText().toString(), etVerificationCode.getText().toString());
    }

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }

    @OnClick(R.id.tv_agreement)
    void onAgreementClick() {
        startActivity(new Intent(this, WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_AGREEMENT));
    }

    @Override
    public void startTimer() {
        timer.start();
        btSendVerificationCode.setEnabled(false);
        btSendVerificationCode.setTextColor(ContextCompat.getColor(this, R.color.colorTextGray));
        btSendVerificationCode.setBackground(this.getResources().getDrawable(R.drawable.verification_code_2));
    }

    @Override
    public void next() {
        Intent intent = new Intent();
        intent.setClass(this, PasswordRetrievalStep2Activity.class);
        intent.putExtra(PasswordRetrievalStep2Activity.INTENT_KEY_PASSWORD_RETRIEVAL_CODE, etVerificationCode.getText().toString());
        intent.putExtra(PasswordRetrievalStep2Activity.INTENT_KEY_PASSWORD_RETRIEVAL_MOBILE, etMobile.getText().toString());
        startActivity(intent);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (etMobile != null) {
            etMobile.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(etMobile, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
