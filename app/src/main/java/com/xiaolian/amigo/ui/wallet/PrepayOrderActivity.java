package com.xiaolian.amigo.ui.wallet;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.base.TimeHolder;
import com.xiaolian.amigo.data.enumeration.Device;
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

    /**
     * 使用时间
     */
    @BindView(R.id.tv_time)
    TextView tv_time;

    /**
     * 设备位置
     */
    @BindView(R.id.tv_location)
    TextView tv_location;

    /**
     * 订单号
     */
    @BindView(R.id.tv_order_no)
    TextView tv_order_no;

    /**
     * 支付方式
     */
    @BindView(R.id.tv_pay_method)
    TextView tv_pay_method;

    /**
     * 找零金额
     */
    @BindView(R.id.rl_odd)
    RelativeLayout rl_odd;

    private PrepayAdaptor.OrderWrapper orderWrapper;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(PrepayOrderActivity.this);

        if (orderWrapper != null) {
            tv_time.setText(CommonUtil.stampToDate(orderWrapper.getTime()));
            Device device = Device.getDevice(orderWrapper.getType());
            if (device != null) {
                tv_location.setText(device.getDesc() + " " + orderWrapper.getOrder().getLocation());
            } else {
                tv_location.setText("未知设备 " + orderWrapper.getOrder().getLocation());
            }
            tv_order_no.setText(orderWrapper.getOrder().getOrderNo());
            tv_pay_method.setText(orderWrapper.getOrder().getPrepay());
            rl_odd.setVisibility(orderWrapper.getOrder().getPaymentType() == 1 ?
                    View.VISIBLE : View.GONE);
        }
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
        if(Device.getDevice(orderWrapper.getType()) == Device.HEARTER) {
            intent = new Intent(this, HeaterActivity.class);
            intent.putExtra(MainActivity.INTENT_KEY_DEVICE_TYPE, Device.HEARTER.getType());
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
