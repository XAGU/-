package com.xiaolian.amigo.ui.repair;

import android.os.Bundle;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.activity.repair.RepairApplyActivity;
import com.xiaolian.amigo.tmp.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设备报修导航页
 *
 * Created by caidong on 2017/9/12.
 */
public class RepairNavActivity extends RepairBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_nav);
        ButterKnife.bind(this);
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
