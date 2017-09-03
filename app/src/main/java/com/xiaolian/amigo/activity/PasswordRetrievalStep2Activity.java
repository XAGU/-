package com.xiaolian.amigo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

public class PasswordRetrievalStep2Activity extends AppCompatActivity {
    @BindView(R.id.et_userpwd)
    TextView et_userpwd;
    @BindView(R.id.et_confirm_userpwd)
    TextView et_confirm_userpwd;
    @BindView(R.id.bt_submit)
    Button bt_submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_retrieval_step2);
        ButterKnife.bind(this);
        // 提交按钮初始化时禁用
        bt_submit.getBackground().setAlpha(100);
        bt_submit.setEnabled(false);
    }

    @OnTextChanged(R.id.et_userpwd)
    void inputPassword(CharSequence s, int start, int before, int count) {
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
        boolean condition = !TextUtils.isEmpty(et_userpwd.getText()) && !TextUtils.isEmpty(et_confirm_userpwd.getText())
                && et_userpwd.getText().toString().equals(et_confirm_userpwd.getText().toString());
        bt_submit.setEnabled(condition);
        bt_submit.getBackground().setAlpha(condition ? 255 : 100);
    }

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }
}
