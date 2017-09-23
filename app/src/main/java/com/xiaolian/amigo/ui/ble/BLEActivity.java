package com.xiaolian.amigo.ui.ble;

/**
 * 测试BLE
 * <p>
 * Created by caidong on 2017/9/21.
 */

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.polidea.rxandroidble.scan.ScanResult;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.ble.adaptor.BLEAdaptor;
import com.xiaolian.amigo.ui.ble.intf.IBLEPresenter;
import com.xiaolian.amigo.ui.ble.intf.IBLEView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BLEActivity extends BLEBaseActivity implements IBLEView {

    private static final String TAG = BLEActivity.class.getSimpleName();

    @Inject
    IBLEPresenter<IBLEView> presenter;

    List<BLEAdaptor.Device> devices = new ArrayList<>();
    BLEAdaptor adaptor;

    @Override
    protected void initData() {
        // 开启蓝牙
        getBLEPermission();
    }


    @Override
    protected void initPresenter() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(BLEActivity.this);
    }

    @Override
    protected void setUp() {

    }

    @Override
    protected RecyclerView.Adapter getAdaptor() {
        adaptor = new BLEAdaptor(this, R.layout.item_ble_device, devices);
        return adaptor;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_ble;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void addDevice(ScanResult result) {
        BLEAdaptor.Device device = new BLEAdaptor.Device(result);
        if (!this.devices.contains(device)) {
            this.devices.add(device);
        }
        adaptor.notifyDataSetChanged();
    }

    @Override
    public void onScanError() {
        Log.e(TAG, "扫描设备失败");
    }

    @Override
    public void onConnectError() {
        Log.e(TAG, "连接设备失败");
    }

    @Override
    public void onWriteError() {
        Log.e(TAG, "写设备失败");
    }

    @Override
    public void onNotifyError() {
        Log.e(TAG, "读设备失败");
    }

    @Override
    public void onStatusError() {
        Log.e(TAG, "设备连接已断开");
    }

    @OnClick(R.id.scan)
    public void sendMessage(View view) {
        presenter.onScan();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void initRefreshLayout() {

    }
}