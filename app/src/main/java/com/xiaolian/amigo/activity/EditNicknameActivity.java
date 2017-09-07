package com.xiaolian.amigo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.xiaolian.amigo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoyi on 2017/9/6.
 */

public class EditNicknameActivity extends AppCompatActivity {


    @BindView(R.id.edit_nickname)
    EditText nickName;

    @BindView(R.id.bt_submit)
    Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nickname);
        ButterKnife.bind(this);
    }


}
