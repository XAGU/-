package com.xiaolian.amigo.ui.device.bathroom;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ObjectsCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.ComplaintType;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.di.componet.DaggerDeviceActivityComponent;
import com.xiaolian.amigo.di.componet.DeviceActivityComponent;
import com.xiaolian.amigo.di.module.DeviceActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.device.DeviceOrderActivity;
import com.xiaolian.amigo.ui.device.intf.IDeviceOrderPresenter;
import com.xiaolian.amigo.ui.device.intf.IDeviceOrderView;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.order.OrderDetailActivity;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BathOrderActivity extends BaseActivity implements IDeviceOrderView {

    private static final String TAG = BathOrderActivity.class.getSimpleName();
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_third)
    TextView tvTitleThird;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    private DeviceActivityComponent mActivityComponent;

    public static final String KEY_USER_STYLE = "KEY_USER_STYLE"; // 使用方式
    private static final int ERROR_ORDER_STATUS = 3;
//    /**
//     * 账单标题
//     */
//    @BindView(R.id.tv_order_title)
//    TextView tvOrderTitle;
    /************** 正常账单内容 ****************/
    @BindView(R.id.ll_order_normal)
    LinearLayout llOrderNormal;
    @BindView(R.id.rl_use_bonus)
    RelativeLayout rlUseBonus;
    /**
     * 代金券抵扣
     */
    @BindView(R.id.tv_bonus_remark)
    TextView tvBonusRemark;
    /**
     * 预付金额
     */
    @BindView(R.id.tv_prepay)
    TextView tvPrepay;
    /**
     * 代金券标题
     */
    @BindView(R.id.tv_bonus_title)
    TextView tvBonusTitle;
    /**
     * 实际扣款
     */
    @BindView(R.id.tv_actual_debit)
    TextView tvActualDebit;
    @BindView(R.id.rl_actual_debit)
    RelativeLayout rlActualDebit;
    /**
     * 实际消费
     */
    @BindView(R.id.tv_consume)
    TextView tvConsume;
    /**
     * 找零金额
     */
    @BindView(R.id.tv_odd)
    TextView tvOdd;


    /************** 异常账单内容 ****************/
    @BindView(R.id.tv_order_error_tip)
    TextView tvOrderErrorTip;
    @BindView(R.id.ll_order_error)
    LinearLayout llOrderError;
    @BindView(R.id.rl_back_bonus)
    RelativeLayout rlBackBonus;
    /**
     * 退还代金券
     */
    @BindView(R.id.tv_back_bonus)
    TextView tvBackBonus;
    /**
     * 退还金额
     */
    @BindView(R.id.tv_back_amount)
    TextView tvBackAmount;

    /************** 基础信息 *****************/

    /**
     * 使用时间
     */
    @BindView(R.id.tv_time)
    TextView tvTime;
    /**
     * 使用时长
     */
    @BindView(R.id.tv_used_time)
    TextView tvUsedTime;
    @BindView(R.id.rl_used_time)
    RelativeLayout rlUsedTime;
    /**
     * 设备位置
     */
    @BindView(R.id.tv_device_location)
    TextView tvDeviceLocation;
    /**
     * 订单号
     */
    @BindView(R.id.tv_order_no)
    TextView tvOrderNo;
    /**
     * 是否用水提示
     */
    @BindView(R.id.tv_order_no_use_tip)
    TextView tvOrderNoUseTip;

    /******************** 底部线条 **********************/
    @BindView(R.id.v_bottom_line1)
    View vBottomLine1;
    @BindView(R.id.v_bottom_line2)
    View vBottomLine2;

    @BindView(R.id.tv_method)
    TextView tvMethod;
    @BindView(R.id.rl_user_style)
    RelativeLayout rlUserStyle;

    private String tv_user_style;
    @Inject
    IDeviceOrderPresenter<IDeviceOrderView> presenter;
    private String orderNo;
    private long orderId;
    private Integer orderType;

    protected void initInject() {
        mActivityComponent = DaggerDeviceActivityComponent.builder()
                .deviceActivityModule(new DeviceActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();
    }


    @Override
    public void setRefreshComplete(OrderDetailRespDTO respDTO) {
        // 设置基础信息
        orderType = ComplaintType.getComplaintTypeByDeviceType(
                Device.getDevice(respDTO.getDeviceType())).getType();
        tvTime.setText(CommonUtil.stampToDate(respDTO.getCreateTime()));
        if (Device.getDevice(respDTO.getDeviceType()) == Device.DRYER) {
            rlUsedTime.setVisibility(View.VISIBLE);
            tvUsedTime.setText(respDTO.getUseTime());
        }

        if (!TextUtils.isEmpty(tv_user_style)) {
            rlUserStyle.setVisibility(View.VISIBLE);
            tvMethod.setText(tv_user_style);
        } else {
            rlUserStyle.setVisibility(View.GONE);
        }

        vBottomLine1.setBackgroundColor(Color.parseColor(OrderDetailActivity.getLineColorByDeviceType(respDTO.getDeviceType())));
        vBottomLine1.setVisibility(View.VISIBLE);
        vBottomLine2.setBackgroundColor(Color.parseColor(OrderDetailActivity.getLineColorByDeviceType(respDTO.getDeviceType())));
        vBottomLine2.setVisibility(View.VISIBLE);

        Device device = Device.getDevice(respDTO.getDeviceType());
        if (device != null) {
            tvDeviceLocation.setText(String.format("%s %s", device.getDesc(), respDTO.getLocation()));
        } else {
            tvDeviceLocation.setText(String.format("未知设备 %s", respDTO.getLocation()));
        }
        orderNo = respDTO.getOrderNo();
        tvOrderNo.setText(orderNo);
        if (ObjectsCompat.equals(respDTO.getStatus(), ERROR_ORDER_STATUS)) {
            // 异常账单
            tvOrderErrorTip.setVisibility(View.VISIBLE);
            llOrderNormal.setVisibility(View.GONE);
            llOrderError.setVisibility(View.VISIBLE);
            // 是否有代金券
            if (TextUtils.isEmpty(respDTO.getBonus())) {
                // 没有代金券
                rlBackBonus.setVisibility(View.GONE);
                tvConsume.setTextColor(ContextCompat.getColor(this, R.color.colorFullRed));
                tvActualDebit.setTextColor(ContextCompat.getColor(this, R.color.colorDark6));
                rlActualDebit.setVisibility(View.GONE);
            } else {
                // 有代金券
                rlBackBonus.setVisibility(View.VISIBLE);
                tvBackBonus.setText(respDTO.getBonus());
                tvConsume.setTextColor(ContextCompat.getColor(this, R.color.colorDark6));
                tvActualDebit.setTextColor(ContextCompat.getColor(this, R.color.colorFullRed));
            }
            tvBackAmount.setText(respDTO.getPrepay());
        } else {
            // 正常账单
            tvOrderErrorTip.setVisibility(View.GONE);
            llOrderNormal.setVisibility(View.VISIBLE);
            llOrderError.setVisibility(View.GONE);
            // 是否有代金券
            if (TextUtils.isEmpty(respDTO.getBonus())) {
                // 没有代金券
                rlUseBonus.setVisibility(View.GONE);
                tvConsume.setTextColor(ContextCompat.getColor(this, R.color.colorFullRed));
                tvActualDebit.setTextColor(ContextCompat.getColor(this, R.color.colorDark6));
                rlActualDebit.setVisibility(View.GONE);
            } else {
                // 有代金券
                rlUseBonus.setVisibility(View.VISIBLE);
                tvConsume.setTextColor(ContextCompat.getColor(this, R.color.colorDark6));
                tvActualDebit.setTextColor(ContextCompat.getColor(this, R.color.colorFullRed));
                // 实际消费为0
                if (TextUtils.equals(respDTO.getConsume(), "¥0")) {
                    rlActualDebit.setVisibility(View.GONE);
                    tvBonusTitle.setText("退还代金券：");
                    tvBonusRemark.setText(respDTO.getBonus());
                    tvConsume.setTextColor(ContextCompat.getColor(this, R.color.colorFullRed));
                } else {
                    tvBonusRemark.setText(getString(R.string.minus, respDTO.getBonus()));
                }
            }


            tvConsume.setText(respDTO.getConsume());
            tvPrepay.setText(respDTO.getPrepay());
            tvOdd.setText(respDTO.getOdd());
            tvActualDebit.setText(getString(R.string.minus, respDTO.getActualDebit()));
        }
    }

    @Override
    public void toComplaint() {

    }

    @Override
    public void showNoUseTip() {
        tvOrderNoUseTip.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bath_order);
        setUnBinder(ButterKnife.bind(this));
        initInject();
        mActivityComponent.inject(this);
        presenter.onAttach(this);
        setUp();
        if (orderId != 0L) {
            presenter.onLoad(orderId);
        }

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            //Log.d("STATE", appBarLayout.getTotalScrollRange() +"//"+ verticalOffset+"//"+tv_toolbar_title.getHeight());
            if (verticalOffset < -(tvToolbarTitle.getHeight() + llHeader.getPaddingTop())) {
                setTitleVisiable(View.VISIBLE);
            } else {
                setTitleVisiable(View.GONE);
            }
        });

    }

    @OnClick({R.id.tv_toolbar_sub_title , R.id.tv_title_third})
    public void toComplaint(View view){
        startActivity(new Intent(this, WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL,
                        Constant.H5_COMPLAINT
                                + "?accessToken=" + presenter.getAccessToken()
                                +"&refreshToken=" + presenter.getRefreshToken()
                                + "&orderId=" + orderId
                                + "&orderNo=" + orderNo
                                + "&orderType=" + orderType));
    }


    /**
     * 设置title是否显示
     *
     * @param visiable
     */
    private void setTitleVisiable(int visiable) {
        tvTitle.setVisibility(visiable);
        tvTitleThird.setVisibility(visiable);
        viewLine.setVisibility(visiable);
    }

    @Override
    protected void setUp() {
        if (getIntent() != null) {
            Intent intent = getIntent();
            orderId = intent.getLongExtra(Constant.BUNDLE_ID, 0L);
            tv_user_style = intent.getStringExtra(KEY_USER_STYLE);
        }
    }

    @OnClick(R.id.tv_order_no_use_tip)
    public void toNoUseHelp() {
        startActivity(new Intent(this, WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_NO_USE_HELP));
    }

    @OnClick({R.id.bt_ok , R.id.iv_back})
    void onOkClick(Button button) {
        button.setEnabled(false);
        backToMain();
    }


    /**
     * 返回首页
     */
    private void backToMain() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage());
        }
        startActivity(new Intent(this, MainActivity.class)
                .putExtra(MainActivity.INTENT_KEY_SWITCH_TO_HOME, true));
    }


    /**
     * 复制
     */
    @OnClick(R.id.tv_copy)
    public void copy() {
        CommonUtil.copy(tvOrderNo.getText().toString(), getApplicationContext());
        onSuccess(R.string.copy_success);
    }

    @Override
    public void onBackPressed() {
        backToMain();
    }


    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
