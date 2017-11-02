package com.xiaolian.amigo.ui.device;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.ComplaintType;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.enumeration.Payment;
import com.xiaolian.amigo.data.enumeration.TradeError;
import com.xiaolian.amigo.data.network.model.dto.response.OrderDetailRespDTO;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.device.intf.IDeviceOrderPresenter;
import com.xiaolian.amigo.ui.device.intf.IDeviceOrderView;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>
 * Created by zcd on 10/9/17.
 */

public class DeviceOrderActivity extends DeviceBaseActivity implements IDeviceOrderView {

    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_device_info)
    TextView tv_device_info;
    @BindView(R.id.tv_order_no)
    TextView tv_order_no;
    @BindView(R.id.tv_pay_method)
    TextView tv_pay_method;
    @BindView(R.id.tv_change_amount)
    TextView tv_change_amount;
    @BindView(R.id.tv_amount)
    TextView tv_amount;
    @BindView(R.id.iv_order_free)
    ImageView iv_order_free;
    @BindView(R.id.rl_odd)
    RelativeLayout rl_odd;
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
        orderNo = respDTO.getOrderNo();
        orderType = ComplaintType.getComplaintTypeByDeviceType(
                Device.getDevice(respDTO.getDeviceType())).getType();
        if (respDTO.getPaymentType() == Payment.BALANCE.getType()) { // 余额支付
            iv_order_free.setVisibility(View.GONE);
        } else { // 红包支付
            rl_odd.setVisibility(View.GONE);
            iv_order_free.setVisibility(View.VISIBLE);
        }
        tv_amount.setText(respDTO.getConsume());
        tv_change_amount.setText(respDTO.getOdd());
        tv_pay_method.setText(respDTO.getPrepay());
        tv_time.setText(CommonUtil.stampToDate(respDTO.getCreateTime()));
        Device device = Device.getDevice(respDTO.getDeviceType());
        if (device != null) {
            tv_device_info.setText(device.getDesc() + " " + respDTO.getLocation());
        } else {
            tv_device_info.setText("未知设备 " + respDTO.getLocation());
        }
        tv_order_no.setText(respDTO.getOrderNo());
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
            e.printStackTrace();
        }
        startActivity(new Intent(this, MainActivity.class)
                .putExtra(MainActivity.INTENT_KEY_SWITCH_TO_HOME, true));
    }

    /**
     * 投诉
     */
    @OnClick(R.id.tv_complaint)
    public void complaint() {
        startActivity(new Intent(this, WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL,
                        Constant.H5_COMPLAINT
                        + "?token=" + presenter.getToken()
                        + "&orderId=" + orderId
                        + "&orderNo=" + orderNo
                        + "&orderType=" + orderType));
    }

    /**
     * 复制
     */
    @OnClick(R.id.tv_copy)
    public void copy() {
        CommonUtil.copy(tv_order_no.getText().toString(), getApplicationContext());
        onSuccess("复制成功");
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        backToMain();
    }

    @Override
    public void onError(TradeError tradeError) {

    }
}
