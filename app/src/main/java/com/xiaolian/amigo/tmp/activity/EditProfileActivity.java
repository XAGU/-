package com.xiaolian.amigo.tmp.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * Created by guoyi on 2017/9/6.
 */

public class EditProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
    }

    public void onclick(View v) {

        switch (v.getId()) {
            case R.id.rel_edit_avatar:
                Toast.makeText(this.getApplicationContext(), "修改头像", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rel_edit_nickname:
                Intent intent = new Intent(getApplicationContext(), EditNicknameActivity.class);
                intent.putExtra("nickName", "");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, 1, new Bundle());
                } else {
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.rel_edit_sex:
                Toast.makeText(this.getApplicationContext(), "修改性别", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rel_edit_mobile:
                Toast.makeText(this.getApplicationContext(), "修改手机号", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rel_edit_password:
                Toast.makeText(this.getApplicationContext(), "修改密码", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rel_edit_school:
                Toast.makeText(this.getApplicationContext(), "修改学校", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rel_edit_room:
                Toast.makeText(this.getApplicationContext(), "修改宿舍", Toast.LENGTH_SHORT).show();
                break;
        }


    }

}
