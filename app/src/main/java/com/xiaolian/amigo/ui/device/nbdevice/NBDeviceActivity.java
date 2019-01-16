package com.xiaolian.amigo.ui.device.nbdevice;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.di.componet.DaggerDeviceActivityComponent;
import com.xiaolian.amigo.ui.base.BaseActivity;
import com.xiaolian.amigo.ui.device.nbdevice.intf.INBDevicePresenter;
import com.xiaolian.amigo.ui.device.nbdevice.intf.INBDeviceView;
import com.xiaolian.amigo.di.componet.DeviceActivityComponent;
import com.xiaolian.amigo.di.module.DeviceActivityModule;
import javax.inject.Inject;
import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.R;
import butterknife.ButterKnife;

public class NBDeviceActivity extends BaseActivity implements INBDeviceView {

    private DeviceActivityComponent mActivityComponent;

    @Inject
    INBDevicePresenter<INBDeviceView>presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_offline_device);
        ButterKnife.bind(this);
        setUp();
        mActivityComponent = DaggerDeviceActivityComponent.builder()
                .deviceActivityModule(new DeviceActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();
        mActivityComponent.inject(this);
        presenter.onAttach(this);
    }

    @Override
    protected void setUp() {

    }
}
