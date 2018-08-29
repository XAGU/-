package com.xiaolian.amigo.ui.device;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.data.enumeration.TradeStep;
import com.xiaolian.amigo.di.componet.DaggerDeviceActivityComponent;
import com.xiaolian.amigo.di.componet.DeviceActivityComponent;
import com.xiaolian.amigo.di.module.DeviceActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;
import com.xiaolian.amigo.ui.device.intf.IDeviceView;
import com.xiaolian.amigo.util.Log;


/**
 * 设备baseActivity
 *
 * @author caidong
 * @date 17/9/28
 */
public abstract class DeviceBaseActivity extends BaseActivity implements IDeviceView {

    private static final String TAG = DeviceBaseActivity.class.getSimpleName();
    private DeviceActivityComponent mActivityComponent;
    /**
     * 设备mac地址
     */
    protected String macAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp();
        mActivityComponent = DaggerDeviceActivityComponent.builder()
                .deviceActivityModule(new DeviceActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();

    }



    public DeviceActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    @Override
    public void onWriteError() {
        Log.e(TAG, "onWriteError");
    }

    @Override
    public void onNotifyError() {
        Log.e(TAG, "onNotifyError");
    }

    @Override
    public void onStatusError() {
        Log.e(TAG, "onStatusError");
    }


    @Override
    protected void setUp() {

    }

    @Override
    public void onConnectError() {

    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onFinish(long orderId) {

    }

    @Override
    public void onConnectSuccess(TradeStep step, Object... extra) {

    }

    @Override
    public void onReconnectSuccess(Object... extra) {

    }
}
