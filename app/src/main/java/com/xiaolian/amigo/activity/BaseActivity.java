package com.xiaolian.amigo.activity;

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
}
