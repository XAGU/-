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
public class FloorActivity extends BaseActivity{

    static List<LocationAdaptor.Location> floors = new ArrayList<LocationAdaptor.Location>() {
        {
            add(new LocationAdaptor.Location("xx楼层", 1));
            add(new LocationAdaptor.Location("xx楼层", 0));
        }
    };

    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;

    @BindView(R.id.rv_floors)
    RecyclerView rv_floors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_floor);
        ButterKnife.bind(this);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new LocationAdaptor(floors, LocActivity.class);
        rv_floors.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, 1, getResources().getColor(R.color.colorDarke)));
        rv_floors.setLayoutManager(manager);
        rv_floors.setAdapter(adapter);
    }

}
