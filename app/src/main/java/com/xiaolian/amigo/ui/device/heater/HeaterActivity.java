package com.xiaolian.amigo.ui.device.heater;

import android.os.Bundle;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.WaterDeviceBaseActivity;
import com.xiaolian.amigo.ui.device.intf.IWaterDeviceBasePresenter;
import com.xiaolian.amigo.ui.device.intf.heator.IHeaterPresenter;
import com.xiaolian.amigo.ui.device.intf.heator.IHeaterView;

import javax.inject.Inject;

/**
 * 热水澡设备页
 *
 * @author zcd
 */
public class HeaterActivity extends WaterDeviceBaseActivity<IHeaterPresenter> implements IHeaterView {
    @Inject
    IHeaterPresenter<IHeaterView> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
        presenter.onAttach(this);
    }

    @Override
    protected int setHeaderBackgroundDrawable() {
        return R.drawable.bg_rect_red;
    }

    @Override
    public IHeaterPresenter setPresenter() {
        return presenter;
    }
}
