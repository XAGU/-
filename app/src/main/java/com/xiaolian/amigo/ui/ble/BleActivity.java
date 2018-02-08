package com.xiaolian.amigo.ui.ble;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.util.Log;

import android.view.View;

import com.polidea.rxandroidble.scan.ScanResult;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.util.ScreenUtils;
import com.xiaolian.amigo.ui.ble.adaptor.BleAdaptor;
import com.xiaolian.amigo.ui.ble.intf.IBlePresenter;
import com.xiaolian.amigo.ui.ble.intf.IBleView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 测试BLE
 *
 * @author caidong
 * @date 17/9/21
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BleActivity extends BleBaseActivity implements IBleView {

    private static final String TAG = BleActivity.class.getSimpleName();

    @Inject
    IBlePresenter<IBleView> presenter;
    @BindView(R.id.rv_devices)
    RecyclerView rv_devices;

    List<BleAdaptor.Device> devices = new ArrayList<>();
    BleAdaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);

        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(BleActivity.this);

        adaptor = new BleAdaptor(devices);
        rv_devices.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 14)));
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_devices.setLayoutManager(manager);
        rv_devices.setAdapter(adaptor);

        // 开启蓝牙
        getBlePermission();
    }


    @Override
    protected void setUp() {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void addDevice(ScanResult result) {
        BleAdaptor.Device device = new BleAdaptor.Device(result);
        if (!this.devices.contains(device)) {
            this.devices.add(device);
        }
        adaptor.notifyDataSetChanged();
    }

    @Override
    public void onScanError() {
        Log.e(TAG, "扫描设备失败");
    }

    @OnClick(R.id.scan)
    public void sendMessage(View view) {
        presenter.onScan();
    }

}