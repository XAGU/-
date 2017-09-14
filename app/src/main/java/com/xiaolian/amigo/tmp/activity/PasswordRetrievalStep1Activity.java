package com.xiaolian.amigo.tmp.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.xiaolian.amigo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 找回密码步骤2
 * Created by caidong on 2017/9/1.
 */

public class PasswordRetrievalStep1Activity extends AppCompatActivity {
    @BindView(R.id.et_mobile)
    TextView et_mobile;
    @BindView(R.id.et_verification_code)
    TextView et_verificationCode;
    @BindView(R.id.bt_send_verification_code)
    Button bt_sendVerificationCode;
    @BindView(R.id.bt_submit)
    Button bt_submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_retrieval_step1);
        ButterKnife.bind(this);
        // 提交按钮初始化时禁用
        bt_submit.getBackground().setAlpha(100);
        bt_submit.setEnabled(false);
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
        final AppCompatActivity context = this;
        CountDownTimer timer = new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                bt_sendVerificationCode.setText(millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                bt_sendVerificationCode.setEnabled(true);
                bt_sendVerificationCode.setText("重新获取");
                bt_sendVerificationCode.setBackground(context.getResources().getDrawable(R.drawable.verification_code_1));
            }
        };

        bt_sendVerificationCode.setEnabled(false);
        timer.start();
        bt_sendVerificationCode.setBackground(this.getResources().getDrawable(R.drawable.verification_code_2));
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
        Intent intent = new Intent();
        intent.setClass(this, PasswordRetrievalStep2Activity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }
}
