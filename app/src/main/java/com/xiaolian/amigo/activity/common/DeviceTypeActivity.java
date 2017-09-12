package com.xiaolian.amigo.activity.common;

import android.os.Bundle;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设备类型
 * <p>
 * Created by caidong on 2017/9/12.
 */
public class DeviceTypeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_type);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rl_heator)
    void selectHeatorLocation() {
        startActivity(this, BuildingActivity.class);
    }

    @OnClick(R.id.rl_dispenser)
    void selectDispenserLocation() {
        startActivity(this, BuildingActivity.class);
    }
}
