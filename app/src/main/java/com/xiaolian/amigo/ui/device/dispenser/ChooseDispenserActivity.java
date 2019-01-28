package com.xiaolian.amigo.ui.device.dispenser;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ObjectsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.integration.android.IntentIntegrator;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
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
import com.xiaolian.amigo.ui.device.washer.ScanActivity;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutFooter;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutHeader;
import com.xiaolian.amigo.util.AppUtils;
import com.xiaolian.amigo.util.Log;
import com.xiaolian.amigo.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.xiaolian.amigo.ui.device.washer.ScanActivity.IS_SACN;
import static com.xiaolian.amigo.ui.device.washer.ScanActivity.SCAN_TYPE;
import static com.xiaolian.amigo.ui.main.MainActivity.INTENT_KEY_AFTER_ORDER_COPY;
import static com.xiaolian.amigo.ui.main.MainActivity.INTENT_KEY_PRE_ORDER_COPY;
import static com.xiaolian.amigo.ui.main.MainActivity.INTENT_KEY_RESIDENCE_ID;

/**
 * 附近饮水机页面
 *
 * @author zcd
 * @date 17/9/20
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

    private TextView tvNearby;
    private TextView tvFavorite;

    private LinearLayout llFooter;

    private RecyclerView recyclerView;
    private SmartRefreshLayout refreshLayout;

    int action = DeviceConstant.ACTION_CHOOSE_DISPENSER;
    private RelativeLayout rlEmpty;
    private RelativeLayout rlError;
    private OrderPreInfoDTO orderPreInfo;
    private TextView tvRescan;
    private TextView tvEmptyTip;
    private LinearLayout llQrCodeScan ;
    private int deviceType;

    //  是否显示蓝牙扫描按钮
    private boolean canShowScanButton = false ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_dispenser);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(ChooseDispenserActivity.this);
        presenter.setDeviceType(deviceType);
        initRecyclerView();

        initRefreshLayout();

        rlEmpty = findViewById(R.id.rl_empty);
        rlError = findViewById(R.id.rl_error);

        tvEmptyTip = findViewById(R.id.tv_empty_tip);

        tvRescan = findViewById(R.id.tv_rescan);
        tvRescan.setOnClickListener(v -> onReScan());

        llFooter = findViewById(R.id.ll_footer);
        llQrCodeScan = findViewById(R.id.ll_qr_code_scan);
        llQrCodeScan.setOnClickListener(v-> gotoScan());
        tvNearby = findViewById(R.id.tv_toolbar_title);
        tvNearby.setOnClickListener(v -> onNearbyClick());
        tvFavorite = findViewById(R.id.tv_toolbar_title2);
        tvFavorite.setOnClickListener(v -> onFavoriteClick());



        switch (action) {
            case DeviceConstant.ACTION_CHOOSE_DISPENSER:
            case DeviceConstant.ACTION_CHANGE_DISPENSER:
                break;
            case DeviceConstant.ACTION_CHOOSE_DRYER:
            case DeviceConstant.ACTION_CHANGE_DRYER:
                tvNearby.setText(R.string.nearby_hair_dryer);
                tvFavorite.setText(R.string.favorite_hair_dryer);
                showBleScanButton();
                break;
            case DeviceConstant.ACTION_CHANGE_DRYER_AND_H5:
                tvNearby.setText(R.string.nearby_hair_dryer);
                tvFavorite.setText(R.string.favorite_hair_dryer);
                showScanButton();
                break;
            default:
                break;
        }
    }
    
    /**
     * 去扫描二维码界面
     */
    private void gotoScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        //底部的提示文字，设为""可以置空
        integrator.setPrompt("");
        //前置或者后置摄像头
        integrator.setCameraId(0);
        //扫描成功的「哔哔」声，默认开启
        integrator.setBeepEnabled(false);
        integrator.setCaptureActivity(ScanActivity.class);
        integrator.setOrientationLocked(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.addExtra(DecodeHintType.CHARACTER_SET.name(), "utf-8");
        integrator.addExtra(DecodeHintType.TRY_HARDER.name(), Boolean.TRUE);
        integrator.addExtra(DecodeHintType.POSSIBLE_FORMATS.name(), BarcodeFormat.QR_CODE);
        integrator.addExtra(SCAN_TYPE, 2);
        integrator.addExtra(IS_SACN, true);
        integrator.initiateScan();
    }


    /**
     * 显示二维码扫码按钮
     */
    private void showScanButton(){
        llFooter.setVisibility(View.GONE);
        llQrCodeScan.setVisibility(View.VISIBLE);

        canShowScanButton = false ;
    }

    /**
     * 显示蓝牙扫描按钮
     */
    private void showBleScanButton(){
        llFooter.setVisibility(View.VISIBLE);
        llQrCodeScan.setVisibility(View.GONE);
        canShowScanButton = true ;
    }

    private void initRefreshLayout() {
        refreshLayout = findViewById(R.id.refreshLayout);

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(com.scwang.smartrefresh.layout.api.RefreshLayout refreshlayout) {
                if (listStatus) {
                    presenter.requestFavorites();
                } else {
                    hideScanStopView();
                    presenter.onLoad();
                }
            }
        });
        refreshLayout.setRefreshHeader(new RefreshLayoutHeader(this));
        refreshLayout.setRefreshFooter(new RefreshLayoutFooter(this));
        refreshLayout.setReboundDuration(200);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.autoRefresh(0);
    }

    private void initRecyclerView() {
        adaptor = new ChooseDispenserAdaptor(this, R.layout.item_dispenser,
                items, Device.getDevice(deviceType));
        adaptor.setOnItemClickListener((deviceNo, supplierId, isFavor, residenceId, usefor, location, price ,preOrderCopy , afterOrderCopy) -> {
            if (orderPreInfo != null) {
                orderPreInfo.setPrice(price);
            }
            if (Device.getDevice(deviceType) == Device.DISPENSER) {
                presenter.closeBleConnection();
                presenter.gotoDispenser(deviceNo, supplierId, isFavor, residenceId, usefor, location , preOrderCopy , afterOrderCopy);
            } else if (Device.getDevice(deviceType) == Device.DRYER) {
                presenter.closeBleConnection();
                presenter.gotoDryer(deviceNo, supplierId, isFavor, residenceId, location);
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 14)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);
    }

    private void onReScan() {
        presenter.startTimer();
        tvRescan.setVisibility(View.GONE);
        refreshLayout.autoRefresh(100);
        hideScanStopView();
    }

    private void hideScanStopView() {
        hideEmptyView();
        if (canShowScanButton) {
            llFooter.setVisibility(View.VISIBLE);
        }
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
            tvRescan.setVisibility(View.GONE);
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
            if (canShowScanButton) {
                llFooter.setVisibility(View.VISIBLE);
            }
            listStatus = false;
            tvNearby.setTextColor(ContextCompat.getColor(this, R.color.colorDark2));
            tvFavorite.setTextColor(ContextCompat.getColor(this, R.color.colorDarkB));
        } else {
            llFooter.setVisibility(View.GONE);
            listStatus = true;
            tvNearby.setTextColor(ContextCompat.getColor(this, R.color.colorDarkB));
            tvFavorite.setTextColor(ContextCompat.getColor(this, R.color.colorDark2));
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
        items.clear();
        adaptor.notifyDataSetChanged();
        rlEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        tvEmptyTip.setText(R.string.empty_tip);
        rlEmpty.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView() {
        items.clear();
        adaptor.notifyDataSetChanged();
        rlError.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideErrorView() {
        rlError.setVisibility(View.GONE);
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
    public void gotoDispenser(String macAddress, Long supplierId, boolean favor, Long residenceId, String usefor, String location
                    , List<String> preOrderCopy , List<String> afterOrderCopy) {
        startActivity(new Intent(this, DispenserActivity.class)
                .putExtra(MainActivity.INTENT_KEY_MAC_ADDRESS,
                        macAddress)
                .putExtra(MainActivity.INTENT_KEY_SUPPLIER_ID, supplierId)
                .putExtra(DispenserActivity.INTENT_KEY_FAVOR,
                        favor)
                .putExtra(DispenserActivity.INTENT_KEY_ID,
                        residenceId)
                .putExtra(INTENT_KEY_RESIDENCE_ID , residenceId)
                .putExtra(DispenserActivity.INTENT_KEY_TEMPERATURE,
                        usefor)
                .putExtra(MainActivity.INTENT_KEY_LOCATION, location)
                .putExtra(MainActivity.INTENT_KEY_DEVICE_TYPE, Device.DISPENSER.getType())
                .putExtra(WaterDeviceBaseActivity.INTENT_PREPAY_INFO, orderPreInfo)
                .putStringArrayListExtra(INTENT_KEY_PRE_ORDER_COPY , (ArrayList<String>) preOrderCopy)
                .putStringArrayListExtra(INTENT_KEY_AFTER_ORDER_COPY , (ArrayList<String>) afterOrderCopy));
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
                .putExtra(INTENT_KEY_RESIDENCE_ID ,residenceId)
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
        llFooter.setVisibility(View.GONE);
        showEmptyView();
        tvRescan.setVisibility(View.VISIBLE);
        tvEmptyTip.setText(String.format("未扫描出附近的%s", Device.getDevice(deviceType).getDesc()));
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
                    if (ObjectsCompat.equals(scanDeviceGroup.getResidenceId(),
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
            if (ObjectsCompat.equals(contained.getId(), device.getId())) {
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
    public String getAppVersion() {
        return AppUtils.getVersionName(this);
    }

    @Override
    protected void onDestroy() {
        presenter.cancelTimer();
        presenter.onDetach();
        super.onDestroy();
    }
}
