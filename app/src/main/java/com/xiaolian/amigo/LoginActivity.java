package com.xiaolian.amigo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.userpwd)
    TextView password;
    @BindView(R.id.login)
    Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        // 登录按钮初始化时禁用
        login.getBackground().setAlpha(100);
        login.setEnabled(false);
    }

    @OnTextChanged(R.id.username)
    void usernameTextChanged(CharSequence s, int start, int before, int count) {
        toggleLoginBtnStatus();
    }

    @OnTextChanged(R.id.userpwd)
    void userpwdTextChanged() {
        toggleLoginBtnStatus();
    }

    /**
     * 根据用户名和密码输入框对登录按钮做点击控制
     */
    void toggleLoginBtnStatus() {
        boolean condition = !TextUtils.isEmpty(username.getText()) && !TextUtils.isEmpty(password.getText());
        login.setEnabled(condition);
        login.getBackground().setAlpha(condition ? 255 : 100);
    }
}
