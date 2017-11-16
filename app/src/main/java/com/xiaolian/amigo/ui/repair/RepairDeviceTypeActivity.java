package com.xiaolian.amigo.ui.repair;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.ui.user.ListChooseActivity;

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
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected int setTitle() {
        return R.string.device_type;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_device_type;
    }

    @Override
    protected void setUp() {

    }

    // 选择热水澡
    @OnClick(R.id.rl_heator)
    void selectHeatorLocation() {
        Map<String, Integer> extraMap = new HashMap<String, Integer>() {
            {
                put(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION, ListChooseActivity.ACTION_LIST_BUILDING);
                put(ListChooseActivity.INTENT_KEY_LIST_DEVICE_TYPE, Device.HEATER.getType());
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
                put(ListChooseActivity.INTENT_KEY_LIST_DEVICE_TYPE, Device.DISPENSER.getType());
            }
        };
        startActivity(this, ListChooseActivity.class, extraMap);
    }
}
