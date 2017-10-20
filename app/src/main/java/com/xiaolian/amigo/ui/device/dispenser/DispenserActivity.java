package com.xiaolian.amigo.ui.device.dispenser;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.DispenserWater;
import com.xiaolian.amigo.data.enumeration.TradeStep;
import com.xiaolian.amigo.data.network.model.device.ScanDeviceGroup;
import com.xiaolian.amigo.ui.device.WaterDeviceBaseActivity;
import com.xiaolian.amigo.ui.device.intf.dispenser.IDispenserPresenter;
import com.xiaolian.amigo.ui.device.intf.dispenser.IDispenserView;

import java.util.List;

import javax.inject.Inject;

/**
 * 饮水机页面
 * @author zcd
 */
public class DispenserActivity extends WaterDeviceBaseActivity<IDispenserPresenter> implements IDispenserView {

    public static final String INTENT_KEY_FAVOR = "intent_key_favor";
    public static final String INTENT_KEY_ID = "intent_key_id";
    public static final String INTENT_KEY_TEMPERATURE = "intent_key_temperature";
    private boolean isFavor;
    private Long id;
    private DispenserWater temperature;
    @Inject
    IDispenserPresenter<IDispenserView> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null) {
            isFavor = getIntent().getBooleanExtra(INTENT_KEY_FAVOR, false);
            id = getIntent().getLongExtra(INTENT_KEY_ID, -1);
            temperature = DispenserWater.getTemperature(getIntent().getStringExtra(INTENT_KEY_TEMPERATURE));
        }
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
    protected String setSubtitleString() {
        return "切换饮水机";
    }

    @Override
    protected void setTempText(TextView tempText) {
        tempText.setVisibility(View.VISIBLE);
        tempText.setText("当前水温：" + temperature.getDesc());
    }

    @Override
    protected View.OnClickListener setTitleClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDispenser();
            }
        };
    }

    public void changeDispenser() {
        // 只有在step为SETILE时才不能更换饮水机
        if (presenter.getStep() != TradeStep.SETTLE) {
            onBackPressed();
        }
    }


    @Override
    protected View.OnClickListener setTopRightIconClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 设置饮水机事件
                if (isFavor) {
                    presenter.unFavorite(id);
                } else {
                    presenter.favorite(id);
                }
            }
        };
    }

    @Override
    protected int setTopRightIconDrawable() {
        if (isFavor) {
            return R.drawable.collected;
        } else {
            return R.drawable.uncollected;
        }
    }

    @Override
    protected int setHeaderIconDrawable() {
        return R.drawable.ic_dispenser;
    }

    @Override
    public IDispenserPresenter setPresenter() {
        return presenter;
    }

    @Override
    public void setFavoriteIcon() {
        isFavor = true;
        setTopRightIcon(R.drawable.collected);
    }

    @Override
    public void setUnFavoriteIcon() {
        isFavor = false;
        setTopRightIcon(R.drawable.uncollected);
    }
}
