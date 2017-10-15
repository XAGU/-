package com.xiaolian.amigo.ui.device;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.ErrorTag;
import com.xiaolian.amigo.data.enumeration.Payment;
import com.xiaolian.amigo.data.enumeration.TradeError;
import com.xiaolian.amigo.data.enumeration.TradeStep;
import com.xiaolian.amigo.data.network.model.dto.response.OrderPreInfoDTO;
import com.xiaolian.amigo.data.network.model.dto.response.PrepayOptionDTO;
import com.xiaolian.amigo.data.network.model.dto.response.UnsettledOrderStatusCheckRespDTO;
import com.xiaolian.amigo.ui.bonus.BonusActivity;
import com.xiaolian.amigo.ui.bonus.adaptor.BonusAdaptor;
import com.xiaolian.amigo.ui.device.intf.IWaterDeviceBasePresenter;
import com.xiaolian.amigo.ui.device.intf.IWaterDeviceBaseView;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.repair.RepairApplyActivity;
import com.xiaolian.amigo.ui.user.ChooseDormitoryActivity;
import com.xiaolian.amigo.ui.wallet.RechargeActivity;
import com.xiaolian.amigo.ui.widget.BezierWaveView;
import com.xiaolian.amigo.ui.widget.DotFlashView;
import com.xiaolian.amigo.ui.widget.dialog.ActionSheetDialog;
import com.xiaolian.amigo.ui.widget.dialog.IOSAlertDialog;
import com.xiaolian.amigo.util.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * <p>
 * Created by zcd on 10/13/17.
 */

public abstract class WaterDeviceBaseActivity<P extends IWaterDeviceBasePresenter> extends DeviceBaseActivity implements IWaterDeviceBaseView {
    private static final String TAG = WaterDeviceBaseActivity.class.getSimpleName();
    /**
     * 跳转到选择红包页面的request code
     */
    private static final int CHOOSE_BONUS_CODE = 0x0010;
    private static final int CHOOSE_DORMITORY_CODE = 0x0011;

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
    @BindView(R.id.ll_error)
    LinearLayout ll_error;

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
     * 小提示（上）
     */
    @BindView(R.id.trade_above_tip)
    TextView trade_above_tip;

    /**
     * 小提示（下）
     */
    @BindView(R.id.trade_below_tip)
    TextView trade_below_tip;

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
//    @BindView(R.id.bt_reconnect)
//    Button bt_reconnect;

    /**
     * 布局头部
     */
    @BindView(R.id.rl_header)
    RelativeLayout rl_header;

    /**
     * 右上角按钮
     */
    @BindView(R.id.iv_top_right_icon)
    ImageView iv_top_right_icon;

    /**
     * 子标题
     */
    @BindView(R.id.tv_sub_title)
    TextView tv_sub_title;

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
     * 设备标题
     */
    @BindView(R.id.tv_device_title)
    TextView tv_device_title;

    /**
     * 显示加载动画
     */
    @BindView(R.id.v_loading)
    DotFlashView v_loading;

    /**
     * 错误标题
     */
    @BindView(R.id.tv_error_title)
    TextView tv_error_title;

    /**
     * 错误提示
     */
    @BindView(R.id.tv_error_tip)
    TextView tv_error_tip;

    /**
     * 错误处理器
     */
    @BindView(R.id.bt_error_handler)
    Button bt_error_handler;

    /**
     * 头部icon
     */
    @BindView(R.id.iv_header_icon)
    ImageView iv_header_icon;

    /**
     * 预计用水量 选中项
     */
    private int mItemIndex;

    /**
     * 支付方式状态
     */
    private boolean isMoneyPay = true;

    P presenter;

    private BonusAdaptor.BonusWrapper choosedBonus;
    private OrderPreInfoDTO orderPreInfo;

    // 设备类型
    private int deviceType;
    // 设备位置
    private String deviceLocation;
    // 设备位置id
    private Long residenceId;
    private String prepayDefaultDesc = "预付5元";
    private Double prepayDefaultAmount = 5.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_device);
        setUnBinder(ButterKnife.bind(this));
        initInject();
        initPresenter();
        initView();

        if (getIntent() != null) {
            macAddress = getIntent().getStringExtra(MainActivity.INTENT_KEY_MAC_ADDRESS);
            tv_device_title.setText(getIntent().getStringExtra(MainActivity.INTENT_KEY_LOCATION));
            deviceType = getIntent().getIntExtra(MainActivity.INTENT_KEY_DEVICE_TYPE, 1);
            residenceId = getIntent().getLongExtra(MainActivity.INTENT_KEY_RESIDENCE_ID, 0L);
        }
        if (TextUtils.isEmpty(macAddress)) {
            macAddress = "08:7C:BE:E1:FD:3B";
        }

        // 连接蓝牙设备
        presenter.onConnect(macAddress);
        presenter.queryPrepayOption(deviceType);
    }

    protected abstract void initInject();

    protected abstract
    @DrawableRes
    int setHeaderBackgroundDrawable();

    private void initPresenter() {
        presenter = setPresenter();
    }

    private void initView() {
        setHeaderBackground(setHeaderBackgroundDrawable());
        setHeaderIcon(setHeaderIconDrawable());
        setTopRightIcon(setTopRightIconDrawable());
        // 默认选择预付5元
        tv_water_right.setTag(R.id.money_pay_amount, prepayDefaultAmount);
        tv_water_right.setText(prepayDefaultDesc);
        if (bsv_wave != null && !bsv_wave.isRunning()) {
            bsv_wave.startAnim();
        }
        // 默认显示连接中状态
        showConnecting();
    }

    // 子类可重写
    protected void setTopRightIcon(int drawableRes) {
        iv_top_right_icon.setImageResource(drawableRes);
    }

    protected abstract int setTopRightIconDrawable();

    private void setHeaderIcon(int drawableRes) {
        iv_header_icon.setImageResource(drawableRes);
    }

    protected abstract
    @DrawableRes
    int setHeaderIconDrawable();


    // 设置头部背景
    public void setHeaderBackground(int headBackground) {
        rl_header.setBackgroundResource(headBackground);
    }

    @Override
    public void setPrepayOption(OrderPreInfoDTO data) {
        orderPreInfo = data;
        try {
            prepayDefaultDesc = data.getOptions().get(0).getItems().get(0).getDescription();
            prepayDefaultAmount = data.getOptions().get(0).getItems().get(0).getPrepay();
            tv_water_right.setTag(R.id.money_pay_amount, prepayDefaultAmount);
            tv_water_right.setText(prepayDefaultDesc);
        } catch (Exception e) {
            Log.w(TAG, "服务器返回的prepayOption为空");
        }
        // TODO 子类重写
    }

    /**
     * 点击选择用水量或选择红包
     */
    @OnClick(R.id.rl_pay_way)
    void onPayWayClick() {
        if (isMoneyPay) {
            ActionSheetDialog actionSheetDialog = new ActionSheetDialog(this).builder();
            if (orderPreInfo.getOptions() != null && !orderPreInfo.getOptions().isEmpty()) {
                PrepayOptionDTO option = orderPreInfo.getOptions().get(0);
                for (int i = 0; i < option.getItems().size(); i ++) {
                    String desc = option.getItems().get(i).getDescription();
                    Double prepay = option.getItems().get(i).getPrepay();
                    int position = i;
                    actionSheetDialog.addSheetItem(desc, ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    mItemIndex = position;
                                    tv_water_right.setText(desc);
                                    tv_water_right.setTag(R.id.money_pay_amount, prepay);
                                }
                            });
                }
            }
        actionSheetDialog.setTitle("选择水量上限")
                    .setItemGravity(Gravity.LEFT)
                    .setShowCanceleButton(false)
                    .addFooter(R.layout.view_actionsheet_foot)
                    .setSelectItem(mItemIndex).show();
        } else {
            Intent intent = new Intent(this, BonusActivity.class);
            intent.putExtra(BonusActivity.INTENT_KEY_BONUS_ACTION, BonusActivity.ACTION_CHOOSE);
            intent.putExtra(BonusActivity.INTENT_KEY_BONUS_DEVICE_TYPE, deviceType);
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
        tv_water_right.setText(prepayDefaultDesc);
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
        int bonusAmount = orderPreInfo.getBonus();
        tv_water_right.setText(bonusAmount == 0 ? "没有可用红包" : bonusAmount + "个可用");
        if (bonusAmount == 0) {
            bt_pay.setEnabled(false);
        }
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
     * 显示支付页面
     */
    void showStep1() {
        hideBottomLayout();
        ll_content_normal.setVisibility(View.VISIBLE);
        if (dfv_dot != null && dfv_dot.isRunning()) {
            dfv_dot.endAnimation();
        }
    }

    /**
     * 直接跳转至结算页面
     */
    void showStep2(UnsettledOrderStatusCheckRespDTO orderStatus) {
        startShower(orderStatus);
    }

    private void toggleSubTitle(boolean visible) {
        if (tv_sub_title != null) {
            tv_sub_title.setVisibility(visible ?
                    View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onConnectSuccess(TradeStep step, Object... extra) {
        if (TradeStep.PAY == step) {
            showStep1();
            // 标记步骤为确认支付页面
            presenter.setStep(TradeStep.PAY);
            toggleSubTitle(true);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Log.wtf(TAG, e);
            }
        } else { // TradeStep.SETTLE
            showStep2((UnsettledOrderStatusCheckRespDTO) extra[0]);
            // 标记步骤为结算找零页面
            presenter.setStep(TradeStep.SETTLE);
            toggleSubTitle(false);
        }

    }

    @Override
    public void onOpen() {
        startShower(null);
        // 标记步骤为结束用水页面
        presenter.setStep(TradeStep.SETTLE);
        toggleSubTitle(true);
    }

    @Override
    public void
    onConnectError() {
        // 连接失败时显示重连页面
        if (null != ll_content_normal && null != ll_content_shower && null != ll_content_unconnected && null != ll_error) {
            ll_content_normal.setVisibility(View.GONE);
            ll_content_shower.setVisibility(View.GONE);
            ll_content_unconnected.setVisibility(View.GONE);
            ll_error.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onReconnectSuccess() {
        hideBottomLayout();
        if (presenter.getStep() == TradeStep.PAY) {
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
        ll_error.setVisibility(View.GONE);
    }

    /**
     * 确认支付
     */
    @OnClick(R.id.bt_pay)
    void pay(Button button) {
        if (!isMoneyPay) {
            if (choosedBonus == null) {
                showMessage("请选择红包");
                return;
            }
            // 点击支付操作时蓝牙必须为开启状态
            setBleCallback(() -> {
                button.setEnabled(false);
                presenter.onPay(Payment.BONUS.getType(), null, choosedBonus.getId());
            });
            getBlePermission();
        } else {
            if (tv_water_right.getTag(R.id.money_pay_amount) == null) {
                showMessage("请选择预计用水量");
                return;
            }
            presenter.queryWallet((Double) tv_water_right.getTag(R.id.money_pay_amount));
        }
    }

    /**
     * 准备结算
     */
    @OnClick(R.id.bt_stop_shower)
    void settle(Button button) {
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
    @OnClick(R.id.bt_error_handler)
    void handleError(Button button) {
        switch (ErrorTag.getErrorTag((int) (button.getTag()))) {
            case CONNECT_ERROR:
                // 点击重连按钮时蓝牙必须为开启状态
                setBleCallback(() -> {
                    // 显示正在连接画面
                    showConnecting();

                    presenter.onReconnect(macAddress);
                });
                getBlePermission();
                break;
            case DEVICE_BUSY:
                startActivityForResult(new Intent(this, ChooseDormitoryActivity.class), CHOOSE_DORMITORY_CODE);
                break;
            case REPAIR:
                Intent intent = new Intent(this, RepairApplyActivity.class);
                intent.putExtra(Constant.DEVICE_TYPE, deviceType);
                intent.putExtra(Constant.LOCATION_ID, residenceId);
                intent.putExtra(Constant.LOCATION, deviceLocation);
                startActivity(intent);
                finish();
                break;
            case CALL:
                // TODO 联系客服
                break;
        }
    }

    @Override
    public void onFinish(long orderId) {
        presenter.onDisConnect();
        Intent intent = new Intent(this, DeviceOrderActivity.class);
        intent.putExtra(Constant.BUNDLE_ID, orderId);
        startActivity(intent);
        finish();
    }

    private void startShower(UnsettledOrderStatusCheckRespDTO orderStatus) {
        hideBottomLayout();
        ll_content_shower.setVisibility(View.VISIBLE);

        if (null != orderStatus) {
            if (Payment.getPayment(orderStatus.getPaymentType()) == Payment.BALANCE) {
                tv_shower_payed.setText(orderStatus.getExtra());
                trade_above_tip.setText(getString(R.string.balance_pay_tip_above));
                trade_below_tip.setText(getString(R.string.balance_pay_tip_below));
                bt_stop_shower.setText("结算找零");
            } else {
                tv_shower_payed.setText(orderStatus.getExtra());
                trade_above_tip.setText(getString(R.string.bonus_pay_tip_above));
                trade_below_tip.setText(getString(R.string.bonus_pay_tip_below));
                bt_stop_shower.setText("结束用水");
            }
        } else {
            if (isMoneyPay) {
                tv_shower_payed.setText("已预付" + (Double) tv_water_right.getTag(R.id.money_pay_amount) + "元");
                trade_above_tip.setText(getString(R.string.balance_pay_tip_above));
                trade_below_tip.setText(getString(R.string.balance_pay_tip_below));
                bt_stop_shower.setText("结算找零");
            } else {
                tv_shower_payed.setText("已使用" + tv_water_right.getText().toString());
                trade_above_tip.setText(getString(R.string.bonus_pay_tip_above));
                trade_below_tip.setText(getString(R.string.bonus_pay_tip_below));
                bt_stop_shower.setText("结束用水");
            }
        }

    }

    private void endShower() {
        hideBottomLayout();
        ll_content_normal.setVisibility(View.VISIBLE);
    }

    /**
     * 重新连接
     */
//    @OnClick(R.id.bt_reconnect)
//    void onReconnectClick() {
//        dfv_dot.startAnimation();
//    }

    /**
     * 右上角图标点击事件
     */
    @OnClick(R.id.iv_top_right_icon)
    void onTopRightIconClick() {
        // 子类重写实现功能
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && bsv_wave != null && !bsv_wave.isRunning()) {
            bsv_wave.startAnim();
        }
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
                    tv_water_right.setText(choosedBonus.getAmount() + "元" + choosedBonus.getName());
                    bt_pay.setEnabled(true);
                }
            }
        } else if (requestCode == CHOOSE_DORMITORY_CODE) {
            if (resultCode == RESULT_CANCELED) {
                // 没有选择宿舍, 不做处理
            } else if (resultCode == RESULT_OK) {
                if (data == null) {
                    return;
                }
                String chosenLocation = data.getStringExtra(MainActivity.INTENT_KEY_LOCATION);
                String chosenMacAddress = data.getStringExtra(MainActivity.INTENT_KEY_MAC_ADDRESS);
                if (TextUtils.equals(chosenMacAddress, this.macAddress)
                        || TextUtils.isEmpty(chosenLocation)
                        || TextUtils.isEmpty(chosenMacAddress)) {
                    // 如果选择的设备的mac地址和当前设备的mac地址相同或者mac地址为空，则不做处理
                    return;
                }
                // 设置设备位置
                tv_device_title.setText(chosenLocation);

                // 重新连接设备
                showConnecting();
                presenter.clearObservers(); // 清空旧连接
                presenter.resetSubscriptions(); // 此步骤非常重要，不加会造成重连请求掉进黑洞的现象
                presenter.resetContext();
                this.macAddress = chosenMacAddress;
                presenter.onConnect(chosenMacAddress);
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

    // 单击回退按钮返回 解决返回区域过小问题
    @OnClick({R.id.iv_back, R.id.v_back_placeholder})
    @Optional
    void back() {
        super.onBackPressed();
//        finish();
    }

    @Override
    public void showRechargeDialog(double amount) {
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

    /**
     * 更换宿舍
     */
    @OnClick({R.id.tv_sub_title, R.id.v_title_placeholder})
    public void changeDormitory() {
        // 只有在step为SETILE时才不能更换宿舍
        if (presenter.getStep() != TradeStep.SETTLE) {
            startActivityForResult(new Intent(this, ChooseDormitoryActivity.class), CHOOSE_DORMITORY_CODE);
        }
    }

    @Override
    public void startUse() {
        // 点击支付操作时蓝牙必须为开启状态
        setBleCallback(() -> {
            bt_pay.setEnabled(false);
            presenter.onPay(Payment.BALANCE.getType(), (Double) tv_water_right.getTag(R.id.money_pay_amount), null);

        });
        getBlePermission();
    }

    public abstract P setPresenter();

    @Override
    public void onError(TradeError tradeError) {

        presenter.cancelTimer();

        // 显示错误页面，必须加这行判断，否则在activity销毁时会报空指针错误
        if (null != ll_content_normal && null != ll_content_shower && null != ll_content_unconnected && null != ll_error) {
            ll_content_normal.setVisibility(View.GONE);
            ll_content_shower.setVisibility(View.GONE);
            ll_content_unconnected.setVisibility(View.GONE);
            ll_error.setVisibility(View.VISIBLE);

            v_loading.setVisibility(tradeError.isShowLoading() ? View.VISIBLE : View.GONE);
            tv_error_title.setText(getString(tradeError.getErrorTitle()));
            tv_error_tip.setText(getString(tradeError.getErrorTip()));
            bt_error_handler.setText(getString(tradeError.getBtnText()));
            bt_error_handler.setTag(tradeError.getBtnTag());
        }

    }
}
