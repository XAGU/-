package com.xiaolian.amigo.ui.device.bathroom;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBathroomPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBathroomView;
import com.xiaolian.amigo.ui.widget.BezierWaveView;
import com.xiaolian.amigo.ui.widget.swipebutton.SlideUnlockView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zcd
 * @date 18/6/29
 */
public class BathroomActivity extends BathroomBaseActivity implements IBathroomView {

    @Inject
    IBathroomPresenter<IBathroomView> presenter;

    @BindView(R.id.ll_content_shower)
    LinearLayout llContentShower;

    @BindView(R.id.trade_tip)
    TextView tvTradeTip;

    @BindView(R.id.tv_shower_payed)
    TextView tvShowerPayed;

    @BindView(R.id.slideView)
    SlideUnlockView slideView;

    @BindView(R.id.bt_stop_shower)
    Button btStopShower;

    @BindView(R.id.bsv_wave)
    BezierWaveView bsvWave;

    @BindView(R.id.fl_bottom)
    FrameLayout flBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bathroom_shower);
        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);
        presenter.onAttach(this);

        initView();
    }

    private void initView() {
        llContentShower.setVisibility(View.VISIBLE);
        tvTradeTip.setText(getString(R.string.balance_trade_tip_water));
        tvShowerPayed.setText("已预付8元");
        slideView.setVisibility(View.VISIBLE);
        btStopShower.setVisibility(View.GONE);
        slideView.setOnUnLockListener(lock -> {
            if (lock) {
                onSlideUnlock();
            }
        });
        if (bsvWave != null && !bsvWave.isRunning()) {
            bsvWave.startAnim();
        }
        flBottom.setBackgroundResource(R.color.heater_gradient_start);
    }

    private void onSlideUnlock() {
        onSuccess("slideUnlock");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && bsvWave != null && !bsvWave.isRunning()) {
            bsvWave.startAnim();
        }
    }
}
