package com.xiaolian.amigo.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.ComplaintType;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.order.Order;
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
    // 设备位置
    @BindView(R.id.tv_device_location)
    TextView tv_device_location;
    // 订单号
    @BindView(R.id.tv_order_no)
    TextView tv_order_no;

    private Order order;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(this);
        render();
    }

    @Override
    protected void setUp() {
        Intent intent = getIntent();
        this.order = (Order) intent.getSerializableExtra(Constant.EXTRA_KEY);
        this.token = intent.getStringExtra(Constant.TOKEN);
    }

    /**
     * 投诉
     */
    @OnClick(R.id.tv_complaint)
    public void complaint() {
        presenter.checkComplaint(order.getId(), ComplaintType.getComplaintTypeByDeviceType(
                Device.getDevice(order.getDeviceType())).getType());
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
    public void render() {
        // 设置基础信息
        tv_time.setText(CommonUtil.stampToDate(order.getCreateTime()));
        Device device = Device.getDevice(order.getDeviceType());
        if (device != null) {
            tv_device_location.setText(device.getDesc() + " " + order.getLocation());
        } else {
            tv_device_location.setText("未知设备 " + order.getLocation());
        }
        tv_order_no.setText(order.getOrderNo());
        if (CommonUtil.equals(order.getStatus(), 3)) {
            tv_order_title.setText(getString(R.string.error_order_title));
            // 异常账单
            tv_order_error_tip.setVisibility(View.VISIBLE);
            ll_order_normal.setVisibility(View.GONE);
            ll_order_error.setVisibility(View.VISIBLE);
            // 是否有代金券
            if (TextUtils.isEmpty(order.getBonus())) {
                // 没有代金券
                rl_back_bonus.setVisibility(View.GONE);
            } else {
                // 有代金券
                rl_back_bonus.setVisibility(View.VISIBLE);
                tv_back_bonus.setText(order.getBonus());
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
            } else {
                // 有代金券
                rl_use_bonus.setVisibility(View.VISIBLE);
                tv_bonus_remark.setText(getString(R.string.minus, order.getBonus()));
            }
            tv_consume.setText(order.getConsume());
            tv_prepay.setText(order.getPrepay());
            tv_odd.setText(order.getOdd());
            tv_actual_debit.setText(getString(R.string.minus, order.getActualDebit()));
        }
    }

    @Override
    public void toComplaint() {
        startActivity(new Intent(this, WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_COMPLAINT
                        + "?token=" + token
                        + "&orderId=" + order.getId()
                        + "&orderNo=" + order.getOrderNo()
                        + "&orderType="
                        + ComplaintType.getComplaintTypeByDeviceType(
                        Device.getDevice(order.getDeviceType())).getType()));
    }

}
