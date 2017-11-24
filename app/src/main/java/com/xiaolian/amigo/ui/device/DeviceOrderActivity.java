package com.xiaolian.amigo.ui.device;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import com.xiaolian.amigo.data.network.model.dto.response.OrderDetailRespDTO;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.device.intf.IDeviceOrderPresenter;
import com.xiaolian.amigo.ui.device.intf.IDeviceOrderView;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>
 * Created by zcd on 10/9/17.
 */

public class DeviceOrderActivity extends DeviceBaseActivity implements IDeviceOrderView {

    private static final String TAG = DeviceOrderActivity.class.getSimpleName();
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

        Intent intent = getIntent();
        orderId = intent.getLongExtra(Constant.BUNDLE_ID, 0L);
        if (orderId != 0L) {
            presenter.onLoad(orderId);
        }
    }

    @Override
    public void setRefreshComplete(OrderDetailRespDTO respDTO) {
        // 设置基础信息
        orderType = ComplaintType.getComplaintTypeByDeviceType(
                Device.getDevice(respDTO.getDeviceType())).getType();
        tv_time.setText(CommonUtil.stampToDate(respDTO.getCreateTime()));
        Device device = Device.getDevice(respDTO.getDeviceType());
        if (device != null) {
            tv_device_location.setText(String.format("%s %s", device.getDesc(), respDTO.getLocation()));
        } else {
            tv_device_location.setText(String.format("未知设备 %s", respDTO.getLocation()));
        }
        orderNo = respDTO.getOrderNo();
        tv_order_no.setText(orderNo);
        if (CommonUtil.equals(respDTO.getStatus(), 3)) {
            // 异常账单
//            tv_order_title.setText(getString(R.string.error_order_title));
            tv_order_error_tip.setVisibility(View.VISIBLE);
            ll_order_normal.setVisibility(View.GONE);
            ll_order_error.setVisibility(View.VISIBLE);
            // 是否有代金券
            if (TextUtils.isEmpty(respDTO.getBonus())) {
                // 没有代金券
                rl_back_bonus.setVisibility(View.GONE);
                tv_consume.setTextColor(ContextCompat.getColor(this, R.color.colorFullRed));
                tv_actual_debit.setTextColor(ContextCompat.getColor(this, R.color.colorDark6));
                tv_actual_debit.setVisibility(View.GONE);
            } else {
                // 有代金券
                rl_back_bonus.setVisibility(View.VISIBLE);
                tv_back_bonus.setText(respDTO.getBonus());
                tv_consume.setTextColor(ContextCompat.getColor(this, R.color.colorDark6));
                tv_actual_debit.setTextColor(ContextCompat.getColor(this, R.color.colorFullRed));
            }
            tv_back_amount.setText(respDTO.getPrepay());
        } else {
            // 正常账单
            tv_order_error_tip.setVisibility(View.GONE);
            ll_order_normal.setVisibility(View.VISIBLE);
            ll_order_error.setVisibility(View.GONE);
            // 是否有代金券
            if (TextUtils.isEmpty(respDTO.getBonus())) {
                // 没有代金券
                rl_use_bonus.setVisibility(View.GONE);
                tv_consume.setTextColor(ContextCompat.getColor(this, R.color.colorFullRed));
                tv_actual_debit.setTextColor(ContextCompat.getColor(this, R.color.colorDark6));
                tv_actual_debit.setVisibility(View.GONE);
            } else {
                // 有代金券
                rl_use_bonus.setVisibility(View.VISIBLE);
                tv_bonus_remark.setText(getString(R.string.minus, respDTO.getBonus()));
                tv_consume.setTextColor(ContextCompat.getColor(this, R.color.colorDark6));
                tv_actual_debit.setTextColor(ContextCompat.getColor(this, R.color.colorFullRed));
            }
            tv_consume.setText(respDTO.getConsume());
            tv_prepay.setText(respDTO.getPrepay());
            tv_odd.setText(respDTO.getOdd());
            tv_actual_debit.setText(getString(R.string.minus, respDTO.getActualDebit()));
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
        CommonUtil.copy(tv_order_no.getText().toString(), getApplicationContext());
        onSuccess(R.string.copy_success);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        backToMain();
    }

    @Override
    public void onError(TradeError tradeError) {

    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
