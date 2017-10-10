package com.xiaolian.amigo.ui.device;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Payment;
import com.xiaolian.amigo.data.network.model.order.Order;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>
 * Created by zcd on 10/9/17.
 */

public class DeviceOrderActivity extends AppCompatActivity {

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

    Order order;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_order);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        this.order = (Order) intent.getSerializableExtra(Constant.EXTRA_KEY);

        if (order.getPaymentType() == 1) {
            // 余额支付
            iv_order_free.setVisibility(View.GONE);
            tv_amount.setText(String.valueOf(order.getConsume()));
            tv_change_amount.setText(String.valueOf(order.getPrepay() - order.getConsume()));
        } else {
            // 红包支付
            iv_order_free.setVisibility(View.VISIBLE);
            tv_amount.setText("0");
            tv_change_amount.setText("0");
        }
        tv_time.setText(CommonUtil.stampToDate(order.getCreateTime()));
        tv_device_info.setText(order.getLocation());
        tv_order_no.setText(order.getOrderNo());
        tv_pay_method.setText(Payment.getPayment(order.getPaymentType()).getDesc());
    }

    @OnClick(R.id.bt_ok)
    void onOkClick() {
        backToMain();
    }

    /**
     * 返回首页
     */
    private void backToMain() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        backToMain();
    }
}
