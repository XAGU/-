package com.xiaolian.amigo.ui.device.dispenser;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.device.ScanDeviceGroup;
import com.xiaolian.amigo.ui.device.DeviceBaseListActivity;
import com.xiaolian.amigo.ui.device.intf.dispenser.IChooseDispenerView;
import com.xiaolian.amigo.ui.device.intf.dispenser.IChooseDispenserPresenter;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 附近饮水机页面
 *
 * @author zcd
 */
public class ChooseDispenserActivity extends DeviceBaseListActivity implements IChooseDispenerView {

    private static final String TAG = ChooseDispenserActivity.class.getSimpleName();

    @Inject
    IChooseDispenserPresenter<IChooseDispenerView> presenter;
    ChooseDispenserAdaptor adaptor;
    /**
     * 列表显示的是附近列表还是收藏列表
     * false 表示附近列表
     * true 表示收藏列表
     */
    boolean listStatus = false;
    List<ChooseDispenserAdaptor.DispenserWrapper> items = new ArrayList<>();
    List<ChooseDispenserAdaptor.DispenserWrapper> nearbyItems = new ArrayList<>();
    List<ChooseDispenserAdaptor.DispenserWrapper> favoriteItems = new ArrayList<>();

    TextView tv_nearby;
    TextView tv_favorite;

    @Override
    protected void onRefresh() {
        // ignore
    }

    @Override
    protected void onLoadMore() {
        // ignore
    }

    @Override
    protected void setRecyclerView(RecyclerView recyclerView) {
        setAutoRefresh(false);
        adaptor = new ChooseDispenserAdaptor(this, R.layout.item_dispenser, items);
        recyclerView.setAdapter(adaptor);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 14)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected int setTitle() {
        return R.string.nearby_dispenser;
    }

    @Override
    protected int setTitle2() {
        return R.string.favorite_dispenser;
    }

    @Override
    protected void initView() {
        getRefreshLayout().setEnableRefresh(false);
        getRefreshLayout().setEnableLoadmore(false);

        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(ChooseDispenserActivity.this);

        tv_nearby = getToolBarTitle();
        tv_nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNearbyClick();
            }
        });
        tv_favorite = getToolBarTitle2();
        tv_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavoriteClick();
            }
        });

        // presenter.requestFavorites();
        presenter.onLoad();
    }

    private void onNearbyClick() {
        if (listStatus) {
            switchListStatus();
            this.items.clear();
            if (nearbyItems.isEmpty()) {
                presenter.onLoad();
            } else {
                this.items.addAll(nearbyItems);
                adaptor.notifyDataSetChanged();
            }
        }
    }

    private void onFavoriteClick() {
        if (!listStatus) {
            switchListStatus();
            this.items.clear();
            if (favoriteItems.isEmpty()) {
                presenter.requestFavorites();
            }
            else {
                this.items.addAll(favoriteItems);
                adaptor.notifyDataSetChanged();
            }
        }
    }


    private void switchListStatus() {
        if (listStatus) {
            listStatus = false;
            tv_nearby.setTextColor(ContextCompat.getColor(this, R.color.colorDark2));
            tv_favorite.setTextColor(ContextCompat.getColor(this, R.color.colorDarkB));
        } else {
            listStatus = true;
            tv_nearby.setTextColor(ContextCompat.getColor(this, R.color.colorDarkB));
            tv_favorite.setTextColor(ContextCompat.getColor(this, R.color.colorDark2));
        }
    }

    @Override
    public void addMore(List<ChooseDispenserAdaptor.DispenserWrapper> wrappers) {
        items.addAll(wrappers);
        favoriteItems.addAll(wrappers);
        adaptor.notifyDataSetChanged();
    }

    @Override
    public void addScanDevices(List<ScanDeviceGroup> devices) {
        Log.i(TAG, "扫描到的设备列表：" + devices);
    }
}
