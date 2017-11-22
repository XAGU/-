package com.xiaolian.amigo.ui.wallet;

import android.content.Intent;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.base.TimeHolder;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.order.Order;
import com.xiaolian.amigo.ui.base.WebActivity;
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

    // 预付金额
    @BindView(R.id.tv_prepay)
    TextView tv_prepay;
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

    @BindView(R.id.tv_prepay_order_tip)
    TextView tv_prepay_order_tip;

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
        tv_prepay.setText(order.getPrepay());
        tv_odd.setText(getString(R.string.wait_to_change));
    }

    @Override
    protected void setUp() {
        super.setUp();
        orderWrapper = (PrepayAdaptor.OrderWrapper) getIntent().getSerializableExtra(Constant.EXTRA_KEY);
    }

    @OnClick(R.id.tv_copy)
    public void copy() {
        CommonUtil.copy(tv_order_no.getText().toString(), getApplicationContext());
        onSuccess(R.string.copy_success);
    }

    @Override
    protected int setTitle() {
        return R.string.prepay_order;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_prepay_order;
    }

    @OnClick(R.id.tv_prepay_order_tip)
    public void onPrepayOrderTipClick() {
        startActivity(new Intent(this, WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_PREPAY_HELP));
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

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
