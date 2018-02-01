package com.xiaolian.amigo.ui.device;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.di.componet.DaggerDeviceActivityComponent;
import com.xiaolian.amigo.di.componet.DeviceActivityComponent;
import com.xiaolian.amigo.di.module.DeviceActivityModule;
import com.xiaolian.amigo.ui.base.BaseToolBarListActivity;

/**
 * 设备列表
 *
 * @author zcd
 * @date 17/9/18
 */

public abstract class DeviceBaseListActivity extends BaseToolBarListActivity {
    @Override
    protected void setUp() {

    }

    private DeviceActivityComponent mActivityComponent;

    @Override
    protected void initInject() {
        mActivityComponent = DaggerDeviceActivityComponent.builder()
                .deviceActivityModule(new DeviceActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();
    }

    public DeviceActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

}
