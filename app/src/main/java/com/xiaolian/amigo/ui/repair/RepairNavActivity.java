package com.xiaolian.amigo.ui.repair;

import com.xiaolian.amigo.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设备报修导航页
 *
 * Created by caidong on 2017/9/12.
 */
public class RepairNavActivity extends RepairBaseActivity {

    @Override
    protected void initView() {
        setMainBackground(R.color.white);
        ButterKnife.bind(this);
    }

    @Override
    protected int setTitle() {
        return R.string.device_repair;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_repair_nav;
    }

    @Override
    protected void setUp() {

    }

    // 申请报修
    @OnClick(R.id.rl_repair_apply_entry)
    void applyRepair() {
        startActivity(this, RepairApplyActivity.class);
    }

    // 报修记录
    @OnClick(R.id.rl_repair_record_entry)
    void queryRepairs() {
        startActivity(this, RepairActivity.class);
    }
}
