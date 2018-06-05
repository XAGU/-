package com.xiaolian.amigo.ui.wallet;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.order.OrderActivity;
import com.xiaolian.amigo.ui.wallet.intf.IWalletPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWalletView;
import com.xiaolian.amigo.ui.widget.dialog.AvailabilityDialog;
import com.xiaolian.amigo.util.Constant;

import java.text.DecimalFormat;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的钱包
 *
 * @author zcd
 * @date 17/9/18
 */
public class WalletActivity extends WalletBaseActivity implements IWalletView {

    @Inject
    IWalletPresenter<IWalletView> presenter;

    /**
     * 余额
     */
    @BindView(R.id.tv_balance)
    TextView tvBalance;

    /**
     * 预付金额
     */
    @BindView(R.id.tv_prepay)
    TextView tvPrepay;

    /**
     * 增送的余额
     */
    @BindView(R.id.tv_balance_present)
    TextView tvBalancePresent;

    /**
     * 可提现余额
     */
    @BindView(R.id.tv_withdraw_available)
    TextView tvWithdrawAvailable;

    /**
     * 问号
     */
    @BindView(R.id.iv_question)
    ImageView ivQuestion;

    /**
     * 问号点击区域
     */
    @BindView(R.id.v_question)
    View vQuestion;
    /**
     * 提现入口
     */
    @BindView(R.id.rl_withdrawal)
    RelativeLayout rlWithdrawal;

    /**
     * 增送金额趋势
     */
    @BindView(R.id.rl_giving_balance)
    RelativeLayout rlGivingBalance;

    private DecimalFormat df = new DecimalFormat("###.##");

    @Override
    protected void initView() {
        setMainBackground(R.color.white);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(WalletActivity.this);
        setToolbarSubTitle("月账单", v ->
                startActivity(new Intent(WalletActivity.this, MonthlyBillActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.requestNetWork();
    }

    @Override
    protected int setTitle() {
        return R.string.my_wallet;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_wallet;
    }

    /**
     * 充值
     */
    @OnClick({R.id.iv_recharge, R.id.rl_recharge})
    void recharge() {
        startActivity(this, RechargeActivity.class);
    }

    /**
     * 查看预付金额列表
     */
    @OnClick(R.id.rl_prepay)
    void prepay() {
        startActivity(this, PrepayActivity.class);
    }

    /**
     * 提现
     */
    @OnClick(R.id.rl_withdrawal)
    void withdrawal() {
        presenter.queryWithdrawTimeValid();
    }

    /**
     * 余额说明
     */
    @OnClick(R.id.v_question)
    void gotoBalanceExplain() {
        startActivity(new Intent(this, BalanceExplainActivity.class)
            .putExtra(WalletConstant.KEY_GIVE_RULE, presenter.getGivingRule())
            .putExtra(WalletConstant.KEY_USE_LIMIT, presenter.getUseLimit())
            .putExtra(WalletConstant.KEY_ALL_BALANCE, presenter.getAllBalance())
            .putExtra(WalletConstant.KEY_CHARGE_BALANCE, presenter.getChargeBalance())
            .putExtra(WalletConstant.KEY_GIVING_BALANCE, presenter.getGivingBalance()));
    }

    /**
     * 消费记录
     */
    @OnClick(R.id.rl_order)
    void orderRecord() {
        startActivity(new Intent(this, OrderActivity.class));
    }

    /**
     * 充值提现记录
     */
    @OnClick(R.id.rl_withdrawal_record)
    void withdrawalRecord() {
        startActivity(new Intent(this, WithdrawalRecordActivity.class));
    }

    @Override
    public void setBalanceText(Double balance) {
        tvBalance.setText(String.format(Locale.getDefault(), "¥%s", df.format(balance)));
    }

    @Override
    public void setPrepayText(Double prepay) {
        tvPrepay.setText(String.format(Locale.getDefault(), "¥%s", df.format(prepay)));
    }

    @Override
    public void gotoWithDraw() {
        startActivity(new Intent(this, WithdrawalActivity.class)
                .putExtra(Constant.EXTRA_KEY, df.format(presenter.getChargeBalance())));
    }

    @Override
    public void showTimeValidDialog(String title, String remark) {
        AvailabilityDialog dialog = new AvailabilityDialog(this);
        if (TextUtils.isEmpty(title)) {
            dialog.setTip(getString(R.string.withdraw_tip));
        } else {
            dialog.setTip(remark);
        }
        dialog.setType(AvailabilityDialog.Type.WITHDRAW_VALID);
        dialog.setTitle(title);
        dialog.setCancelVisible(false);
        dialog.setOkText(getString(R.string.i_know));
        dialog.setOnOkClickListener(Dialog::dismiss
//                startActivity(new Intent(this, WithdrawalActivity.class)
//                        .putExtra(Constant.EXTRA_KEY, tvBalance.getText().toString().replace("¥", "")))
        );
        dialog.show();
    }

    @Override
    public void setBalancePresentText(Double givingBalance) {
        setBalancePresent(String.format(Locale.getDefault(), "¥%s", df.format(givingBalance)));
    }

    @Override
    public void setWithdrawAvailableText(Double chargeBalance) {
        setWithdrawAvailable(String.format(Locale.getDefault(), "¥%s", df.format(chargeBalance)));
    }

    @Override
    public void hideGivingBalance() {
//        vQuestion.setVisibility(View.GONE);
//        ivQuestion.setVisibility(View.GONE);
        rlGivingBalance.setVisibility(View.GONE);
    }

    @Override
    public void showGivingBalance() {
//        vQuestion.setVisibility(View.VISIBLE);
//        ivQuestion.setVisibility(View.VISIBLE);
        rlGivingBalance.setVisibility(View.VISIBLE);
    }

    /**
     * 设置增送的余额
     */
    private void setBalancePresent(String value) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String tip = getString(R.string.present_balance_colon);
        SpannableString valueSpan = new SpannableString(value);
        valueSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorFullRed)),
                0, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(tip);
        builder.append(valueSpan);
        tvBalancePresent.setText(builder);
    }

    /**
     * 可提现余额
     */
    private void setWithdrawAvailable(String value) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String tip = getString(R.string.withdraw_available_colon);
        SpannableString valueSpan = new SpannableString(value);
        valueSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorFullRed)),
                0, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(tip);
        builder.append(valueSpan);
        tvWithdrawAvailable.setText(builder);
    }

    public void showWithDraw() {
        rlWithdrawal.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideWithDraw() {
        rlWithdrawal.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
