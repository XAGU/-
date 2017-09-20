package com.xiaolian.amigo.ui.repair;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.activity.common.DeviceTypeActivity;
import com.xiaolian.amigo.tmp.base.BaseActivity;
import com.xiaolian.amigo.tmp.common.config.SpaceItemDecoration;
import com.xiaolian.amigo.tmp.common.util.ScreenUtils;
import com.xiaolian.amigo.ui.repair.adaptor.RepairAdaptor;
import com.xiaolian.amigo.ui.repair.adaptor.RepairProblemAdaptor;
import com.xiaolian.amigo.ui.repair.intf.IRepairApplyView;
import com.xiaolian.amigo.ui.repair.intf.IRepairDetailView;
import com.xiaolian.amigo.ui.user.ListChooseActivity;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 报修申请
 * <p>
 * Created by caidong on 2017/9/12.
 */
public class RepairApplyActivity extends RepairBaseActivity implements IRepairApplyView {

    List<RepairProblemAdaptor.ProblemWrapper> problems = new ArrayList<RepairProblemAdaptor.ProblemWrapper>() {
        {
            add(new RepairProblemAdaptor.ProblemWrapper("问题一", "问题二", "问题三"));
            add(new RepairProblemAdaptor.ProblemWrapper("问题一", "问题二", null));
        }
    };

    @BindView(R.id.rv_problems)
    RecyclerView rv_problems;
    @BindView(R.id.tv_location)
    TextView tv_location;

    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_apply);

        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

//        presenter.onAttach(this);

        adapter = new RepairProblemAdaptor(problems);
        rv_problems.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 10)));
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_problems.setLayoutManager(manager);
        rv_problems.setAdapter(adapter);

        render();

//        presenter.requestRepairs(Constant.PAGE_START_NUM);
    }

    @Override
    protected void setUp() {
        Intent intent = getIntent();
        if (null != intent) {
            location = getIntent().getStringExtra(Constant.LOCATION);
        }
    }

    @Override
    public void render() {
        // 填充设备位置
        if (null != location) {
            tv_location.setText(location);
        }
    }

    @OnClick(R.id.rl_device)
    void selectDevice() {
        Map<String, Integer> extraMap = new HashMap<String, Integer>() {
            {
                put(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION, ListChooseActivity.ACTION_LIST_DEVICE);
            }
        };
        startActivity(this, ListChooseActivity.class, extraMap);
    }
}
