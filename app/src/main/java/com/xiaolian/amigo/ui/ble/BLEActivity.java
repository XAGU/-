package com.xiaolian.amigo.ui.ble;

/**
 * 测试BLE
 * <p>
 * Created by caidong on 2017/9/21.
 */

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;

import com.polidea.rxandroidble.scan.ScanResult;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.ble.intf.IBLEPresenter;
import com.xiaolian.amigo.ui.ble.intf.IBLEView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BLEActivity extends BLEBaseActivity implements IBLEView {

    private static final String TAG = BLEActivity.class.getSimpleName();
    @Inject
    IBLEPresenter<IBLEView> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);

        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(BLEActivity.this);
        // 开启蓝牙
        getBLEPermission();
    }

    @Override
    protected void setUp() {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void addDevice(ScanResult result) {
        Log.i(TAG, "添加设备成功：" + result.getBleDevice().getMacAddress());
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

    @OnClick(R.id.scan)
    public void sendMessage(View view) {
        presenter.onScan();
    }
}