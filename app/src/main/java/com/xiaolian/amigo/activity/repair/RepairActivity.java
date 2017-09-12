package com.xiaolian.amigo.activity.repair;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.activity.repair.adaptor.RepairAdaptor;
import com.xiaolian.amigo.base.BaseActivity;
import com.xiaolian.amigo.common.config.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 维修列表
 *
 * Created by caidong on 2017/9/12.
 */
public class RepairActivity extends BaseActivity{

    static List<RepairAdaptor.Repair> repairs = new ArrayList<RepairAdaptor.Repair>() {
        {
            add(new RepairAdaptor.Repair("", "", 1));
            add(new RepairAdaptor.Repair("", "", 1));
            add(new RepairAdaptor.Repair("", "", 1));
        }
    };

    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;

    @BindView(R.id.rv_repairs)
    RecyclerView rv_repairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);
        ButterKnife.bind(this);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new RepairAdaptor(repairs);
        rv_repairs.addItemDecoration(new SpaceItemDecoration(14));
        rv_repairs.setLayoutManager(manager);
        rv_repairs.setAdapter(adapter);
    }

}
