package com.xiaolian.amigo.ui.device;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.base.TimeHolder;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.enumeration.ErrorTag;
import com.xiaolian.amigo.data.enumeration.TradeError;
import com.xiaolian.amigo.data.enumeration.TradeStep;
import com.xiaolian.amigo.data.network.model.order.OrderPreInfoDTO;
import com.xiaolian.amigo.data.network.model.order.UnsettledOrderStatusCheckRespDTO;
import com.xiaolian.amigo.ui.bonus.BonusActivity;
import com.xiaolian.amigo.ui.bonus.adaptor.BonusAdaptor;
import com.xiaolian.amigo.ui.device.dispenser.ChooseDispenserActivity;
import com.xiaolian.amigo.ui.device.intf.IWaterDeviceBasePresenter;
import com.xiaolian.amigo.ui.device.intf.IWaterDeviceBaseView;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.repair.RepairApplyActivity;
import com.xiaolian.amigo.ui.user.ChooseDormitoryActivity;
import com.xiaolian.amigo.ui.wallet.RechargeActivity;
import com.xiaolian.amigo.ui.widget.BezierWaveView;
import com.xiaolian.amigo.ui.widget.DotFlashView;
import com.xiaolian.amigo.ui.widget.dialog.IOSAlertDialog;
import com.xiaolian.amigo.ui.widget.dialog.NoticeAlertDialog;
import com.xiaolian.amigo.ui.widget.swipebutton.SlideUnlockView;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.DimentionUtils;
import com.xiaolian.amigo.util.Log;
import com.xiaolian.amigo.util.TimeUtils;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * <p>
 * Created by zcd on 10/13/17.
 */

public abstract class WaterDeviceBaseActivity<P extends IWaterDeviceBasePresenter> extends DeviceBaseActivity implements IWaterDeviceBaseView {

    public static final String INTENT_HOME_PAGE_JUMP = "intent_home_page_jump";
    public static final String INTENT_RECOVER = "intent_recover";
    public static final String INTENT_PREPAY_INFO = "intent_prepay_info";

    private static final String TAG = WaterDeviceBaseActivity.class.getSimpleName();
    // 跳转到选择代金券页面的request code
    private static final int CHOOSE_BONUS_CODE = 0x0010;
    protected static final int CHOOSE_DORMITORY_CODE = 0x0011;
    private static final int REQUEST_CODE_RECHARGE = 0x0012;

    // 温馨提示dialog
    private NoticeAlertDialog noticeAlertDialog;

    // 确认支付
    @BindView(R.id.bt_pay)
    Button bt_pay;
    // 正常页面
    @BindView(R.id.ll_content_normal)
    LinearLayout ll_content_normal;
    // 未连接页面
    @BindView(R.id.ll_content_unconnected)
    LinearLayout ll_content_unconnected;
    // 连接失败页面
    @BindView(R.id.ll_error)
    LinearLayout ll_error;
    // 当前水温
    @BindView(R.id.tv_temp)
    TextView tv_temp;
    // 开始使用页面
    @BindView(R.id.ll_content_shower)
    LinearLayout ll_content_shower;
    // et.已使用10元代金券
    @BindView(R.id.tv_shower_payed)
    TextView tv_shower_payed;
    // 提示
    @BindView(R.id.trade_tip)
    TextView trade_tip;
    // 设备连接状态
    @BindView(R.id.tv_connect_status)
    TextView tv_connect_status;
    // 结束洗澡
    @BindView(R.id.bt_stop_shower)
    Button bt_stop_shower;
    // 滑动按钮
    @BindView(R.id.slideView)
    SlideUnlockView slideView;
    // 布局头部
    @BindView(R.id.rl_header)
    RelativeLayout rl_header;
    // 底部
    @BindView(R.id.fl_bottom)
    FrameLayout fl_bottom;
    // 右上角按钮
    @BindView(R.id.iv_top_right_icon)
    ImageView iv_top_right_icon;
    // 右上角按钮placeholder
    @BindView(R.id.v_icon_placeholder)
    View v_icon_placeholder;
    // 标题placeholder
    @BindView(R.id.v_title_placeholder)
    View v_title_placeholder;
    // 子标题
    @BindView(R.id.tv_sub_title)
    TextView tv_sub_title;
    // 选择代金券
    @BindView(R.id.rl_choose_bonus)
    RelativeLayout rl_choose_bonus;
    // 支付方式左侧textview
    @BindView(R.id.tv_water_left)
    TextView tv_water_left;
    // 支付方式右侧textview
    @BindView(R.id.tv_water_right)
    TextView tv_water_right;
    // 波浪控件
    @BindView(R.id.bsv_wave)
    BezierWaveView bsv_wave;
    // 加载进度控件
    @BindView(R.id.dfv_dot)
    DotFlashView dfv_dot;
    // 设备标题
    @BindView(R.id.tv_device_title)
    TextView tv_device_title;
    // 显示加载动画
    @BindView(R.id.v_loading)
    DotFlashView v_loading;
    // 错误标题
    @BindView(R.id.tv_error_title)
    TextView tv_error_title;
    // 错误提示
    @BindView(R.id.tv_error_tip)
    TextView tv_error_tip;
    // 错误处理器
    @BindView(R.id.bt_error_handler)
    Button bt_error_handler;
    // 头部icon
    @BindView(R.id.iv_header_icon)
    ImageView iv_header_icon;
    // 已连接待支付状态标题
    @BindView(R.id.tv_connect_tip_title)
    TextView tv_connect_tip_title;
    @BindView(R.id.tv_connect_tip)
    TextView tv_connect_tip;
    @BindView(R.id.v_connect_tip_bar)
    View v_connect_tip_bar;

    /**
     * 支付信息
     */
    private Long bonusId;
    private String bonusDescription;
    private Double bonusAmount;
    private Double prepay;
    private Double minPrepay = 0.01;
    private Double balance;
    private Double prepayAmount;
    // 最低费率
    private Integer price;

    P presenter;

    private BonusAdaptor.BonusWrapper choosedBonus;

    // 设备类型
    private int deviceType;
    // 设备位置
    private String location;
    // 设备位置id
    private Long residenceId;
    private boolean homePageJump;
    // 标记是否为恢复用水
    private boolean recorvery;
    private CountDownTimer timer;
    private volatile boolean userWater = false;
    private boolean needRecharge;
    private boolean supportSlideBack = true;
    private DecimalFormat df = new DecimalFormat("###.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_device);
        setUnBinder(ButterKnife.bind(this));
        initInject();
        initPresenter();
        initView();


        // 连接蓝牙设备
        presenter.setHomePageJump(homePageJump);
        if (recorvery || !homePageJump) {
            presenter.setStep(TradeStep.SETTLE);
        }
        presenter.onPreConnect(macAddress);
        if (prepay == null) {
            presenter.queryPrepayOption(deviceType);
        } else {
            refreshPrepayStatus();
        }
    }

    @Override
    public boolean supportSlideBack() {
        return supportSlideBack;
    }

    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null) {
            macAddress = getIntent().getStringExtra(MainActivity.INTENT_KEY_MAC_ADDRESS);
            deviceType = getIntent().getIntExtra(MainActivity.INTENT_KEY_DEVICE_TYPE, 1);
            location = getIntent().getStringExtra(MainActivity.INTENT_KEY_LOCATION);
            residenceId = getIntent().getLongExtra(MainActivity.INTENT_KEY_RESIDENCE_ID, 0L);
            homePageJump = getIntent().getBooleanExtra(INTENT_HOME_PAGE_JUMP, true);
            recorvery = getIntent().getBooleanExtra(MainActivity.INTENT_KEY_RECOVERY, false);
            OrderPreInfoDTO orderPreInfo = getIntent().getParcelableExtra(INTENT_PREPAY_INFO);
            if (orderPreInfo != null) {
                price = orderPreInfo.getPrice();
                balance = orderPreInfo.getBalance();
                minPrepay = orderPreInfo.getMinPrepay();
                if (orderPreInfo.getBonus() != null) {
                    bonusId = orderPreInfo.getBonus().getId();
                    bonusDescription = orderPreInfo.getBonus().getDescription();
                    bonusAmount = orderPreInfo.getBonus().getAmount();
                    if (bonusAmount == null || bonusAmount < 0) {
                        bonusAmount = 0.0;
                    }
                }
                prepay = orderPreInfo.getPrepay();
                if (balance == null || balance < 0) {
                    balance = 0.0;
                }
                if (minPrepay == null || minPrepay < 0) {
                    minPrepay = 0.0;
                }
            }
        }
    }

    protected abstract void initInject();

    protected abstract
    @DrawableRes
    int setHeaderBackgroundDrawable();

    protected abstract
    @ColorRes
    int setBottomBackgroundColor();

    private void initPresenter() {
        presenter = setPresenter();
    }

    private void initView() {
        tv_connect_status.setText(recorvery ? "正在恢复上一次用水" : "正在连接设备, 请稍后");
        toggleSubTitle(!recorvery);
        tv_device_title.setText(location);
        setHeaderBackground(setHeaderBackgroundDrawable());
        setBottomBackground(setBottomBackgroundColor());
        setHeaderIcon(setHeaderIconDrawable());
        setTopRightIcon(setTopRightIconDrawable());
        setTopRightIconClickEvent(setTopRightIconClickListener());
        setTitleClickEvent(setTitleClickListener());
        setSubtitle(setSubtitleString());
        if (bsv_wave != null && !bsv_wave.isRunning()) {
            bsv_wave.startAnim();
        }
        setTempText(tv_temp);
        // 默认显示连接中状态
        showConnecting();
        initSlideView();
    }

    private void initSlideView() {
        slideView.setOnUnLockListener(lock -> {
            if (lock) {
                onSlideUnlock();
            }
        });
    }

    protected void setTempText(TextView tempText) {
    }

    protected String getLocation() {
        return location;
    }

    protected abstract String setSubtitleString();

    protected void setSubtitle(@NonNull String titile) {
        tv_sub_title.setText(titile);
    }

    private void setTitleClickEvent(View.OnClickListener onClickListener) {
        tv_sub_title.setOnClickListener(onClickListener);
        v_title_placeholder.setOnClickListener(onClickListener);
    }

    protected abstract View.OnClickListener setTitleClickListener();

    private void setTopRightIconClickEvent(View.OnClickListener onClickListener) {
        v_icon_placeholder.setOnClickListener(onClickListener);
    }

    protected abstract View.OnClickListener setTopRightIconClickListener();

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

    // 设置底部颜色
    private void setBottomBackground(int color) {
        fl_bottom.setBackgroundResource(color);
    }

    // 更新预付界面
    private void refreshPrepayStatus() {
        if (minPrepay == null || prepay == null || balance == null) {
            Log.wtf(TAG, "预付信息不全!");
            return;
        }
        // 有代金券
        if (bonusId != null) {
            String tip;
            String buttonText;
            // 余额为0
            if (balance == 0) {
                // 代金券金额大于等于最小预付金额
                if (bonusAmount >= minPrepay) {
                    tip = getString(R.string.connect_prepay_tip_5, df.format(minPrepay));
                    prepayAmount = 0.0;
                    needRecharge = false;
                    SpannableStringBuilder builder = new SpannableStringBuilder();
                    builder.append(getString(R.string.prepay));
                    buttonText = df.format(prepayAmount) + getString(R.string.yuan);
                    SpannableString buttonSpan = new SpannableString(buttonText);
                    buttonSpan.setSpan(new AbsoluteSizeSpan(
                                    DimentionUtils.convertSpToPixels(18, this)), 0, buttonText.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(buttonSpan);
                    builder.append(getSlideButtonText());

                    showBonusLayout(tip, builder, bonusDescription);
                }
                // 代金券金额小于最小预付金额  需要充值
                else {
                    tip = getString(R.string.connect_prepay_tip_7, df.format(minPrepay));
                    needRecharge = true;
                    buttonText = getString(R.string.to_recharge);
                    showBonusLayout(tip, new SpannableStringBuilder(buttonText), bonusDescription);
                }

            }
            // 余额不为0
            else {
                // 余额加代金券大于等于预付金额
                if (balance + bonusAmount >= prepay) {
                    tip = getString(R.string.connect_prepay_tip_4, df.format(prepay));
                    prepayAmount = prepay - bonusAmount;
                    if (prepayAmount < 0) {
                        prepayAmount = 0.0;
                    }
                    needRecharge = false;
                    SpannableStringBuilder builder = new SpannableStringBuilder();
                    builder.append(getString(R.string.prepay));
                    buttonText = df.format(prepayAmount) + getString(R.string.yuan);
                    SpannableString buttonSpan = new SpannableString(buttonText);
                    buttonSpan.setSpan(new AbsoluteSizeSpan(
                                    DimentionUtils.convertSpToPixels(18, this)), 0, buttonText.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(buttonSpan);
                    builder.append(getSlideButtonText());
                    showBonusLayout(tip, builder, bonusDescription);
                }
                // 余额加代金券小于预付金额 大于等于最小预付金额
                else if (balance + bonusAmount >= minPrepay
                        && balance + bonusAmount < prepay) {
                    tip = getString(R.string.connect_prepay_tip_6, df.format(prepay));
                    prepayAmount = balance;
                    needRecharge = false;
                    SpannableStringBuilder builder = new SpannableStringBuilder();
                    builder.append(getString(R.string.prepay));
                    buttonText = df.format(prepayAmount) + getString(R.string.yuan);
                    SpannableString buttonSpan = new SpannableString(buttonText);
                    buttonSpan.setSpan(new AbsoluteSizeSpan(
                                    DimentionUtils.convertSpToPixels(18, this)), 0, buttonText.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(buttonSpan);
                    builder.append(getSlideButtonText());
                    showBonusLayout(tip, builder, bonusDescription);
                }
                // 余额加代金券小于最小预付金额
                else if (balance + bonusAmount < minPrepay) {
                    tip = getString(R.string.connect_prepay_tip_7, df.format(minPrepay));
                    needRecharge = true;
                    buttonText = getString(R.string.to_recharge);
                    showBonusLayout(tip, new SpannableStringBuilder(buttonText), bonusDescription);
                }
            }
        }
        // 无代金券
        else {
            String title;
            String tip;
            String buttonText;
            // 余额大于等于最小预付金额
            if (balance >= minPrepay) {
                // 余额大于等于预付金额
                if (balance >= prepay) {
                    prepayAmount = prepay;
                    title = getString(R.string.need_prepay_amount, df.format(prepayAmount));
                    tip = getString(R.string.connect_prepay_tip_1);
                    SpannableStringBuilder builder = new SpannableStringBuilder();
                    builder.append(getString(R.string.prepay));
                    buttonText = df.format(prepayAmount) + getString(R.string.yuan);
                    SpannableString buttonSpan = new SpannableString(buttonText);
                    buttonSpan.setSpan(new AbsoluteSizeSpan(
                                    DimentionUtils.convertSpToPixels(18, this)), 0, buttonText.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(buttonSpan);
                    builder.append(getSlideButtonText());
                    needRecharge = false;
                    showNoBonusLayout(title, tip, builder);
                } else {
                    prepayAmount = balance;
                    title = getString(R.string.need_prepay_amount, df.format(prepayAmount));
                    tip = getString(R.string.connect_prepay_tip_2, df.format(prepay));
                    needRecharge = false;
                    SpannableStringBuilder builder = new SpannableStringBuilder();
                    builder.append(getString(R.string.prepay));
                    buttonText = df.format(prepayAmount) + getString(R.string.yuan);
                    SpannableString buttonSpan = new SpannableString(buttonText);
                    buttonSpan.setSpan(new AbsoluteSizeSpan(
                                    DimentionUtils.convertSpToPixels(18, this)), 0, buttonText.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(buttonSpan);
                    builder.append(getSlideButtonText());
                    showNoBonusLayout(title, tip, builder);
                }
            }
            // 余额不足
            else {
                title = getString(R.string.prepay_no_balance);
                tip = getString(R.string.connect_prepay_tip_3, df.format(minPrepay));
                buttonText = getString(R.string.to_recharge);
                needRecharge = true;
                showNoBonusLayout(title, tip, new SpannableStringBuilder(buttonText));
            }
        }

    }

    // 滑动按钮文字 比如 热水澡显示为"开始洗澡" 饮水机显示为 "开始接水"
    protected abstract String getSlideButtonText();

    // 开始使用设备后的提示文案 比如 热水澡显示为 "用水结束后请务必滑动下方按钮，进行结算找零\n消费金额以实际用水量为"
    protected abstract String getBalanceTradeTip();

    @Override
    public void setPrepayOption(OrderPreInfoDTO data) {
        if (data.getBonus() != null) {
            bonusId = data.getBonus().getId();
            bonusAmount = data.getBonus().getAmount();
            bonusDescription = data.getBonus().getDescription();
        }
        prepay = data.getPrepay();
        minPrepay = data.getMinPrepay();
        balance = data.getBalance();
        refreshPrepayStatus();
    }

    private void showBonusLayout(String tip, SpannableStringBuilder buttonText, String remarks) {
        rl_choose_bonus.setVisibility(View.VISIBLE);
        tv_connect_tip_title.setVisibility(View.GONE);
        tv_connect_tip.setVisibility(View.VISIBLE);
        v_connect_tip_bar.setVisibility(View.GONE);
        tv_connect_tip.setText(tip);
        bt_pay.setText(buttonText);
        tv_water_right.setText(remarks);
    }

    private void showNoBonusLayout(String title, String tip, SpannableStringBuilder buttonText) {
        rl_choose_bonus.setVisibility(View.GONE);
        tv_connect_tip_title.setVisibility(View.VISIBLE);
        tv_connect_tip.setVisibility(View.VISIBLE);
        v_connect_tip_bar.setVisibility(View.VISIBLE);
        tv_connect_tip_title.setText(title);
        tv_connect_tip.setText(tip);
        bt_pay.setText(buttonText);
    }

    /**
     * 点击选择用水量或选择代金券
     */
    @OnClick(R.id.rl_choose_bonus)
    void onChooseBonusClick() {
        Intent intent = new Intent(this, BonusActivity.class);
        intent.putExtra(BonusActivity.INTENT_KEY_BONUS_ACTION, BonusActivity.ACTION_CHOOSE);
        intent.putExtra(BonusActivity.INTENT_KEY_BONUS_DEVICE_TYPE, deviceType);
        startActivityForResult(intent, CHOOSE_BONUS_CODE);
    }

    /**
     * 显示设备连接中
     */
    void showConnecting() {
        hideBottomLayout();
        ll_content_unconnected.setVisibility(View.VISIBLE);
        // tv_connect_status.setText("正在连接设备，请稍后");
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
        if (visible) {
            supportSlideBack = true;
        } else {
            supportSlideBack = false;
        }
        if (tv_sub_title != null && (deviceType == 1 || deviceType == 2)) {
            tv_sub_title.setVisibility(visible ?
                    View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onConnectSuccess(TradeStep step, Object... extra) {
        presenter.setConnecting(false);
        if (TradeStep.PAY == step) {
            showStep1();
            // 标记步骤为确认支付页面
            presenter.setStep(TradeStep.PAY);
            toggleSubTitle(true);
            // 激活支付按钮
            bt_pay.setEnabled(true);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Log.wtf(TAG, e);
            }
            initConnectSuccessTimer();
        } else { // TradeStep.SETTLE
            showStep2((UnsettledOrderStatusCheckRespDTO) extra[0]);

            // 标记步骤为结算找零页面
            presenter.setStep(TradeStep.SETTLE);
            toggleSubTitle(false);
        }

    }

    /**
     * 初始化握手成功定时器
     */
    private void initConnectSuccessTimer() {
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
        timer = new CountDownTimer(30 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (userWater) {
                    this.cancel();
                    Log.i(TAG, "已支付进入用水环节，取消定时器。");
                }
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "设备连接到达30s用户仍然未支付进入用水环节。");

                // 关闭蓝牙连接
                presenter.closeBleConnecttion();

                onError(TradeError.CONNECT_ERROR_1);
            }
        };
        Log.i(TAG, "启动30s定时器，监测用户是否长时间占用设备连接。");
        timer.start();
    }

    @Override
    public void onOpen() {
        startShower(null);
        // 标记步骤为结束用水页面
        presenter.setStep(TradeStep.SETTLE);
        toggleSubTitle(false);
    }

    @Override
    public void onConnectError() {
        // 连接失败时显示重连页面
        if (null != ll_content_normal && null != ll_content_shower && null != ll_content_unconnected && null != ll_error) {
            ll_content_normal.setVisibility(View.GONE);
            ll_content_shower.setVisibility(View.GONE);
            ll_content_unconnected.setVisibility(View.GONE);
            ll_error.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onReconnectSuccess(Object... extra) {
        super.onReconnectSuccess(extra);
        // 能进入onReconnectSuccess，表示step为SETTLE
        presenter.setConnecting(false);
        hideBottomLayout();
        // 显示结束用水页面
        ll_content_shower.setVisibility(View.VISIBLE);
        showStep2((UnsettledOrderStatusCheckRespDTO) extra[0]);
        toggleSubTitle(false);
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
        // 需要充值
        if (needRecharge) {
            startActivityForResult(new Intent(this, RechargeActivity.class),
                    REQUEST_CODE_RECHARGE);
        } else {
            if (prepayAmount == null || prepayAmount < 0) {
                prepayAmount = 0.0;
            }
            if (prepayAmount <= 0 && bonusId == null) {
                onError("预付金额不能为0");
                return;
            }
            realPay();
        }
    }

    // 显示温馨提现
    protected void showAlertNotice(NoticeAlertDialog.OnOkClickListener listener) {
        if (price == null || price == 0) {
            return;
        }
        if (noticeAlertDialog == null) {
            noticeAlertDialog = new NoticeAlertDialog(this);
        }
        if (Device.getDevice(deviceType) == Device.HEATER) {
            noticeAlertDialog.setContent(Html.fromHtml(getString(R.string.heater_notice_content, price)));
        } else if (Device.getDevice(deviceType) == Device.DISPENSER) {
            noticeAlertDialog.setContent(Html.fromHtml(getString(R.string.dispenser_notice_content, price)));
        }
        noticeAlertDialog.setTitle(R.string.water_use_notice_title);
        noticeAlertDialog.hideNoticeSymbol();
        noticeAlertDialog.setOnOkClickListener(listener);
        noticeAlertDialog.show();
    }

    private void realPay() {
        userWater = true;
        // 点击支付操作时蓝牙必须为开启状态
        setBleCallback(() -> {
            bt_pay.setEnabled(false);
            presenter.onPay(prepayAmount, bonusAmount != null && bonusAmount > 0 ? bonusId : null);
        });
        getBlePermission();
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

    void onSlideUnlock() {
        // 点击结束用水操作时蓝牙必须为开启状态
        setBleCallback(() -> presenter.onClose());
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
                intent.putExtra(Constant.LOCATION, Device.getDevice(deviceType).getDesc() + "：" + location);
                startActivity(intent);
                finish();
                break;
            case CALL:
                // 取消掉联系客服状态
//                presenter.queryCsInfo();
                break;
            case CHANGE_DORMITORY:
                changeDormitory();
                break;
            case CHANGE_DISPENSER:
                changeDispenser();
                break;
        }
    }

    @Override
    public void onFinish(long orderId) {
        if (orderId == 0L) {
            Log.wtf(TAG, "订单id不能为0");
            return;
        }
        presenter.onDisConnect();
        Intent intent = new Intent(this, DeviceOrderActivity.class);
        intent.putExtra(Constant.BUNDLE_ID, orderId);
        startActivity(intent);
        finish();
    }

    private void startShower(UnsettledOrderStatusCheckRespDTO orderStatus) {
        hideBottomLayout();
        ll_content_shower.setVisibility(View.VISIBLE);
        slideView.reset();

        if (null != orderStatus) {
            tv_shower_payed.setText(orderStatus.getExtra());
            // 从未结算订单列表跳转过来，tip展示需要标注订单时间等信息
            if (!homePageJump) {
                String time = TimeUtils.convertTimestampToAccurateFormat(orderStatus.getCreateTime());
                String tip = String.format("你%s在%s使用了%s\n点击滑动下方按钮进行结算找零", time, orderStatus.getLocation(), Device.getDevice(deviceType).getDesc());
//                    String tip = String.format("您%s在%s使用预付了%s元的用水已结束\n点击下方按钮进行找零", time, orderStatus.getLocation(), String.valueOf(orderStatus.getPrepay().intValue()));
                trade_tip.setText(tip);
            } else {
                trade_tip.setText(getBalanceTradeTip());
            }
            bt_stop_shower.setText(getString(R.string.settlement_and_change));
            bt_stop_shower.setVisibility(View.GONE);
            slideView.setVisibility(View.VISIBLE);
            slideView.setDisableStr(getString(R.string.slide_to_settlement));
            slideView.setEnableStr(getString(R.string.settlement));
        } else {
            tv_shower_payed.setText(getString(R.string.prepaid, String.valueOf(prepayAmount)));
            trade_tip.setText(getBalanceTradeTip());
            bt_stop_shower.setText(getString(R.string.settlement_and_change));
            bt_stop_shower.setVisibility(View.GONE);
            slideView.setVisibility(View.VISIBLE);
            slideView.setDisableStr(getString(R.string.slide_to_settlement));
            slideView.setEnableStr(getString(R.string.settlement));
        }

    }

    private void endShower() {
        hideBottomLayout();
        ll_content_normal.setVisibility(View.VISIBLE);
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
                bonusAmount = 0.0;
                bonusDescription = getString(R.string.not_use_bonus);
                refreshPrepayStatus();
            } else if (resultCode == RESULT_OK) {
                choosedBonus = (BonusAdaptor.BonusWrapper) data.getSerializableExtra(BonusActivity.INTENT_KEY_BONUS_RESULT);
                if (choosedBonus != null) {
                    bonusId = choosedBonus.getId();
                    bonusAmount = choosedBonus.getAmount();
                    bonusDescription = choosedBonus.getDescription();
                    refreshPrepayStatus();
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
                Long chosenResidenceId = data.getLongExtra(MainActivity.INTENT_KEY_RESIDENCE_ID, -1);
                if (TextUtils.equals(chosenMacAddress, this.macAddress)
                        || TextUtils.isEmpty(chosenLocation)
                        || TextUtils.isEmpty(chosenMacAddress)) {
                    // 如果选择的设备的mac地址和当前设备的mac地址相同或者mac地址为空，则不做处理
                    return;
                }
                // 设置设备位置
                tv_device_title.setText(chosenLocation);
                if (chosenResidenceId != -1) {
                    residenceId = chosenResidenceId;
                }

                // 重新连接设备
                showConnecting();
                presenter.clearObservers(); // 清空旧连接
                presenter.resetSubscriptions(); // 此步骤非常重要，不加会造成重连请求掉进黑洞的现象
                presenter.resetContext();
                this.macAddress = chosenMacAddress;
                presenter.onConnect(chosenMacAddress);
            }
        } else if (requestCode == REQUEST_CODE_RECHARGE) {
            presenter.queryPrepayOption(deviceType);
        }
    }


    @Override
    protected void onDestroy() {
        presenter.onDisConnect();
        super.onDestroy();
        if (null != timer) {
            timer.cancel();
        }
        TimeHolder.get().setLastConnectTime(System.currentTimeMillis());
    }


    @Override
    public void showRechargeDialog(double amount) {
        new IOSAlertDialog(this).builder()
                .setMsg("sorry,你的账户余额不足" + amount + "元~")
                .setPositiveButton("前往充值", v -> startActivity(new Intent(getApplicationContext(), RechargeActivity.class)))
                .setNegativeClickListener("取消", IOSAlertDialog::dismiss).show();
    }


    @Override
    public void startUse() {
        // 点击支付操作时蓝牙必须为开启状态
        setBleCallback(() -> {
            bt_pay.setEnabled(false);
            presenter.onPay((Double) tv_water_right.getTag(R.id.money_pay_amount), null);

        });
        getBlePermission();
    }

    public abstract P setPresenter();

    @Override
    public void onError(TradeError tradeError) {
        // 如果是在结算页面，则重置slideView
//        if (presenter.getStep() == TradeStep.SETTLE && slideView != null) {
//            slideView.reset();
//        }
        if (slideView != null) {
            slideView.reset();
        }

        // 断开物理连接定时器
        presenter.cancelTimer();

        // 断开握手连接定时器
        if (null != timer) {
            timer.cancel();
        }

        // 结账中不关闭观察者
        if (presenter.getStep() != TradeStep.CLOSE_VALVE) {
            // 异常发生时关闭蓝牙连接
            presenter.closeBleConnecttion();
        }

        // 显示错误页面，必须加这行判断，否则在activity销毁时会报空指针错误
        if (null != ll_content_normal && null != ll_content_shower && null != ll_content_unconnected && null != ll_error) {
            ll_content_normal.setVisibility(View.GONE);
            ll_content_shower.setVisibility(View.GONE);
            ll_content_unconnected.setVisibility(View.GONE);
            ll_error.setVisibility(View.VISIBLE);

            v_loading.setVisibility(tradeError.isShowLoading() ? View.VISIBLE : View.GONE);
            tv_error_title.setText(getString(tradeError.getErrorTitle()));
            tv_error_tip.setText(getString(tradeError.getErrorTip()));

            if (tradeError == TradeError.DEVICE_BROKEN_3 && deviceType == Device.DISPENSER.getType()) {
                bt_error_handler.setText(getString(R.string.change_dispenser));
                bt_error_handler.setTag(ErrorTag.CHANGE_DISPENSER.getCode());
            } else {
                bt_error_handler.setText(getString(tradeError.getBtnText()));
                bt_error_handler.setTag(tradeError.getBtnTag());
            }
        }
    }

    @Override
    public void showCsCallDialog(String tel) {
        CommonUtil.call(this.getApplicationContext(), tel);
    }

    protected void back2Main() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void changeDormitory() {
        // 只有在step为SETILE时才不能更换宿舍
        if (!recorvery && presenter.getStep() != TradeStep.SETTLE) {
            startActivityForResult(
                    new Intent(this, ChooseDormitoryActivity.class)
                            .putExtra(ChooseDormitoryActivity.INTENT_KEY_LAST_DORMITORY, residenceId),
                    CHOOSE_DORMITORY_CODE);
        }
    }

    public void changeDispenser() {
        // 只有在step为SETILE时才不能更换饮水机
        if (presenter.getStep() != TradeStep.SETTLE) {
            startActivity(new Intent(this, ChooseDispenserActivity.class)
                    .putExtra(DeviceConstant.INTENT_KEY_ACTION, DeviceConstant.ACTION_CHANGE_DISPENSER));
        }
    }

    public void changeDryer() {
        // 只有在step为SETILE时才不能更换吹风机
        if (presenter.getStep() != TradeStep.SETTLE) {
            startActivity(new Intent(this, ChooseDispenserActivity.class)
                    .putExtra(DeviceConstant.INTENT_KEY_ACTION, DeviceConstant.ACTION_CHANGE_DRYER));
        }
    }

    // 单击回退按钮返回 解决返回区域过小问题
    @OnClick({R.id.iv_back, R.id.v_back_placeholder})
    @Optional
    void back() {
        back2Main();
    }

//    @Override
//    public void onBackPressed() {
//        back2Main();
//    }

}
