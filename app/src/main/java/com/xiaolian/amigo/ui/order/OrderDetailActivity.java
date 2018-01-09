package com.xiaolian.amigo.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.ComplaintType;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.order.Order;
import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.order.intf.IOrderDetailPresenter;
import com.xiaolian.amigo.ui.order.intf.IOrderDetailView;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消费账单详情
 * <p>
 * Created by caidong on 2017/9/18.
 */
public class OrderDetailActivity extends OrderBaseActivity implements IOrderDetailView {
    @Inject
    IOrderDetailPresenter<IOrderDetailView> presenter;

    // 账单标题
    @BindView(R.id.tv_order_title)
    TextView tv_order_title;
    /************** 正常账单内容 ****************/
    @BindView(R.id.ll_order_normal)
    LinearLayout ll_order_normal;
    @BindView(R.id.rl_use_bonus)
    RelativeLayout rl_use_bonus;
    // 代金券抵扣
    @BindView(R.id.tv_bonus_remark)
    TextView tv_bonus_remark;
    // 预付金额
    @BindView(R.id.tv_prepay)
    TextView tv_prepay;
    // 实际扣款
    @BindView(R.id.tv_actual_debit)
    TextView tv_actual_debit;
    @BindView(R.id.rl_actual_debit)
    RelativeLayout rl_actual_debit;
    // 实际消费
    @BindView(R.id.tv_consume)
    TextView tv_consume;
    // 找零金额
    @BindView(R.id.tv_odd)
    TextView tv_odd;


    /************** 异常账单内容 ****************/
    @BindView(R.id.tv_order_error_tip)
    TextView tv_order_error_tip;
    @BindView(R.id.ll_order_error)
    LinearLayout ll_order_error;
    @BindView(R.id.rl_back_bonus)
    RelativeLayout rl_back_bonus;
    // 退还代金券
    @BindView(R.id.tv_back_bonus)
    TextView tv_back_bonus;
    // 退还金额
    @BindView(R.id.tv_back_amount)
    TextView tv_back_amount;

    /************** 基础信息 *****************/
    // 使用时间
    @BindView(R.id.tv_time)
    TextView tv_time;
    // 使用时长
    @BindView(R.id.tv_used_time)
    TextView tv_used_time;
    @BindView(R.id.rl_used_time)
    RelativeLayout rl_used_time;
    // 设备位置
    @BindView(R.id.tv_device_location)
    TextView tv_device_location;
    // 订单号
    @BindView(R.id.tv_order_no)
    TextView tv_order_no;
    // 是否最低消费
    @BindView(R.id.tv_order_no_use_tip)
    TextView tv_order_no_use_tip;

    private Long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(this);
        presenter.getOrder(orderId);
    }

    @Override
    protected void setUp() {
        Intent intent = getIntent();
        this.orderId = intent.getLongExtra(Constant.EXTRA_KEY, -1);
    }

    /**
     * 投诉
     */
    @OnClick(R.id.tv_complaint)
    public void complaint() {
        presenter.checkComplaint();
    }

    /**
     * 复制
     */
    @OnClick(R.id.tv_copy)
    public void copy() {
        CommonUtil.copy(tv_order_no.getText().toString(), getApplicationContext());
        onSuccess(R.string.copy_success);
    }

    @Override
    public void showNoUseTip() {
        tv_order_no_use_tip.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_order_no_use_tip)
    public void toNoUseHelp() {
        startActivity(new Intent(this, WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_NO_USE_HELP));
    }

    @Override
    public void toComplaint() {
        String token = presenter.getToken();
        OrderDetailRespDTO order = presenter.getOrder();
        startActivity(new Intent(this, WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_COMPLAINT
                        + "?token=" + token
                        + "&orderId=" + order.getId()
                        + "&orderNo=" + order.getOrderNo()
                        + "&orderType="
                        + ComplaintType.getComplaintTypeByDeviceType(
                        Device.getDevice(order.getDeviceType())).getType()));
    }

    @Override
    public void renderView(OrderDetailRespDTO order) {
        // 设置基础信息
        tv_time.setText(CommonUtil.stampToDate(order.getCreateTime()));
        tv_device_location.setText(String.format("%s %s",
                Device.getDevice(order.getDeviceType()).getDesc(), order.getLocation()));
        if (Device.getDevice(order.getDeviceType()) == Device.DRYER) {
            rl_used_time.setVisibility(View.VISIBLE);
            tv_used_time.setText(order.getUseTime());
        }
        tv_order_no.setText(order.getOrderNo());
        if (CommonUtil.equals(order.getStatus(), 3)) {
//            tv_order_title.setText(getString(R.string.error_order_title));
            // 异常账单
            tv_order_error_tip.setVisibility(View.VISIBLE);
            ll_order_normal.setVisibility(View.GONE);
            ll_order_error.setVisibility(View.VISIBLE);
            // 是否有代金券
            if (TextUtils.isEmpty(order.getBonus())) {
                // 没有代金券
                rl_back_bonus.setVisibility(View.GONE);
                tv_consume.setTextColor(ContextCompat.getColor(this, R.color.colorFullRed));
                tv_actual_debit.setTextColor(ContextCompat.getColor(this, R.color.colorDark6));
                rl_actual_debit.setVisibility(View.GONE);
            } else {
                // 有代金券
                rl_back_bonus.setVisibility(View.VISIBLE);
                tv_back_bonus.setText(order.getBonus());
                tv_consume.setTextColor(ContextCompat.getColor(this, R.color.colorDark6));
                tv_actual_debit.setTextColor(ContextCompat.getColor(this, R.color.colorFullRed));
            }
            tv_back_amount.setText(order.getPrepay());
        } else {
            // 正常账单
            tv_order_error_tip.setVisibility(View.GONE);
            ll_order_normal.setVisibility(View.VISIBLE);
            ll_order_error.setVisibility(View.GONE);
            // 是否有代金券
            if (TextUtils.isEmpty(order.getBonus())) {
                // 没有代金券
                rl_use_bonus.setVisibility(View.GONE);
                tv_consume.setTextColor(ContextCompat.getColor(this, R.color.colorFullRed));
                tv_actual_debit.setTextColor(ContextCompat.getColor(this, R.color.colorDark6));
                rl_actual_debit.setVisibility(View.GONE);
            } else {
                // 有代金券
                rl_use_bonus.setVisibility(View.VISIBLE);
                tv_bonus_remark.setText(getString(R.string.minus, order.getBonus()));
                tv_consume.setTextColor(ContextCompat.getColor(this, R.color.colorDark6));
                tv_actual_debit.setTextColor(ContextCompat.getColor(this, R.color.colorFullRed));
            }
            tv_consume.setText(order.getConsume());
            tv_prepay.setText(order.getPrepay());
            tv_odd.setText(order.getOdd());
            tv_actual_debit.setText(getString(R.string.minus, order.getActualDebit()));
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
