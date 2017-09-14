package com.xiaolian.amigo.tmp.activity.repair;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.activity.repair.adaptor.RepairAdaptor;
import com.xiaolian.amigo.tmp.activity.repair.adaptor.RepairProgressAdaptor;
import com.xiaolian.amigo.tmp.base.BaseActivity;
import com.xiaolian.amigo.tmp.common.config.RecycleViewDivider;
import com.xiaolian.amigo.tmp.common.config.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 报修详情
 * <p>
 * Created by caidong on 2017/9/12.
 */
public class RepairDetailActivity extends BaseActivity {

    static List<RepairProgressAdaptor.Progress> progresses = new ArrayList<RepairProgressAdaptor.Progress>() {
        {
            add(new RepairProgressAdaptor.Progress("", "", 1));
            add(new RepairProgressAdaptor.Progress("", "", 1));
            add(new RepairProgressAdaptor.Progress("", "", 1));
            add(new RepairProgressAdaptor.Progress("", "", 1));
        }
    };

    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;

    @BindView(R.id.rv_repair_progresses)
    RecyclerView rv_repairProgresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_detail);
        ButterKnife.bind(this);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new RepairProgressAdaptor(progresses);
        rv_repairProgresses.addItemDecoration(new RecycleViewDivider(this, RecycleViewDivider.VERTICAL_LIST));
        rv_repairProgresses.setLayoutManager(manager);
        rv_repairProgresses.setAdapter(adapter);
    }
}
