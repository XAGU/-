package com.xiaolian.amigo.tmp.activity.bonus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoyi on 2017/9/6.
 */

public class BonusExchangeActivity extends BaseActivity {


    @BindView(R.id.et_change_code)
    EditText et_changeCode;

    @BindView(R.id.bt_submit)
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus_exchange);
        ButterKnife.bind(this);
    }

}
