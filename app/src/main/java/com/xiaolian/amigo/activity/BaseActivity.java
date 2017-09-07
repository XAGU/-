package com.xiaolian.amigo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.xiaolian.amigo.R;

import butterknife.OnClick;

/**
 * Created by guoyi on 2017/9/7.
 */

public class BaseActivity extends AppCompatActivity {

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }

    // 启动activity完成跳转
    protected void startActivity(AppCompatActivity activity, Class<?> clazz) {
        Intent intent = new Intent();
        intent.setClass(activity, clazz);
        startActivity(intent);
    }
}
