package com.xiaolian.amigo.ui.ble;

/**
 * 测试BLE
 * <p>
 * Created by caidong on 2017/9/21.
 */

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.xiaolian.amigo.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BLEActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.open)
    public void sendMessage(View view) {
    }
}