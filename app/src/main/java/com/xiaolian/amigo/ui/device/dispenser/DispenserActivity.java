package com.xiaolian.amigo.ui.device.dispenser;

import android.os.Bundle;
import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.WaterDeviceBaseActivity;
import com.xiaolian.amigo.ui.device.intf.dispenser.IDispenserPresenter;
import com.xiaolian.amigo.ui.device.intf.dispenser.IDispenserView;

import javax.inject.Inject;

/**
 * 饮水机页面
 * @author zcd
 */
public class DispenserActivity extends WaterDeviceBaseActivity<IDispenserPresenter> implements IDispenserView {

    @Inject
    IDispenserPresenter<IDispenserView> presenter;

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
        return R.drawable.bg_rect_green;
    }

    @Override
    protected View.OnClickListener setTopRightIconClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 设置饮水机事件
            }
        };
    }

    @Override
    protected int setTopRightIconDrawable() {
        return R.drawable.uncollected;
    }

    @Override
    protected int setHeaderIconDrawable() {
        return R.drawable.ic_dispenser;
    }

    @Override
    public IDispenserPresenter setPresenter() {
        return presenter;
    }

}
