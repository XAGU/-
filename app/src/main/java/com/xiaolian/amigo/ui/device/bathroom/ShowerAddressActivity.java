package com.xiaolian.amigo.ui.device.bathroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.bathroom.adapter.ShowerAddressAdapter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IShowerAddressView;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 洗澡地址
 *
 * @author zcd
 * @date 18/7/12
 */
public class ShowerAddressActivity extends BathroomListBaseActivity
        implements IShowerAddressView {
    private ShowerAddressAdapter adaptor;
    private List<ShowerAddressAdapter.ShowerAddressWrapper> addresses = new ArrayList<ShowerAddressAdapter.ShowerAddressWrapper>() {
        {
            add(new ShowerAddressAdapter.ShowerAddressWrapper("1"));
            add(new ShowerAddressAdapter.ShowerAddressWrapper("2"));
            add(new ShowerAddressAdapter.ShowerAddressWrapper("2"));
            add(new ShowerAddressAdapter.ShowerAddressWrapper("2"));
            add(new ShowerAddressAdapter.ShowerAddressWrapper("2"));
            add(new ShowerAddressAdapter.ShowerAddressWrapper("2"));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setTitle() {
        tvToolbarTitle.setText(getString(R.string.shower_address));
        tvToolbarSubTitle.setText(getString(R.string.new_shower_address));
        tvToolbarSubTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.shower_address));
        tvSubTitle.setText(getString(R.string.new_shower_address));
        tvToolbarSubTitle.setOnClickListener(v -> gotoChooseShowerAddress());
        tvSubTitle.setOnClickListener(v -> gotoChooseShowerAddress());
    }

    private void gotoChooseShowerAddress() {
        startActivity(new Intent(this, ChooseShowerAddressActivity.class));
    }

    @Override
    protected void initRecyclerView() {
        adaptor = new ShowerAddressAdapter(this, R.layout.item_shower_address, addresses);
        adaptor.setOnItemLongClickListener(() -> onSuccess("请左滑操作"));
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 14)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);
    }

    @Override
    protected void onLoadMore() {

    }

    @Override
    protected void onRefresh() {

    }

    @Override
    protected void initIntent() {

    }
}
