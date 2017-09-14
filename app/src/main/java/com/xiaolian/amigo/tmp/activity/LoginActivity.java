package com.xiaolian.amigo.tmp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.xiaolian.amigo.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 用户登录
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_mobile)
    TextView et_mobile;
    @BindView(R.id.et_userpwd)
    TextView et_userpwd;
    @BindView(R.id.bt_submit)
    Button bt_submit;
    @BindView(R.id.tv_password_retrieval_link)
    TextView tv_password_retrieval_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        // 登录按钮初始化时禁用
        bt_submit.getBackground().setAlpha(100);
        bt_submit.setEnabled(false);
    }

    @OnTextChanged(R.id.et_mobile)
    void inputMobile(CharSequence s, int start, int before, int count) {
        toggleSubmitBtnStatus();
    }

    @OnTextChanged(R.id.et_userpwd)
    void inputPassword() {
        toggleSubmitBtnStatus();
    }

    @OnClick(R.id.tv_password_retrieval_link)
    void retrievalPassword() {
        Intent intent = new Intent();
        intent.setClass(this, PasswordRetrievalStep1Activity.class);
        startActivity(intent);
    }

    /**
     * 根据用户名和密码输入框对登录按钮做点击控制
     */
    void toggleSubmitBtnStatus() {
        boolean condition = !TextUtils.isEmpty(et_mobile.getText()) && !TextUtils.isEmpty(et_userpwd.getText());
        bt_submit.setEnabled(condition);
        bt_submit.getBackground().setAlpha(condition ? 255 : 100);
    }
}
