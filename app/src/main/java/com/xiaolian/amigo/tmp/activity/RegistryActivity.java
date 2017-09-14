package com.xiaolian.amigo.tmp.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.component.SchoolDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 账号注册
 * Created by caidong on 2017/9/1.
 */

public class RegistryActivity extends AppCompatActivity {

    @BindView(R.id.tv_school)
    TextView tv_school;
    @BindView(R.id.et_userpwd)
    EditText et_userpwd;
    @BindView(R.id.bt_submit)
    Button bt_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
        ButterKnife.bind(this);

        bt_submit.getBackground().setAlpha(100);
        bt_submit.setEnabled(false);
    }

    /**
     * 点击学校文本框
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.tv_school)
    void inputSchool() {
        SchoolDialog dialog = new SchoolDialog(this);
        dialog.create();
        dialog.show();
    }

    @OnTextChanged(R.id.et_userpwd)
    void inputPassword() {
        toggleSumbitBtnStatus();
    }

    /**
     * 触发提交按钮状态更新
     */
    public void toggleSumbitBtnStatus() {
        boolean condition = !TextUtils.isEmpty(tv_school.getText()) && !TextUtils.isEmpty(et_userpwd.getText());
        bt_submit.setEnabled(condition);
        bt_submit.getBackground().setAlpha(condition ? 255 : 100);
    }
}
