package com.xiaolian.amigo.ui.device.dispenser;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.enumeration.TradeError;
import com.xiaolian.amigo.data.network.model.order.OrderPreInfoDTO;
import com.xiaolian.amigo.data.vo.ScanDevice;
import com.xiaolian.amigo.data.vo.ScanDeviceGroup;
import com.xiaolian.amigo.ui.device.DeviceBaseActivity;
import com.xiaolian.amigo.ui.device.DeviceConstant;
import com.xiaolian.amigo.ui.device.WaterDeviceBaseActivity;
import com.xiaolian.amigo.ui.device.dryer.DryerActivity;
import com.xiaolian.amigo.ui.device.intf.dispenser.IChooseDispenerView;
import com.xiaolian.amigo.ui.device.intf.dispenser.IChooseDispenserPresenter;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutFooter;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutHeader;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Log;
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
public class ChooseDispenserActivity extends DeviceBaseActivity implements IChooseDispenerView {

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

    private TextView tv_nearby;
    private TextView tv_favorite;

    private LinearLayout ll_footer;

    private RecyclerView recyclerView;
    private SmartRefreshLayout refreshLayout;

    int action = DeviceConstant.ACTION_CHOOSE_DISPENSER;
    private RelativeLayout rl_empty;
    private RelativeLayout rl_error;
    private OrderPreInfoDTO orderPreInfo;
    private TextView tv_rescan;
    private TextView tv_empty_tip;
    private int deviceType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_dispenser);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(ChooseDispenserActivity.this);
        presenter.setDeviceType(deviceType);
        adaptor = new ChooseDispenserAdaptor(this, R.layout.item_dispenser,
                items, Device.getDevice(deviceType) == Device.DISPENSER);
        adaptor.setOnItemClickListener((deviceNo, supplierId, isFavor, residenceId, usefor, location) -> {
            if (Device.getDevice(deviceType) == Device.DISPENSER) {
                presenter.closeBleConnection();
                presenter.gotoDispenser(deviceNo, supplierId, isFavor, residenceId, usefor, location);
            } else if (Device.getDevice(deviceType) == Device.DRYER) {
                presenter.closeBleConnection();
                presenter.gotoDryer(deviceNo, supplierId, isFavor, residenceId, location);
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 14)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);
        refreshLayout = findViewById(R.id.refreshLayout);

        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onRefresh(com.scwang.smartrefresh.layout.api.RefreshLayout refreshlayout) {
                if (listStatus) {
                    presenter.requestFavorites();
                } else {
                    hideScanStopView();
                    presenter.onLoad();
                }
            }

            @Override
            public void onLoadmore(com.scwang.smartrefresh.layout.api.RefreshLayout refreshlayout) {

            }
        });
        refreshLayout.setRefreshHeader(new RefreshLayoutHeader(this));
        refreshLayout.setRefreshFooter(new RefreshLayoutFooter(this));
        refreshLayout.setReboundDuration(200);
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.autoRefresh(0);

        rl_empty = findViewById(R.id.rl_empty);
        rl_error = findViewById(R.id.rl_error);

        tv_empty_tip = findViewById(R.id.tv_empty_tip);

        tv_rescan = findViewById(R.id.tv_rescan);
        tv_rescan.setOnClickListener(v -> onReScan());

        ll_footer = findViewById(R.id.ll_footer);

        tv_nearby = findViewById(R.id.tv_toolbar_title);
        tv_nearby.setOnClickListener(v -> onNearbyClick());
        tv_favorite = findViewById(R.id.tv_toolbar_title2);
        tv_favorite.setOnClickListener(v -> onFavoriteClick());

        switch (action) {
            case DeviceConstant.ACTION_CHOOSE_DISPENSER:
            case DeviceConstant.ACTION_CHANGE_DISPENSER:
                break;
            case DeviceConstant.ACTION_CHOOSE_DRYER:
            case DeviceConstant.ACTION_CHANGE_DRYER:
                tv_nearby.setText(R.string.nearby_hair_dryer);
                tv_favorite.setText(R.string.favorite_hair_dryer);
                break;
        }
    }

    private void onReScan() {
        presenter.startTimer();
        tv_rescan.setVisibility(View.GONE);
        refreshLayout.autoRefresh(100);
        hideScanStopView();
    }

    private void hideScanStopView() {
        hideEmptyView();
        ll_footer.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null) {
            action = getIntent().getIntExtra(DeviceConstant.INTENT_KEY_ACTION, DeviceConstant.ACTION_CHOOSE_DISPENSER);
            orderPreInfo = getIntent().getParcelableExtra(WaterDeviceBaseActivity.INTENT_PREPAY_INFO);
            deviceType = getIntent().getIntExtra(DeviceConstant.INTENT_DEVICE_TYPE, Device.DISPENSER.getType());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void onNearbyClick() {
        if (listStatus) {
            presenter.startTimer();
            switchListStatus();
            presenter.setListStatus(false);
            this.items.clear();
            hideEmptyView();
            hideErrorView();
            if (nearbyItems.isEmpty()) {
//                presenter.onLoad();
                refreshLayout.autoRefresh(0);
                adaptor.notifyDataSetChanged();
            } else {
                this.items.addAll(nearbyItems);
                adaptor.notifyDataSetChanged();
            }
        }
    }

    private void onFavoriteClick() {
        if (!listStatus) {
            presenter.cancelTimer();
            tv_rescan.setVisibility(View.GONE);
            switchListStatus();
            presenter.setListStatus(true);
            this.items.clear();
            if (favoriteItems.isEmpty()) {
                presenter.requestFavorites();
            } else {
                hideEmptyView();
                hideErrorView();
                this.items.addAll(favoriteItems);
                adaptor.notifyDataSetChanged();
//                adaptor.notifyItemRangeChanged(0, items.size());
//                recyclerView.setAdapter(adaptor);
            }
        }
    }


    private void switchListStatus() {
        if (listStatus) {
            ll_footer.setVisibility(View.VISIBLE);
            listStatus = false;
            tv_nearby.setTextColor(ContextCompat.getColor(this, R.color.colorDark2));
            tv_favorite.setTextColor(ContextCompat.getColor(this, R.color.colorDarkB));
        } else {
            ll_footer.setVisibility(View.GONE);
            listStatus = true;
            tv_nearby.setTextColor(ContextCompat.getColor(this, R.color.colorDarkB));
            tv_favorite.setTextColor(ContextCompat.getColor(this, R.color.colorDark2));
        }
    }

    @Override
    public void addMore(List<ChooseDispenserAdaptor.DispenserWrapper> wrappers) {
        if (wrappers.isEmpty() && !listStatus) {
            return;
        }
        items.addAll(wrappers);
        favoriteItems.addAll(wrappers);
//        if (recyclerView.getAdapter() == null) {
//        }
        adaptor.notifyDataSetChanged();
    }

    @Override
    public void addScanDevices(List<ScanDeviceGroup> devices) {
        updateDevice(devices);
        Log.i(TAG, "扫描到的设备列表：" + devices);
    }

    @Override
    public void showEmptyView() {
//        recyclerView.setVisibility(View.GONE);
        items.clear();
        adaptor.notifyDataSetChanged();
        rl_empty.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
//        recyclerView.setVisibility(View.VISIBLE);
        tv_empty_tip.setText(R.string.empty_tip);
        rl_empty.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView() {
//        recyclerView.setVisibility(View.GONE);
        items.clear();
        adaptor.notifyDataSetChanged();
        rl_error.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideErrorView() {
//        recyclerView.setVisibility(View.VISIBLE);
        rl_error.setVisibility(View.GONE);
    }

    @Override
    public void completeRefresh() {
        refreshLayout.finishRefresh(100);
    }

    @Override
    public void finishView() {
        finish();
    }

    @Override
    public void gotoDispenser(String macAddress, Long supplierId, boolean favor, Long residenceId, String usefor, String location) {
        startActivity(new Intent(this, DispenserActivity.class)
                .putExtra(MainActivity.INTENT_KEY_MAC_ADDRESS,
                        macAddress)
                .putExtra(MainActivity.INTENT_KEY_SUPPLIER_ID, supplierId)
                .putExtra(DispenserActivity.INTENT_KEY_FAVOR,
                        favor)
                .putExtra(DispenserActivity.INTENT_KEY_ID,
                        residenceId)
                .putExtra(DispenserActivity.INTENT_KEY_TEMPERATURE,
                        usefor)
                .putExtra(MainActivity.INTENT_KEY_LOCATION, location)
                .putExtra(MainActivity.INTENT_KEY_DEVICE_TYPE, Device.DISPENSER.getType())
                .putExtra(WaterDeviceBaseActivity.INTENT_PREPAY_INFO, orderPreInfo));
        finish();
    }

    @Override
    public void gotoDryer(String deviceNo, Long supplierId, Boolean isFavor, Long residenceId, String location) {
        startActivity(new Intent(this, DryerActivity.class)
                .putExtra(MainActivity.INTENT_KEY_MAC_ADDRESS,
                        deviceNo)
                .putExtra(MainActivity.INTENT_KEY_SUPPLIER_ID, supplierId)
                .putExtra(DispenserActivity.INTENT_KEY_FAVOR,
                        isFavor)
                .putExtra(DispenserActivity.INTENT_KEY_ID,
                        residenceId)
                .putExtra(MainActivity.INTENT_KEY_LOCATION, location)
                .putExtra(MainActivity.INTENT_KEY_DEVICE_TYPE, Device.DRYER.getType())
                .putExtra(WaterDeviceBaseActivity.INTENT_PREPAY_INFO, orderPreInfo));
        finish();
    }

    @Override
    public void showScanStopView() {
        if (!nearbyItems.isEmpty() || listStatus) {
            return;
        }
        refreshLayout.finishRefresh(10);
        ll_footer.setVisibility(View.GONE);
        showEmptyView();
        tv_rescan.setVisibility(View.VISIBLE);
        tv_empty_tip.setText("未扫描出附近的饮水机");
    }


    private synchronized void updateDevice(List<ScanDeviceGroup> devices) {
        if (devices.isEmpty()) {
//            if (nearbyItems.isEmpty()) {
//                showEmptyView();
//            }
            return;
        }
        if (nearbyItems.isEmpty()) {
            for (ScanDeviceGroup scanDeviceGroup : devices) {
                nearbyItems.add(new ChooseDispenserAdaptor.DispenserWrapper(scanDeviceGroup));
            }
            if (!listStatus) {
                items.addAll(nearbyItems);
                if (recyclerView.getAdapter() == null) {
                    recyclerView.setAdapter(adaptor);
                }
                adaptor.notifyDataSetChanged();
            }
        } else {
            boolean needNotify = false;
            List<ChooseDispenserAdaptor.DispenserWrapper> tempItems = null;
            for (ScanDeviceGroup scanDeviceGroup : devices) {
                boolean isContained = false;
                for (ChooseDispenserAdaptor.DispenserWrapper wrapper : nearbyItems) {
                    if (CommonUtil.equals(scanDeviceGroup.getResidenceId(),
                            wrapper.getResidenceId())) {
                        isContained = true;
                        for (ScanDevice device : scanDeviceGroup.getWater()) {
                            if (!isContainDevice(device, wrapper.getDeviceGroup())) {
                                needNotify = true;
                                if (wrapper.getDeviceGroup().getWater() != null) {
                                    wrapper.getDeviceGroup().getWater().add(device);
                                } else {
                                    List<ScanDevice> water = new ArrayList<>();
                                    water.add(device);
                                    wrapper.getDeviceGroup().setWater(water);
                                }
                            }
                        }
                    }
                }
                if (!isContained) {
                    if (tempItems == null) {
                        tempItems = new ArrayList<>();
                    }
                    tempItems.add(new ChooseDispenserAdaptor.DispenserWrapper(scanDeviceGroup));
                    needNotify = true;
                }
            }
            if (tempItems != null) {
                nearbyItems.addAll(tempItems);
            }
            if (needNotify && !listStatus) {
                items.clear();
                items.addAll(nearbyItems);
                adaptor.notifyDataSetChanged();
            }
        }
    }

    private boolean isContainDevice(ScanDevice device, ScanDeviceGroup deviceGroup) {
        for (ScanDevice contained : deviceGroup.getWater()) {
            if (CommonUtil.equals(contained.getId(), device.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        presenter.closeBleConnection();
        super.onPause();
    }

    @Override
    public void onError(TradeError tradeError) {

    }

    @Override
    protected void onDestroy() {
        presenter.cancelTimer();
        presenter.onDetach();
        super.onDestroy();
    }
}
