package com.xiaolian.amigo.ui.device.bathroom;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderPreconditionRespDTO;
import com.xiaolian.amigo.ui.device.bathroom.adapter.DeviceInfoAdapter;
import com.xiaolian.amigo.ui.widget.BathroomOperationStatusView;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_BALANCE;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_BONUS_AMOUNT;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_BONUS_DESC;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_BONUS_ID;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_EXPIRED_TIME;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_LOCATION;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_MAX_MISSABLE_TIMES;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_MIN_PREPAY;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_MISSED_TIMES;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_ORDER_PRECONDITION;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_PREPAY;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_RESERVEDTIME;

/**
 * 使用公共澡堂基础类
 *
 * @author zcd
 * @date 18/7/2
 */
public abstract class UseWayActivity extends BathroomBaseActivity {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    @BindView(R.id.tv_toolbar_sub_title)
    TextView tvToolbarSubTitle;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_sub_title)
    TextView tvSubTitle;

    @BindView(R.id.view_line)
    View viewLine;

    @BindView(R.id.statusView)
    BathroomOperationStatusView statusView;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.ll_header)
    LinearLayout llHeader;

    @BindView(R.id.rl_tip)
    RelativeLayout rlTip;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.tv_tip1)
    TextView tvTip1;
    @BindView(R.id.tv_tip2)
    TextView tvTip2;
    @BindView(R.id.tv_tip3)
    TextView tvTip3;
    @BindView(R.id.tv_tip4)
    TextView tvTip4;
    @BindView(R.id.tv_booking_tip)
    TextView tvBookingTip;

    @BindView(R.id.bt_start_to_use)
    Button btStartToUse;

    /**
     * 设备位置
     */
    protected String location;
    @BindView(R.id.left_oper)
    TextView leftOper;
    @BindView(R.id.right_oper)
    TextView rightOper;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    /**
     * 过期时间
     */
    protected Long expiredTime;
    /**
     * 预付金额
     */
    protected Double prepay;
    /**
     * 最小预付金额
     */
    protected Double minPrepay;
    /**
     * 用户余额
     */
    protected Double balance;
    /**
     * 红包
     */
    protected Long bonusId = -1L;
    /**
     * 预留时间
     */
    protected String reservedTime;
    protected String bonusDesc;
    protected Double bonusAmount;

    /**
     * 失约次数 只有预约才会返回
     */
    protected Integer missedTimes;
    /**
     * 总共可失约次数 只有预约才会返回
     */
    protected Integer maxMissAbleTimes;

    protected boolean needRecharge;
    protected Double prepayAmount;


    /**
     * 已存在订单状态
     */
    protected BathOrderPreconditionRespDTO bathOrderPreconditionRespDTO;
    @BindView(R.id.tip_recycler)
    RecyclerView tipRecycler;
    private DecimalFormat df = new DecimalFormat("###.##");

    private List<String> tips = new ArrayList<>();

    private CommonAdapter<String> commonAdapter ;


    private String Tips = "" ;
    protected List<DeviceInfoAdapter.DeviceInfoWrapper> items = new ArrayList<DeviceInfoAdapter.DeviceInfoWrapper>() {
        {
//            add(new DeviceInfoAdapter.DeviceInfoWrapper("浴室位置：",
//                    "五楼215", R.color.colorDark2, 14, Typeface.NORMAL, false));
//            add(new DeviceInfoAdapter.DeviceInfoWrapper("预留时间：",
//                    "15分钟", R.color.colorDark2, 14, Typeface.NORMAL, false));
//            add(new DeviceInfoAdapter.DeviceInfoWrapper("预付金额：",
//                    "10元", R.color.colorDark2, 14, Typeface.NORMAL, false));
//            add(new DeviceInfoAdapter.DeviceInfoWrapper("红包抵扣：",
//                    "2元新人有礼", R.color.colorDark2, 14, Typeface.NORMAL, true));
        }
    };

    protected DeviceInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        setUnBinder(ButterKnife.bind(this));
        initIntent();
        initView();
    }


    protected void initIntent() {
        if (getIntent() != null) {
            balance = getIntent().getDoubleExtra(KEY_BALANCE, 0);
            bonusId = getIntent().getLongExtra(KEY_BONUS_ID, Constant.INVALID_ID);
            bonusDesc = getIntent().getStringExtra(KEY_BONUS_DESC);
            bonusAmount = getIntent().getDoubleExtra(KEY_BONUS_AMOUNT, 0);
            minPrepay = getIntent().getDoubleExtra(KEY_MIN_PREPAY, 0);
            prepay = getIntent().getDoubleExtra(KEY_PREPAY, 0);
            location = getIntent().getStringExtra(KEY_LOCATION);
            expiredTime = getIntent().getLongExtra(KEY_EXPIRED_TIME, 0);
            missedTimes = getIntent().getIntExtra(KEY_MISSED_TIMES, 0);
            maxMissAbleTimes = getIntent().getIntExtra(KEY_MAX_MISSABLE_TIMES, 0);
            reservedTime = getIntent().getStringExtra(KEY_RESERVEDTIME);
            bathOrderPreconditionRespDTO = getIntent().getParcelableExtra(KEY_ORDER_PRECONDITION);
        }
    }

    private void initView() {
        llBottomVisible(false);
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            //Log.d("STATE", appBarLayout.getTotalScrollRange() +"//"+ verticalOffset+"//"+tv_toolbar_title.getHeight());
            if (verticalOffset < -(tvToolbarTitle.getHeight() + llHeader.getPaddingTop())) {
                tvTitle.setVisibility(View.VISIBLE);
                tvSubTitle.setVisibility(View.VISIBLE);
                viewLine.setVisibility(View.VISIBLE);
            } else {
                tvTitle.setVisibility(View.GONE);
                tvSubTitle.setVisibility(View.GONE);
                viewLine.setVisibility(View.GONE);
            }
        });
        setTitle(tvTitle);
        setToolbarTitle(tvToolbarTitle);
        setSubTitle(tvSubTitle);
        setToolbarSubTitle(tvToolbarSubTitle);
        setTips(tvTip1, tvTip2, tvTip3, tvTip4, tvTip, rlTip);
        initRecyclerView();
    }


    public TextView getTvTip1() {
        return tvTip1;
    }

    public TextView getTvTip2() {
        return tvTip2;
    }

    public TextView getTvTip() {
        return tvTip;
    }

    public TextView getTvTip3() {
        return tvTip3;
    }

    protected String getTips(){
        return "";
    }


    protected void llBottomVisible(boolean isVisible) {
        if (isVisible && llBottom.getVisibility() == View.GONE) {
            llBottom.setVisibility(View.VISIBLE);
        } else if (!isVisible && llBottom.getVisibility() == View.VISIBLE) {
            llBottom.setVisibility(View.GONE);
        } else {

        }
    }

    protected abstract void setToolbarTitle(TextView textView);

    protected abstract void setTitle(TextView textView);

    protected abstract void setToolbarSubTitle(TextView textView);

    protected abstract void setSubTitle(TextView textView);

    protected abstract void setTips(TextView tip1,
                                    TextView tip2,
                                    TextView tip3,
                                    TextView tip4,
                                    TextView tip,
                                    RelativeLayout rlTip);


    protected abstract String getButtonText();

    protected void setButtonText() {
        if (minPrepay == null || prepay == null || balance == null) {
            adapter.notifyDataSetChanged();
            onError("预付信息不全");
            return;
        }
        String tip = getString(R.string.atLeastInTheWallet);
        // 有代金券
        if (bonusId != -1) {

//            String buttonText;
//            // 余额为0
//            if (balance == 0) {
//                // 代金券金额大于等于最小预付金额
//                if (bonusAmount >= minPrepay) {
////                    tip = df.format(minPrepay) + getString(R.string.yuan);
//                    prepayAmount = 0.0;
//                    needRecharge = false;
//                    SpannableStringBuilder builder = new SpannableStringBuilder();
//                    builder.append(getString(R.string.prepay));
//                    buttonText = df.format(prepayAmount) + getString(R.string.yuan);
//                    SpannableString buttonSpan = new SpannableString(buttonText);
//                    buttonSpan.setSpan(new AbsoluteSizeSpan(
//                                    DimentionUtils.convertSpToPixels(18, this)), 0, buttonText.length(),
//                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    builder.append(buttonSpan);
//                    builder.append(getButtonText());
//                    btStartToUse.setText(builder);
//                    statusView.getTip().setVisibility(View.GONE);
//                }
//                // 代金券金额小于最小预付金额  需要充值
//                else {
////                    tip = df.format(minPrepay) + getString(R.string.yuan);
////                    tip = "低于" + df.format(minPrepay) + getString(R.string.yuan) + "，请前往充值";
//                      tip = String.format(tip ,df.format(minPrepay));
//                    needRecharge = true;
//                    buttonText = getString(R.string.InsufficientBalance);
//                    btStartToUse.setText(buttonText);
//                    statusView.getTip().setText(tip);
//                    statusView.getTip().setVisibility(View.VISIBLE);
//                }
//            }
//            // 余额不为0
//            else {
//                // 余额加代金券大于等于预付金额
//                if (balance + bonusAmount >= prepay) {
////                    tip = df.format(prepay) + getString(R.string.yuan);
//                    prepayAmount = prepay - bonusAmount;
//                    if (prepayAmount < 0) {
//                        prepayAmount = 0.0;
//                    }
//                    needRecharge = false;
//                    SpannableStringBuilder builder = new SpannableStringBuilder();
//                    builder.append(getString(R.string.prepay));
//                    buttonText = df.format(prepayAmount) + getString(R.string.yuan);
//                    SpannableString buttonSpan = new SpannableString(buttonText);
//                    buttonSpan.setSpan(new AbsoluteSizeSpan(
//                                    DimentionUtils.convertSpToPixels(18, this)), 0, buttonText.length(),
//                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    builder.append(buttonSpan);
//                    builder.append(getButtonText());
//                    btStartToUse.setText(builder);
//                    statusView.getTip().setVisibility(View.GONE);
//                }
//                // 余额加代金券小于预付金额 大于等于最小预付金额
//                else if (balance + bonusAmount >= minPrepay
//                        && balance + bonusAmount < prepay) {
////                    tip = "低于" + df.format(prepay) + getString(R.string.yuan) + "需预付全部余额";
//                    prepayAmount = balance;
//                    needRecharge = false;
//                    SpannableStringBuilder builder = new SpannableStringBuilder();
//                    builder.append(getString(R.string.prepay));
//                    buttonText = df.format(prepayAmount) + getString(R.string.yuan);
//                    SpannableString buttonSpan = new SpannableString(buttonText);
//                    buttonSpan.setSpan(new AbsoluteSizeSpan(
//                                    DimentionUtils.convertSpToPixels(18, this)), 0, buttonText.length(),
//                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    builder.append(buttonSpan);
//                    builder.append(getButtonText());
//                    btStartToUse.setText(builder);
//                    statusView.getTip().setVisibility(View.GONE);
//                }
//                // 余额加代金券小于最小预付金额
//                else if (balance + bonusAmount < minPrepay) {
////                    tip = "低于" + df.format(minPrepay) + "，请前往充值";
//                    tip = String.format(tip ,df.format(minPrepay));
//                    needRecharge = true;
//                    buttonText = getString(R.string.InsufficientBalance);
//                    btStartToUse.setText(buttonText);
//                    statusView.getTip().setText(tip);
//                    statusView.getTip().setVisibility(View.VISIBLE);
//                }
//            }
////            items.add(new DeviceInfoAdapter.DeviceInfoWrapper("预付金额：",
////                    tip, R.color.colorDark2, 14, Typeface.NORMAL, false));
//            items.add(new DeviceInfoAdapter.DeviceInfoWrapper("红包抵扣：",
//                    bonusDesc,
//                    R.color.colorDark2, 14, Typeface.NORMAL, true));
//            adapter.notifyDataSetChanged();
        }
        // 无代金券
        else {
            statusView.getTip().setVisibility(View.VISIBLE);
            String title;
            String buttonText;
            // 余额大于等于最小预付金额
            if (balance >= minPrepay) {
                // 余额大于等于预付金额
                if (balance >= prepay) {
                    prepayAmount = prepay;
                    title = getString(R.string.need_prepay_amount, df.format(prepayAmount));
//                    tip = df.format(prepay) + getString(R.string.yuan);
//                    SpannableStringBuilder builder = new SpannableStringBuilder();
//                    builder.append(getString(R.string.prepay));
//                    buttonText = df.format(prepayAmount) + getString(R.string.yuan);
//                    SpannableString buttonSpan = new SpannableString(buttonText);
//                    buttonSpan.setSpan(new AbsoluteSizeSpan(
//                                    DimentionUtils.convertSpToPixels(18, this)), 0, buttonText.length(),
//                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    builder.append(buttonSpan);
//                    needRecharge = false;
//                    builder.append(getButtonText());
                    statusView.getTip().setText(getString(R.string.atLeastInTheWallet, df.format(prepayAmount)));
//                    btStartToUse.setText(getButtonText());
                } else {
                    prepayAmount = balance;
                    title = getString(R.string.need_prepay_amount, df.format(prepayAmount));
//                    tip = "低于" + df.format(prepay) + getString(R.string.yuan) + "，需预付全部余额";
                    needRecharge = false;
//                    SpannableStringBuilder builder = new SpannableStringBuilder();
//                    builder.append(getString(R.string.prepay));
//                    buttonText = df.format(prepayAmount) + getString(R.string.yuan);
//                    SpannableString buttonSpan = new SpannableString(buttonText);
//                    buttonSpan.setSpan(new AbsoluteSizeSpan(
//                                    DimentionUtils.convertSpToPixels(18, this)), 0, buttonText.length(),
//                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    builder.append(buttonSpan);
//                    builder.append(getButtonText());
                    statusView.getTip().setText(getString(R.string.atLeastInTheWallet, df.format(prepayAmount)));
//                    btStartToUse.setText(getButtonText());
                }
            }
            // 余额不足
            else {
//                title = getString(R.string.prepay_no_balance);
//                tip = "低于" + df.format(minPrepay) + getString(R.string.yuan) + "，请前往充值";
                tip = String.format(tip, df.format(minPrepay));
                buttonText = getString(R.string.InsufficientBalance);
                needRecharge = true;
//                btStartToUse.setText(buttonText);
                statusView.getTip().setText(tip);
            }
//            items.add(new DeviceInfoAdapter.DeviceInfoWrapper("预付金额：",
//                    tip, R.color.colorDark2, 14, Typeface.NORMAL, false));
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 根据上一笔订单设置tip 内容
     */
    public void forOldOrderToSetTipTxt(BathOrderPreconditionRespDTO dto) {
        // 余额大于等于最小预付金额
        if (dto.getPrepayInfo().getBalance() >= dto.getPrepayInfo().getMinPrepay()) {
            // 余额大于等于预付金额
            if (dto.getPrepayInfo().getBalance() >= dto.getPrepayInfo().getPrepay()) {
                prepayAmount = dto.getPrepayInfo().getPrepay();
                setTipTxt(getString(R.string.atLeastInTheWallet, df.format(prepayAmount)));
            } else {
                prepayAmount = dto.getPrepayInfo().getBalance();
                setTipTxt(getString(R.string.atLeastInTheWallet, df.format(prepayAmount)));
            }
        }
        // 余额不足
        else {
            setTipTxt(getString(R.string.atLeastInTheWallet, df.format(dto.getPrepayInfo().getPrepay())));
        }
    }

    /**
     * 设置Tip内容
     */
    public void setTipTxt(String tipTxt) {
        statusView.getTip().setVisibility(View.VISIBLE);
        statusView.getTip().setText(tipTxt);
    }


    private void initRecyclerView() {
        adapter = new DeviceInfoAdapter(this, R.layout.item_bathroom_device_info, items);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 1)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    private void setTitleVisible(int visible) {
        tvTitle.setVisibility(visible);
        viewLine.setVisibility(visible);
    }
}
