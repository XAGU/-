package com.xiaolian.amigo.ui.repair;

import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设备报修导航页
 *
 * @author caidong
 * @date 17/9/22
 */
public class RepairNavActivity extends RepairBaseActivity {

    private Long lastRepairTime;

    @BindView(R.id.v_dot)
    View vDot;

    @Override
    protected void initView() {
        setMainBackground(R.color.white);
        ButterKnife.bind(this);
        if (lastRepairTime != null && !CommonUtil.equals(lastRepairTime, Constant.INVALID_ID)) {
            vDot.setVisibility(View.VISIBLE);
        }
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
        if (getIntent() != null) {
            lastRepairTime = getIntent().getLongExtra(RepairActivity.INTENT_KEY_LAST_REPAIR_TIME, Constant.INVALID_ID);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        vDot.setVisibility(View.GONE);
    }

    /**
     * 申请报修
     */
    @OnClick(R.id.rl_repair_apply_entry)
    void applyRepair() {
        startActivity(this, RepairApplyActivity.class);
    }

    /**
     * 报修记录
     */
    @OnClick(R.id.rl_repair_record_entry)
    void queryRepairs() {
        Map<String, Long> extraMap = new HashMap<String, Long>(1) {
            {
                put(RepairActivity.INTENT_KEY_LAST_REPAIR_TIME, lastRepairTime);
            }
        };
        startActivity(this, RepairActivity.class, extraMap);
    }
}
