package com.xiaolian.amigo.ui.device.heater;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.device.WaterDeviceBaseActivity;
import com.xiaolian.amigo.ui.device.intf.IWaterDeviceBasePresenter;
import com.xiaolian.amigo.ui.device.intf.heator.IHeaterPresenter;
import com.xiaolian.amigo.ui.device.intf.heator.IHeaterView;
import com.xiaolian.amigo.util.Constant;

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
    protected View.OnClickListener setTopRightIconClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WebActivity.class)
                        .putExtra(WebActivity.INTENT_KEY_URL, Constant.HELP_URL));
            }
        };
    }

    @Override
    protected int setTopRightIconDrawable() {
        return R.drawable.ic_question;
    }

    @Override
    protected int setHeaderIconDrawable() {
        return R.drawable.ic_shower;
    }

    @Override
    public IHeaterPresenter setPresenter() {
        return presenter;
    }
}
