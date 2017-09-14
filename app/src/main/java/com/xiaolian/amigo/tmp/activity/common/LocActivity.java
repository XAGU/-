package com.xiaolian.amigo.tmp.activity.common;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.activity.common.adaptor.LocationAdaptor;
import com.xiaolian.amigo.tmp.activity.repair.RepairApplyActivity;
import com.xiaolian.amigo.tmp.activity.repair.adaptor.RepairAdaptor;
import com.xiaolian.amigo.tmp.base.BaseActivity;
import com.xiaolian.amigo.tmp.common.config.RecycleViewDivider;
import com.xiaolian.amigo.tmp.common.config.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 维修列表
 * <p>
 * Created by caidong on 2017/9/12.
 */
public class LocActivity extends BaseActivity {

    static List<LocationAdaptor.Location> locs = new ArrayList<LocationAdaptor.Location>() {
        {
            add(new LocationAdaptor.Location("xx位置", 1));
            add(new LocationAdaptor.Location("xx位置", 0));
        }
    };

    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;

    @BindView(R.id.rv_locs)
    RecyclerView rv_locs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_loc);
        ButterKnife.bind(this);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new LocationAdaptor(locs, RepairApplyActivity.class);
        rv_locs.addItemDecoration(new RecycleViewDivider(this, RecycleViewDivider.VERTICAL_LIST));
        rv_locs.setLayoutManager(manager);
        rv_locs.setAdapter(adapter);
    }
}
