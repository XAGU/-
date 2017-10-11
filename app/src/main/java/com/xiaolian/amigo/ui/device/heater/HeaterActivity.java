package com.xiaolian.amigo.ui.device.heater;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.order.Order;
import com.xiaolian.amigo.ui.widget.BezierWaveView;
import com.xiaolian.amigo.ui.bonus.BonusActivity;
import com.xiaolian.amigo.ui.bonus.adaptor.BonusAdaptor;
import com.xiaolian.amigo.ui.device.DeviceBaseActivity;
import com.xiaolian.amigo.ui.device.DeviceOrderActivity;
import com.xiaolian.amigo.ui.device.intf.heator.IHeaterPresenter;
import com.xiaolian.amigo.ui.device.intf.heator.IHeaterView;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.wallet.RechargeActivity;
import com.xiaolian.amigo.ui.widget.DotFlashView;
import com.xiaolian.amigo.ui.widget.dialog.ActionSheetDialog;
import com.xiaolian.amigo.ui.widget.dialog.IOSAlertDialog;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.ble.Agreement;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 热水器设备页
 *
 * @author zcd
 */
public class HeaterActivity extends DeviceBaseActivity implements IHeaterView {
    /**
     * 跳转到选择红包页面的request code
     */
    private static final int CHOOSE_BONUS_CODE = 0x0010;

    /**
     * 确认支付
     */
    @BindView(R.id.bt_pay)
    Button bt_pay;

    /**
     * 正常页面
     */
    @BindView(R.id.ll_content_normal)
    LinearLayout ll_content_normal;

    /**
     * 未连接页面
     */
    @BindView(R.id.ll_content_unconnected)
    LinearLayout ll_content_unconnected;

    /**
     * 连接失败页面
     */
    @BindView(R.id.ll_content_connect_failed)
    LinearLayout ll_content_connect_failed;

    /**
     * 开始使用页面
     */
    @BindView(R.id.ll_content_shower)
    LinearLayout ll_content_shower;

    /**
     * et.已使用10元红包
     */
    @BindView(R.id.tv_shower_payed)
    TextView tv_shower_payed;

    /**
     * 设备连接状态
     */
    @BindView(R.id.tv_connect_status)
    TextView tv_connect_status;

    /**
     * 结束洗澡
     */
    @BindView(R.id.bt_stop_shower)
    Button bt_stop_shower;

    /**
     * 重新连接
     */
    @BindView(R.id.bt_reconnect)
    Button bt_reconnect;

    /**
     * 布局头部
     */
    @BindView(R.id.rl_header)
    RelativeLayout rl_header;

    /**
     * 余额支付
     */
    @BindView(R.id.tv_money_pay)
    TextView tv_money_pay;

    /**
     * 红包支付
     */
    @BindView(R.id.tv_bonus_pay)
    TextView tv_bonus_pay;

    /**
     * 预计用水量
     */
    @BindView(R.id.rl_pay_way)
    RelativeLayout rl_pay_way;

    /**
     * 支付方式左侧textview
     */
    @BindView(R.id.tv_water_left)
    TextView tv_water_left;

    /**
     * 支付方式右侧textview
     */
    @BindView(R.id.tv_water_right)
    TextView tv_water_right;

    /**
     * 波浪控件
     */
    @BindView(R.id.bsv_wave)
    BezierWaveView bsv_wave;

    /**
     * 加载进度控件
     */
    @BindView(R.id.dfv_dot)
    DotFlashView dfv_dot;

    /**
     * 设备名称
     */
    @BindView(R.id.tv_device_name)
    TextView tv_device_name;

    /**
     * 预计用水量 选中项
     */
    private int mItemIndex;

    /**
     * 支付方式状态
     */
    private boolean isMoneyPay = true;

    // 当前步骤，默认为第一步，1 - 确认支付页面， 2 - 结束用水页面
    private int step = 1;

    @Inject
    IHeaterPresenter<IHeaterView> presenter;

    /******************   Begin for test *******************/
    String orderId = "12345678";
    private BonusAdaptor.BonusWrapper choosedBonus;
    /******************    End for test  *******************/

    /**
     * 点击选择用水量或选择红包
     */
    @OnClick(R.id.rl_pay_way)
    void onPayWayClick() {
        if (isMoneyPay) {
            new ActionSheetDialog(this).builder()
                    .addSheetItem("预付5元／1吨水", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            mItemIndex = 0;
                            tv_water_right.setText("预付5元／1吨水");
                            tv_water_right.setTag(R.id.money_pay_amount, 5);
                        }
                    })
                    .addSheetItem("预付10元／2吨水", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            mItemIndex = 1;
                            tv_water_right.setText("预付10元／2吨水");
                            tv_water_right.setTag(R.id.money_pay_amount, 10);
                        }
                    }).setTitle("选择水量上限")
                    .setItemGravity(Gravity.LEFT)
                    .setShowCanceleButton(false)
                    .addFooter(R.layout.view_actionsheet_foot)
                    .setSelectItem(mItemIndex).show();
        } else {
            Intent intent = new Intent(this, BonusActivity.class);
            intent.putExtra(BonusActivity.INTENT_KEY_BONUS_ACTION, BonusActivity.ACTION_CHOOSE);
            startActivityForResult(intent, CHOOSE_BONUS_CODE);
        }
    }

    /**
     * 选择余额支付
     */
    @OnClick(R.id.tv_money_pay)
    void chooseMoneyPay() {
        clearPayTabStatus();
        isMoneyPay = true;
        tv_money_pay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.line_red);
        tv_money_pay.setTextColor(ContextCompat.getColor(this, R.color.black));
        tv_money_pay.setTypeface(tv_money_pay.getTypeface(), Typeface.BOLD);
        tv_water_left.setText(getString(R.string.excepted_water));
        tv_water_right.setText("预付5元／1顿水");
    }

    /**
     * 选择红包支付
     */
    @OnClick(R.id.tv_bonus_pay)
    void chooseBonusPay() {
        clearPayTabStatus();
        isMoneyPay = false;
        tv_bonus_pay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.line_red);
        tv_bonus_pay.setTextColor(ContextCompat.getColor(this, R.color.black));
        tv_bonus_pay.setTypeface(tv_bonus_pay.getTypeface(), Typeface.BOLD);
        tv_water_left.setText(getString(R.string.choose_bonus));
        tv_water_right.setText(presenter.getBonusAmount() + "个可用");
    }

    /**
     * 清除状态
     */
    void clearPayTabStatus() {
        tv_money_pay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        tv_bonus_pay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        tv_money_pay.setTextColor(ContextCompat.getColor(this, R.color.colorTextGray));
        tv_bonus_pay.setTextColor(ContextCompat.getColor(this, R.color.colorTextGray));
        tv_money_pay.setTypeface(null, Typeface.NORMAL);
        tv_bonus_pay.setTypeface(null, Typeface.NORMAL);
        bt_pay.setEnabled(true);
    }

    /**
     * 显示设备连接中
     */
    void showConnecting() {
        hideBottomLayout();
        ll_content_unconnected.setVisibility(View.VISIBLE);
        tv_connect_status.setText("正在连接设备，请稍后");
        if (dfv_dot != null && !dfv_dot.isRunning()) {
            dfv_dot.startAnimation();
        }
    }

    /**
     * 显示连接完成页面
     */
    void showConnected() {
        hideBottomLayout();
        ll_content_normal.setVisibility(View.VISIBLE);
        if (dfv_dot != null && dfv_dot.isRunning()) {
            dfv_dot.endAnimation();
        }
    }

    @Override
    public void onConnectSuccess() {
        showConnected();
        // 标记步骤为确认支付页面
        step = 1;
    }

    @Override
    public void onOpen() {
        startShower();
        // 标记步骤为结束用水页面
        step = 2;
    }

    @Override
    public void onConnectError() {
        // 连接失败时显示重连页面
        if (null != ll_content_normal && null != ll_content_shower && null != ll_content_unconnected && null != ll_content_connect_failed) {
            ll_content_normal.setVisibility(View.GONE);
            ll_content_shower.setVisibility(View.GONE);
            ll_content_unconnected.setVisibility(View.GONE);
            ll_content_connect_failed.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onReconnectSuccess() {
        hideBottomLayout();
        if (step == 1) {
            // 显示确认支付页面
            ll_content_normal.setVisibility(View.VISIBLE);
        } else {
            // 显示结束用水页面
            ll_content_shower.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏底部布局
     */
    void hideBottomLayout() {
        ll_content_normal.setVisibility(View.GONE);
        ll_content_shower.setVisibility(View.GONE);
        ll_content_unconnected.setVisibility(View.GONE);
        ll_content_connect_failed.setVisibility(View.GONE);
    }

    /**
     * 确认支付点击事件
     */
    @OnClick(R.id.bt_pay)
    void onOkButtonClick(Button button) {
        if (!isMoneyPay) {
            if (choosedBonus == null) {
                showMessage("请选择红包");
                return;
            }
            startShower();
        } else {
            if (tv_water_right.getTag(R.id.money_pay_amount) == null) {
                showMessage("请选择预计用水量");
                return;
            }
            presenter.queryWallet((Integer) tv_water_right.getTag(R.id.money_pay_amount));
        }


        // 点击支付操作时蓝牙必须为开启状态
        setBleCallback(() -> {
            button.setEnabled(false);
            presenter.onPay(1, 50, null);
            // presenter.onWrite(Agreement.getInstance().setBalance(orderId, 50));
        });
        getBlePermission();

    }

    /**
     * 结束洗澡
     */
    @OnClick(R.id.bt_stop_shower)
    void stopShower(Button button) {
        // 点击结束用水操作时蓝牙必须为开启状态
        setBleCallback(() -> {
            button.setEnabled(false);
            presenter.onClose();
        });
        getBlePermission();
    }

    /**
     * 重新连接
     */
    @OnClick(R.id.bt_reconnect)
    void reconnect(Button button) {
        // 点击重连按钮时蓝牙必须为开启状态
        setBleCallback(() -> {
            // 显示正在连接画面
            showConnecting();

            presenter.onReconnect(macAddress);
        });
        getBlePermission();
    }

    @Override
    public void onFinish() {
        // mock数据
        Order order = new Order();
        order.setDeviceNo("asdas2412as");
        order.setLocation("一号楼102室内");
        order.setDeviceNo("asdas2412as");
        order.setConsume(1.2);
        order.setPrepay(10.0);
        order.setOrderNo("77f88c5ea7886c34");
        order.setPaymentType(isMoneyPay ? 1 : 2);
        order.setWaterUsage(50);
        order.setConsume(2.1);
        order.setCreateTime(System.currentTimeMillis());
        Intent intent = new Intent(this, DeviceOrderActivity.class);
        intent.putExtra(Constant.EXTRA_KEY, order);
        startActivity(intent);
    }

    private void startShower() {
        hideBottomLayout();
        ll_content_shower.setVisibility(View.VISIBLE);

        if (isMoneyPay) {
            tv_shower_payed.setText("已预付" + (Integer) tv_water_right.getTag(R.id.money_pay_amount) + "元");
        } else {
            tv_shower_payed.setText("已使用" + tv_water_right.getText().toString());
        }
    }

    private void endShower() {
        hideBottomLayout();
        ll_content_normal.setVisibility(View.VISIBLE);
    }

    /**
     * 重新连接
     */
    @OnClick(R.id.bt_reconnect)
    void onReconnectClick() {
        dfv_dot.startAnimation();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && bsv_wave != null && !bsv_wave.isRunning()) {
            bsv_wave.startAnim();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_geyser);

        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        initView();

        presenter.onAttach(this);
        if (getIntent() != null) {
            macAddress = getIntent().getStringExtra(MainActivity.INTENT_KEY_MAC_ADDRESS);
        } else {
            macAddress = "08:7C:BE:E1:FD:3B";
        }
        // 连接蓝牙设备
        presenter.onConnect(macAddress);
    }

    private void initView() {
        // 默认选择预付5元
        tv_water_right.setTag(R.id.money_pay_amount, 5);
        tv_device_name.setText("3栋－5楼－510");
        if (bsv_wave != null && !bsv_wave.isRunning()) {
            bsv_wave.startAnim();
        }

        // 默认显示连接中状态
        showConnecting();
//        tv_water_right.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//               showConnected();
//            }
//        }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_BONUS_CODE) {
            if (resultCode == RESULT_CANCELED) {
                tv_water_right.setText(R.string.not_use_bonus);
                bt_pay.setEnabled(false);
            } else if (resultCode == RESULT_OK) {
                choosedBonus = (BonusAdaptor.BonusWrapper) data.getExtras().getSerializable(BonusActivity.INTENT_KEY_BONUS_RESULT);
                if (choosedBonus != null) {
                    tv_water_right.setText(choosedBonus.getDesc());
                    bt_pay.setEnabled(true);
                }
            }
        }
    }

    @Override
    protected void setUp() {
        super.setUp();
    }

    @Override
    protected void onDestroy() {
        presenter.onDisConnect();
        super.onDestroy();
    }

    @Override
    public void showRechargeDialog(int amount) {
        new IOSAlertDialog(this).builder()
                .setMsg("sorry,您的账户余额不足" + amount + "元~")
                .setPositiveButton("前往充值", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), RechargeActivity.class));
                    }
                })
                .setNegativeClickListener("取消", new IOSAlertDialog.OnDialogClickListener() {
                    @Override
                    public void onDialogClickListener(IOSAlertDialog iosAlertDialog) {
                        iosAlertDialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void startUse() {
        startShower();
    }
}
