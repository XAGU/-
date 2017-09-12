package com.xiaolian.amigo.activity.common;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.activity.common.adaptor.LocationAdaptor;
import com.xiaolian.amigo.activity.repair.adaptor.RepairAdaptor;
import com.xiaolian.amigo.base.BaseActivity;
import com.xiaolian.amigo.common.config.RecycleViewDivider;
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
public class BuildingActivity extends BaseActivity{

    static List<LocationAdaptor.Location> buildings = new ArrayList<LocationAdaptor.Location>() {
        {
            add(new LocationAdaptor.Location("xx楼栋", 1));
            add(new LocationAdaptor.Location("xx楼栋", 0));
        }
    };

    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;

    @BindView(R.id.rv_buildings)
    RecyclerView rv_buildings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_building);
        ButterKnife.bind(this);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new LocationAdaptor(buildings, FloorActivity.class);
        rv_buildings.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, 1, getResources().getColor(R.color.colorDarke)));
        rv_buildings.setLayoutManager(manager);
        rv_buildings.setAdapter(adapter);
    }
}
