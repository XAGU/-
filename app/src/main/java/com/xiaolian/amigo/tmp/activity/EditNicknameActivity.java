package com.xiaolian.amigo.tmp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.base.BaseActivity;
import com.xiaolian.amigo.tmp.base.BeanFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoyi on 2017/9/6.
 */

public class EditNicknameActivity extends BaseActivity {


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
