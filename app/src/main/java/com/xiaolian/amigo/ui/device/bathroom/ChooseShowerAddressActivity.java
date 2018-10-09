package com.xiaolian.amigo.ui.device.bathroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.bathroom.adapter.ChooseShowerAddressAdapter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IChooseShowerAddressView;
import com.xiaolian.amigo.ui.widget.RecycleViewDivider;
import com.xiaolian.amigo.util.Constant;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择设置洗澡地址
 *
 * @author zcd
 * @date 18/7/12
 */
public class ChooseShowerAddressActivity extends BathroomListBaseActivity
        implements IChooseShowerAddressView {
    private int action = BathroomConstant.ACTION_CHOOSE_BUILD;

    private ChooseShowerAddressAdapter adapter;
    private List<ChooseShowerAddressAdapter.ChooseShowerAddressWrapper> addresses = new ArrayList<ChooseShowerAddressAdapter.ChooseShowerAddressWrapper>() {
        {
            add(new ChooseShowerAddressAdapter.ChooseShowerAddressWrapper("1"));
            add(new ChooseShowerAddressAdapter.ChooseShowerAddressWrapper("1"));
            add(new ChooseShowerAddressAdapter.ChooseShowerAddressWrapper("1"));
            add(new ChooseShowerAddressAdapter.ChooseShowerAddressWrapper("1"));
            add(new ChooseShowerAddressAdapter.ChooseShowerAddressWrapper("1"));
            add(new ChooseShowerAddressAdapter.ChooseShowerAddressWrapper("1"));
            add(new ChooseShowerAddressAdapter.ChooseShowerAddressWrapper("1"));
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMainBackground();
    }

    private void initMainBackground() {
        clMain.setBackgroundResource(R.color.white);
        appBarLayout.setBackgroundResource(R.color.white);
        toolBar.setBackgroundResource(R.color.white);
        llHeader.setBackgroundResource(R.color.white);
    }

    @Override
    protected void setTitle() {
        switch (action) {
            case BathroomConstant.ACTION_CHOOSE_BUILD:
                tvToolbarTitle.setText(getString(R.string.choose_building));
                tvTitle.setText(getString(R.string.choose_building));
                break;
            case BathroomConstant.ACTION_CHOOSE_FLOOR:
                tvToolbarTitle.setText(getString(R.string.choose_floor));
                tvTitle.setText(getString(R.string.choose_floor));
                break;
            case BathroomConstant.ACTION_CHOOSE_ROOM:
                tvToolbarTitle.setText(getString(R.string.choose_room));
                tvTitle.setText(getString(R.string.choose_room));
                break;
        }
        tvToolbarSubTitle.setVisibility(View.GONE);
        tvSubTitle.setVisibility(View.GONE);
    }

    @Override
    protected void initRecyclerView() {
        adapter = new ChooseShowerAddressAdapter(this, R.layout.item_choose_shower_address, addresses);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (action == BathroomConstant.ACTION_CHOOSE_BUILD) {
                    gotoChooseShowerAddress(BathroomConstant.ACTION_CHOOSE_FLOOR);
                } else if (action == BathroomConstant.ACTION_CHOOSE_FLOOR) {
                    gotoChooseShowerAddress(BathroomConstant.ACTION_CHOOSE_ROOM);
                } else if (action == BathroomConstant.ACTION_CHOOSE_ROOM) {
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.addItemDecoration(new RecycleViewDivider(this, RecycleViewDivider.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void gotoChooseShowerAddress(int action) {
        startActivity(new Intent(this, ChooseShowerAddressActivity.class)
                .putExtra(BathroomConstant.ACTION, action));
    }

    @Override
    protected void onLoadMore() {

    }

    @Override
    protected void onRefresh() {

    }

    @Override
    protected void initIntent() {
        if (getIntent() != null) {
            action = getIntent().getIntExtra(BathroomConstant.ACTION,
                    BathroomConstant.ACTION_CHOOSE_BUILD);
        }
    }
}
