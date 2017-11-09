package com.xiaolian.amigo.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.login.intf.IPasswordRetrievalStep1Presenter;
import com.xiaolian.amigo.ui.login.intf.IPasswordRetrievalStep1View;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.ViewUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 找回密码步骤1
 * Created by caidong on 2017/9/1.
 */

public class PasswordRetrievalStep1Activity extends LoginBaseActivity implements IPasswordRetrievalStep1View {

    @Inject
    IPasswordRetrievalStep1Presenter<IPasswordRetrievalStep1View> presenter;

    @BindView(R.id.et_mobile)
    TextView et_mobile;
    @BindView(R.id.et_verification_code)
    TextView et_verificationCode;
    @BindView(R.id.bt_send_verification_code)
    Button bt_sendVerificationCode;
    @BindView(R.id.bt_submit)
    Button bt_submit;
    CountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_retrieval_step1);

        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);

        presenter.onAttach(PasswordRetrievalStep1Activity.this);
        // 提交按钮初始化时禁用
        bt_submit.getBackground().setAlpha(100);
        bt_submit.setEnabled(false);

        ViewUtil.setEditHintAndSize(getString(R.string.mobile_hint), 14, et_mobile);
        ViewUtil.setEditHintAndSize(getString(R.string.verification_code_hint), 14, et_verificationCode);
    }

    @Override
    protected void setUp() {

    }

    @OnTextChanged(R.id.et_mobile)
    void inputMobile(CharSequence s, int start, int before, int count) {
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
                    bt_sendVerificationCode.setText(millisUntilFinished / 1000 + "秒");
                }

                @Override
                public void onFinish() {
                    bt_sendVerificationCode.setEnabled(true);
                    bt_sendVerificationCode.setText("重新获取");
                    bt_sendVerificationCode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorFullRed));
                    bt_sendVerificationCode.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.verification_code_1));
                }
            };
        }
        presenter.getVerification(et_mobile.getText().toString());
    }



    /**
     * 触发提交按钮状态更新
     */
    void toggleSubmitBtnStatus() {
        // 两次密码输入一致时才会触发状态变更
        boolean condition = !TextUtils.isEmpty(et_mobile.getText()) && !TextUtils.isEmpty(et_verificationCode.getText());
        bt_submit.setEnabled(condition);
        bt_submit.getBackground().setAlpha(condition ? 255 : 100);
    }

    /**
     * 跳转至找回密码第二步
     */
    @OnClick(R.id.bt_submit)
    void jump2Step2() {
        presenter.checkVerification(et_mobile.getText().toString(), et_verificationCode.getText().toString());
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
        bt_sendVerificationCode.setEnabled(false);
        bt_sendVerificationCode.setTextColor(ContextCompat.getColor(this, R.color.colorTextGray));
        bt_sendVerificationCode.setBackground(this.getResources().getDrawable(R.drawable.verification_code_2));
    }

    @Override
    public void next() {
        Intent intent = new Intent();
        intent.setClass(this, PasswordRetrievalStep2Activity.class);
        intent.putExtra(PasswordRetrievalStep2Activity.INTENT_KEY_PASSWORD_RETRIEVAL_CODE, et_verificationCode.getText().toString());
        intent.putExtra(PasswordRetrievalStep2Activity.INTENT_KEY_PASSWORD_RETRIEVAL_MOBILE, et_mobile.getText().toString());
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
        if (et_mobile != null) {
            et_mobile.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et_mobile, InputMethodManager.SHOW_IMPLICIT);
        }
    }

}
