package com.xiaolian.amigo.ui.wallet;

import android.content.Intent;
import android.support.v4.util.ObjectsCompat;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.base.TimeHolder;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.enumeration.DispenserCategory;
import com.xiaolian.amigo.data.enumeration.DispenserWater;
import com.xiaolian.amigo.data.network.model.order.Order;
import com.xiaolian.amigo.data.network.model.order.OrderDetailRespDTO;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.device.WaterDeviceBaseActivity;
import com.xiaolian.amigo.ui.device.dispenser.DispenserActivity;
import com.xiaolian.amigo.ui.device.dryer.DryerActivity;
import com.xiaolian.amigo.ui.device.heater.HeaterActivity;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.order.OrderConstant;
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
 *
 * @author zcd
 * @date 17/10/12
 */
public class PrepayOrderActivity extends WalletBaseActivity implements IPrepayOrderView {
    @Inject
    IPrepayOrderPresenter<IPrepayOrderView> presenter;

    /**
     * 预付金额
     */
    @BindView(R.id.tv_prepay)
    TextView tvPrepay;
    /**
     * 找零金额
     */
    @BindView(R.id.tv_odd)
    TextView tvOdd;
    /**
     * 使用时间
     */
    @BindView(R.id.tv_time)
    TextView tvTime;
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

    @BindView(R.id.tv_prepay_order_tip)
    TextView tvPrepayOrderTip;

//    private PrepayAdaptor.OrderWrapper orderWrapper;
    private OrderDetailRespDTO orderDetailRespDTO;

    private Long orderId;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(PrepayOrderActivity.this);

        if (orderId > 0) /*获取订单信息*/{
            presenter.getOrder(orderId);
        }
    }

    @Override
    public void render(OrderDetailRespDTO orderDetailRespDTO) {
        this.orderDetailRespDTO = orderDetailRespDTO;
//        Order order = orderWrapper.getOrder();
        // 设置基础信息
        tvTime.setText(CommonUtil.stampToDate(orderDetailRespDTO.getCreateTime()));
        Device device = Device.getDevice(orderDetailRespDTO.getDeviceType());
        if (device != null) {
            tvDeviceLocation.setText(String.format("%s %s", device.getDesc(), orderDetailRespDTO.getLocation()));
        } else {
            tvDeviceLocation.setText(String.format("未知设备 %s", orderDetailRespDTO.getLocation()));
        }
        tvOrderNo.setText(orderDetailRespDTO.getOrderNo());
        tvPrepay.setText(orderDetailRespDTO.getPrepay());
        tvOdd.setText(getString(R.string.wait_to_change));
    }

    @Override
    protected void setUp() {
        super.setUp();
//        orderWrapper = (PrepayAdaptor.OrderWrapper) getIntent().getSerializableExtra(Constant.EXTRA_KEY);
        orderId = getIntent().getLongExtra(OrderConstant.KEY_ORDER_ID, -1);
    }

    @OnClick(R.id.tv_copy)
    public void copy() {
        CommonUtil.copy(tvOrderNo.getText().toString(), getApplicationContext());
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

    /**
     * 前往结算订单
     */
    @OnClick(R.id.bt_ok)
    public void settleOrder() {
        String macAddress = orderDetailRespDTO.getMacAddress();
        String location = orderDetailRespDTO.getLocation();
        Intent intent = null;
        if (Device.getDevice(orderDetailRespDTO.getDeviceType()) == Device.HEATER) {
            intent = new Intent(this, HeaterActivity.class);
            intent.putExtra(MainActivity.INTENT_KEY_DEVICE_TYPE, Device.HEATER.getType());
        } else if (Device.getDevice(orderDetailRespDTO.getDeviceType()) == Device.DISPENSER) {
            intent = new Intent(this, DispenserActivity.class);
            intent.putExtra(MainActivity.INTENT_KEY_DEVICE_TYPE, Device.DISPENSER.getType());
        } else if (Device.getDevice(orderDetailRespDTO.getDeviceType()) == Device.DRYER) {
            intent = new Intent(this, DryerActivity.class);
            intent.putExtra(MainActivity.INTENT_KEY_DEVICE_TYPE, Device.DRYER.getType());
        }
        // FIXME 添加新设备需要改动此处
        if (intent == null) {
            onError(R.string.network_available_error_tip);
            return;
        }
        intent.putExtra(MainActivity.INTENT_KEY_LOCATION, location);
        intent.putExtra(MainActivity.INTENT_KEY_MAC_ADDRESS, macAddress);
        intent.putExtra(MainActivity.INTENT_KEY_SUPPLIER_ID, orderDetailRespDTO.getSupplierId());
        intent.putExtra(WaterDeviceBaseActivity.INTENT_HOME_PAGE_JUMP, false);
        intent.putExtra(DispenserActivity.INTENT_KEY_ID, orderDetailRespDTO.getResidenceId());
        intent.putExtra(MainActivity.INTENT_KEY_RESIDENCE_ID, orderDetailRespDTO.getResidenceId());
        if (orderDetailRespDTO.getCategory() != null
                && ObjectsCompat.equals(orderDetailRespDTO.getCategory(), DispenserCategory.MULTI.getType())) {
            intent.putExtra(DispenserActivity.INTENT_KEY_TEMPERATURE, DispenserWater.ALL.getType());
        } else {
            intent.putExtra(DispenserActivity.INTENT_KEY_TEMPERATURE, orderDetailRespDTO.getUsefor());
        }
        TimeHolder.get().setLastConnectTime(System.currentTimeMillis());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
