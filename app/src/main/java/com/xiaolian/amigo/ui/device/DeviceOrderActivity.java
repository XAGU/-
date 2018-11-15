package com.xiaolian.amigo.ui.device;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ObjectsCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.ComplaintType;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.enumeration.TradeError;
import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.device.intf.IDeviceOrderPresenter;
import com.xiaolian.amigo.ui.device.intf.IDeviceOrderView;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.order.OrderDetailActivity;
import com.xiaolian.amigo.util.AppUtils;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设备账单
 *
 * @author zcd
 * @date 17/10/9
 */

public class DeviceOrderActivity extends DeviceBaseActivity implements IDeviceOrderView {

    private static final String TAG = DeviceOrderActivity.class.getSimpleName();

    public static final String KEY_USER_STYLE = "KEY_USER_STYLE"; // 使用方式
    private static final int ERROR_ORDER_STATUS = 3;
    /**
     * 账单标题
     */
    @BindView(R.id.tv_order_title)
    TextView tvOrderTitle;
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


    @BindView(R.id.tv_time_end)
    TextView tvTimeEnd ;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_order);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(this);
        setUp();
        if (orderId != 0L) {
            presenter.onLoad(orderId);
        }
    }

    @Override
    protected void setUp() {
        if (getIntent() != null) {
            Intent intent = getIntent();
            orderId = intent.getLongExtra(Constant.BUNDLE_ID, 0L);
            tv_user_style = intent.getStringExtra(KEY_USER_STYLE);
        }
    }

    @Override
    public void setRefreshComplete(OrderDetailRespDTO respDTO) {
        // 设置基础信息
        orderType = ComplaintType.getComplaintTypeByDeviceType(
                Device.getDevice(respDTO.getDeviceType())).getType();
        tvTime.setText(CommonUtil.stampToDate(respDTO.getCreateTime()));
        tvTimeEnd.setText(CommonUtil.stampToDate(respDTO.getFinishTime()));
        if (Device.getDevice(respDTO.getDeviceType()) == Device.DRYER) {
            rlUsedTime.setVisibility(View.VISIBLE);
            tvUsedTime.setText(respDTO.getUseTime());
        }

        if (!TextUtils.isEmpty(tv_user_style)) {
            rlUserStyle.setVisibility(View.VISIBLE);
            tvMethod.setText(tv_user_style);
        }else{
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
        startActivity(new Intent(this, WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL,
                        Constant.H5_COMPLAINT
                                + "?token=" + presenter.getToken()
                                + "&orderId=" + orderId
                                + "&orderNo=" + orderNo
                                + "&orderType=" + orderType));
    }

    @Override
    public void showNoUseTip() {
        tvOrderNoUseTip.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_order_no_use_tip)
    public void toNoUseHelp() {
        startActivity(new Intent(this, WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_NO_USE_HELP));
    }

    @OnClick(R.id.bt_ok)
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
     * 投诉
     */
    @OnClick(R.id.tv_complaint)
    public void complaint() {
        presenter.checkComplaint(orderId, orderType);
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
    public void onError(TradeError tradeError) {

    }

    @Override
    public String getAppVersion() {
        return AppUtils.getVersionName(this);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
