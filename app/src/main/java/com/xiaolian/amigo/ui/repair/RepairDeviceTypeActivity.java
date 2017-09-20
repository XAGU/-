package com.xiaolian.amigo.ui.repair;

import android.os.Bundle;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.user.ListChooseActivity;
import com.xiaolian.amigo.util.Constant;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设备类型
 * <p>
 * Created by caidong on 2017/9/12.
 */
public class RepairDeviceTypeActivity extends RepairBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_type);
        ButterKnife.bind(this);
    }

    @Override
    protected void setUp() {

    }

    // 选择热水器
    @OnClick(R.id.rl_heator)
    void selectHeatorLocation() {
        Map<String, Integer> extraMap = new HashMap<String, Integer>() {
            {
                put(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION, ListChooseActivity.ACTION_LIST_BUILDING);
                put(ListChooseActivity.INTENT_KEY_LIST_BUILDING_TYPE, Constant.ROOM_BUILDING_TYPE);
            }
        };
        startActivity(this, ListChooseActivity.class, extraMap);
    }

    // 选择饮水机
    @OnClick(R.id.rl_dispenser)
    void selectDispenserLocation() {
        Map<String, Integer> extraMap = new HashMap<String, Integer>() {
            {
                put(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION, ListChooseActivity.ACTION_LIST_BUILDING);
            }
        };
        startActivity(this, ListChooseActivity.class, extraMap);
    }
}
