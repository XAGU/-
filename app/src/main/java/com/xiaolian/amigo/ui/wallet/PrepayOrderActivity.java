package com.xiaolian.amigo.ui.wallet;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.base.TimeHolder;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.order.Order;
import com.xiaolian.amigo.ui.device.WaterDeviceBaseActivity;
import com.xiaolian.amigo.ui.device.dispenser.DispenserActivity;
import com.xiaolian.amigo.ui.device.heater.HeaterActivity;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.wallet.adaptor.PrepayAdaptor;
import com.xiaolian.amigo.ui.wallet.intf.IPrepayOrderPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IPrepayOrderView;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 待找零账单
 * <p>
 * Created by zcd on 10/12/17.
 */
public class PrepayOrderActivity extends WalletBaseActivity implements IPrepayOrderView {
    @Inject
    IPrepayOrderPresenter<IPrepayOrderView> presenter;

    @BindView(R.id.ll_order_normal)
    LinearLayout ll_order_normal;
    @BindView(R.id.rl_use_bonus)
    RelativeLayout rl_use_bonus;
    // 使用红包
    @BindView(R.id.tv_bonus_remark)
    TextView tv_bonus_remark;
    // 预付金额
    @BindView(R.id.tv_prepay)
    TextView tv_prepay;
    // 实际扣款
    @BindView(R.id.tv_consume)
    TextView tv_consume;
    // 找零金额
    @BindView(R.id.tv_odd)
    TextView tv_odd;

    // 使用时间
    @BindView(R.id.tv_time)
    TextView tv_time;
    // 设备位置
    @BindView(R.id.tv_device_location)
    TextView tv_device_location;
    // 订单号
    @BindView(R.id.tv_order_no)
    TextView tv_order_no;
    private PrepayAdaptor.OrderWrapper orderWrapper;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(PrepayOrderActivity.this);

        if (orderWrapper != null) {
            render(orderWrapper);
        }
    }

    private void render(PrepayAdaptor.OrderWrapper orderWrapper) {
        Order order = orderWrapper.getOrder();
        // 设置基础信息
        tv_time.setText(CommonUtil.stampToDate(order.getCreateTime()));
        Device device = Device.getDevice(order.getDeviceType());
        if (device != null) {
            tv_device_location.setText(device.getDesc() + " " + order.getLocation());
        } else {
            tv_device_location.setText("未知设备 " + order.getLocation());
        }
        tv_order_no.setText(order.getOrderNo());
        // 正常账单
        ll_order_normal.setVisibility(View.VISIBLE);
        // 是否有红包
        if (TextUtils.isEmpty(order.getBonus())) {
            // 没有红包
            rl_use_bonus.setVisibility(View.GONE);
        } else {
            // 有红包
            rl_use_bonus.setVisibility(View.VISIBLE);
            tv_bonus_remark.setText(order.getBonus());
        }
        tv_consume.setText(getString(R.string.wait_to_settlement));
        tv_prepay.setText(order.getPrepay());
        tv_odd.setText(getString(R.string.wait_to_change));
    }

    @Override
    protected void setUp() {
        super.setUp();
        orderWrapper = (PrepayAdaptor.OrderWrapper) getIntent().getSerializableExtra(Constant.EXTRA_KEY);
    }

    @Override
    protected int setTitle() {
        return R.string.prepay_order;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_prepay_order;
    }

    // 前往结算订单
    @OnClick(R.id.bt_ok)
    public void settleOrder() {
        String macAddress = orderWrapper.getMacAddress();
        String location = orderWrapper.getLocation();
        Intent intent = null;
        if(Device.getDevice(orderWrapper.getType()) == Device.HEATER) {
            intent = new Intent(this, HeaterActivity.class);
            intent.putExtra(MainActivity.INTENT_KEY_DEVICE_TYPE, Device.HEATER.getType());
        }else {
            intent = new Intent(this, DispenserActivity.class);
            intent.putExtra(MainActivity.INTENT_KEY_DEVICE_TYPE, Device.DISPENSER.getType());
        }
        intent.putExtra(MainActivity.INTENT_KEY_LOCATION, location);
        intent.putExtra(MainActivity.INTENT_KEY_MAC_ADDRESS, macAddress);
        intent.putExtra(MainActivity.INTENT_KEY_MAC_ADDRESS, macAddress);
        intent.putExtra(WaterDeviceBaseActivity.INTENT_HOME_PAGE_JUMP, false);
        TimeHolder.get().setLastConnectTime(System.currentTimeMillis());
        startActivity(intent);
    }
}
