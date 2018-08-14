package com.xiaolian.amigo.ui.device.bathroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderCurrentRespDTO;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.device.DeviceOrderActivity;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBathroomHeartPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBathroomHeartView;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.widget.BezierWaveView;
import com.xiaolian.amigo.ui.widget.swipebutton.SlideUnlockView;
import com.xiaolian.amigo.util.Constant;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiaolian.amigo.ui.device.DeviceOrderActivity.KEY_USER_STYLE;

public class BathroomHeaterActivity extends BathroomBaseActivity implements IBathroomHeartView {

    public static final String KEY_BATH_ORDER_ID = "KEY_BATH_ORDER_ID";
    @Inject
    IBathroomHeartPresenter<IBathroomHeartView> presenter;


    @BindView(R.id.tv_device_title)
    TextView tvDeviceTitle;
    @BindView(R.id.iv_top_right_icon)
    ImageView ivTopRightIcon;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.fl_bottom)
    FrameLayout flBottom;
    @BindView(R.id.iv_header_icon)
    ImageView ivHeaderIcon;
    @BindView(R.id.bsv_wave)
    BezierWaveView bsvWave;
    @BindView(R.id.tv_shower_payed)
    TextView tvShowerPayed;
    @BindView(R.id.trade_tip)
    TextView tradeTip;
    @BindView(R.id.slideView)
    SlideUnlockView slideView;
    @BindView(R.id.ll_content_shower)
    LinearLayout llContentShower;
    @BindView(R.id.bt_stop_shower)
    Button btStopShower;

    private long orderId;

    private BathOrderCurrentRespDTO bathOrderRespDTO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bathroom_heater);
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        ButterKnife.bind(this);
        setUp();
        initPreView();
        initView();
//        initTextData();
    }

    @Override
    protected void setUp() {
        if (getIntent() != null) {
            orderId = getIntent().getLongExtra(KEY_BATH_ORDER_ID, -1);
        }
    }

    private void initView() {
        if (orderId == -1) {
            onError("订单数据出错");
            this.finish();
            return;
        }
        presenter.queryBathroomOrder(orderId , true);

    }

    private void initPreView() {
        ivTopRightIcon.setImageResource(R.drawable.ic_question);
        rlHeader.setBackgroundResource(R.drawable.bg_rect_red);
        flBottom.setBackgroundResource(R.color.heater_gradient_start);
        ivHeaderIcon.setImageResource(R.drawable.ic_shower);
        llContentShower.setVisibility(View.VISIBLE);
        slideView.setVisibility(View.VISIBLE);
        btStopShower.setVisibility(View.GONE);
        slideView.setDisableStr(getString(R.string.slide_to_settlement));
        slideView.setEnableStr(getString(R.string.settlement));
        tradeTip.setText("用水结束后，请务必滑动下方按钮，进行结算找零\n消费金额以实际用水量为准");
    }


    private void initTextData() {
        tvDeviceTitle.setText("3楼1栋");

        if (bsvWave != null && !bsvWave.isRunning()) {
            bsvWave.startAnim();
        }
        tvShowerPayed.setText("已预付5元");
        flBottom.setVisibility(View.VISIBLE);
        String tip = "用水结束后，请务必滑动下方按钮，进行结算找零\n消费金额以实际用水量为准" ;

        tradeTip.setText(tip.replace("\\n" ,"\n"));

    }

    private void initData() {
        tvDeviceTitle.setText(bathOrderRespDTO.getLocation());
        ivTopRightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WebActivity.class)
                        .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_HELP));
            }
        });
        if (bsvWave != null && !bsvWave.isRunning()) {
            bsvWave.startAnim();
        }

        tvShowerPayed.setText("已预付" + bathOrderRespDTO.getPrepayAmount() + "元");
        tradeTip.setText(getString(R.string.tip));
        slideView.setOnUnLockListener(new SlideUnlockView.OnUnLockListener() {
            @Override
            public void setUnLocked(boolean lock) {
                if (lock) {
                    presenter.askSettle(bathOrderRespDTO.getTradeOrderId());
                }
            }
        });
    }


    @Override
    public void goToOrderInfo(BathOrderCurrentRespDTO dto) {
        String userMethod ="";
        if (dto.getLocation().equals("任意空浴室")){
            userMethod = "预约任意空浴室";
        }else{
            userMethod = "预约指定浴室";
        }
        Intent intent = new Intent(this, BathOrderActivity.class);
        intent.putExtra(Constant.BUNDLE_ID, dto.getTradeOrderId());
        intent.putExtra(KEY_USER_STYLE ,userMethod);
        startActivity(intent);
    }

    @Override
    public void getOrderInfo(BathOrderCurrentRespDTO dto) {
        if (dto != null) {
            bathOrderRespDTO = dto;
            initData();
        }
    }

    @Override
    public void reset() {
        slideView.reset();
    }

    @Override
    public void goToOrderInfo() {
        String userMethod ="";
        if (bathOrderRespDTO.getLocation().equals("任意空浴室")){
            userMethod = "预约任意空浴室";
        }else{
            userMethod = "预约指定浴室";
        }
        Intent intent = new Intent(this, BathOrderActivity.class);
        intent.putExtra(Constant.BUNDLE_ID, bathOrderRespDTO.getTradeOrderId());
        intent.putExtra(KEY_USER_STYLE ,userMethod);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }

    @OnClick(R.id.iv_back)
    public void back(View view) {
        startActivity(new Intent(this , MainActivity.class));
    }

}
