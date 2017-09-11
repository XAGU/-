package com.xiaolian.amigo.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.xiaolian.amigo.R;

import butterknife.OnClick;

/**
 * Created by guoyi on 2017/9/7.
 */

public class BaseActivity extends AppCompatActivity implements BaseView{

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }

    private ProgressDialog progressDialog;

    // 启动activity完成跳转
    public void startActivity(AppCompatActivity activity, Class<?> clazz) {
        Intent intent = new Intent();
        intent.setClass(activity, clazz);
        startActivity(intent);
    }


    public void showLoading(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
