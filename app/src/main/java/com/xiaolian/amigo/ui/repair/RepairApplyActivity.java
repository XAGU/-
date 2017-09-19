package com.xiaolian.amigo.ui.repair;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.activity.common.DeviceTypeActivity;
import com.xiaolian.amigo.tmp.base.BaseActivity;
import com.xiaolian.amigo.tmp.common.config.SpaceItemDecoration;
import com.xiaolian.amigo.tmp.common.util.ScreenUtils;
import com.xiaolian.amigo.ui.repair.adaptor.RepairAdaptor;
import com.xiaolian.amigo.ui.repair.adaptor.RepairProblemAdaptor;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 报修申请
 * <p>
 * Created by caidong on 2017/9/12.
 */
public class RepairApplyActivity extends RepairBaseActivity {

    List<RepairProblemAdaptor.ProblemWrapper> problems = new ArrayList<RepairProblemAdaptor.ProblemWrapper>() {
        {
            add(new RepairProblemAdaptor.ProblemWrapper("问题一", "问题二", "问题三"));
            add(new RepairProblemAdaptor.ProblemWrapper("问题一", "问题二", null));
        }
    };

    @BindView(R.id.rv_problems)
    RecyclerView rv_problems;

    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;

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

//        presenter.requestRepairs(Constant.PAGE_START_NUM);
    }

    @Override
    protected void setUp() {

    }

    @OnClick(R.id.rl_device)
    void selectDevice() {
        startActivity(this, DeviceTypeActivity.class);
    }
}
