package com.xiaolian.amigo.ui.wallet;

import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.wallet.intf.IBalanceExplainView;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.function.Supplier;

/**
 * 余额
 *
 * @author zcd
 * @date 18/4/23
 */
public class BalanceExplainActivity extends WalletBaseActivity implements IBalanceExplainView {

    /**
     * 可用余额
     */
    private TextView tvBalanceAvailable;
    /**
     * 可提现余额
     */
    private TextView tvWithdrawAvailable;
    /**
     * 增送的余额
     */
    private TextView tvBalancePresent;
    /**
     * 增送规则
     */
    private TextView tvPresentRule;
    /**
     * 使用期限
     */
    private TextView tvUsePeriod;
    /**
     * 可用余额
     */
    private TextView tvBalanceAvailableTip;
    /**
     * 增送的余额
     */
    private TextView tvBalancePresentTip;
    /**
     * 可提现余额
     */
    private TextView tvWithdrawAvailableTip;

    private DecimalFormat df = new DecimalFormat("###.##");

    @Override
    protected void initView() {
        setMainBackground(R.color.white);
        bindView();
        Double allBalance = 0.0;
        Double giveBalance = 0.0;
        Double chargeBalance = 0.0;
        String useLimit = "";
        String giveRule = "";
        if (getIntent() != null) {
            allBalance = getIntent().getDoubleExtra(WalletConstant.KEY_ALL_BALANCE, 0);
            giveBalance = getIntent().getDoubleExtra(WalletConstant.KEY_GIVING_BALANCE, 0);
            chargeBalance = getIntent().getDoubleExtra(WalletConstant.KEY_CHARGE_BALANCE, 0);
            useLimit = getIntent().getStringExtra(WalletConstant.KEY_USE_LIMIT);
            giveRule = getIntent().getStringExtra(WalletConstant.KEY_GIVE_RULE);
        }
        setBalanceAvailable(formatDouble(allBalance));
        setWithdrawAvailable(formatDouble(chargeBalance));
        setBalancePresent(formatDouble(giveBalance));
        setPresentRule(giveRule);
        setUsePeriod(useLimit);
        setBalanceAvailableTip("可用余额＝赠送的余额＋可提现余额，均可以用了消费。系统优先扣除可用余额。");
        setBalancePresentTip("赠送的余额具有有效器，过期自动清零，且只能用于消费，不可提现。");
        setWithdrawAvailableTip("指可用余额中可以用于提现或消费的金额，该金额可提现至支付宝账户。");
    }

    private String formatDouble(Double value) {
        return String.format(Locale.getDefault(), "¥%s", df.format(value));
    }

    private void bindView() {
        tvBalanceAvailable = findViewById(R.id.tv_balance_available);
        tvWithdrawAvailable = findViewById(R.id.tv_withdraw_available);
        tvBalancePresent = findViewById(R.id.tv_balance_present);
        tvPresentRule = findViewById(R.id.tv_present_rule);
        tvUsePeriod = findViewById(R.id.tv_use_period);
        tvBalanceAvailableTip = findViewById(R.id.tv_balance_available_tip);
        tvBalancePresentTip = findViewById(R.id.tv_balance_present_tip);
        tvWithdrawAvailableTip = findViewById(R.id.tv_withdraw_available_tip);
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected int setTitle() {
        return R.string.balance_explain;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_balance_explain;
    }

    private void setTextView(TextView textView, String key, String value, int colorRes) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString valueSpan = new SpannableString(value);
        valueSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, colorRes)),
                0, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(key);
        builder.append(valueSpan);
        textView.setText(builder);
    }

    /**
     * 设置可用余额
     */
    private void setBalanceAvailable(String value) {
        String tip = getString(R.string.balance_available_colon);
        setTextView(tvBalanceAvailable, tip, value, R.color.colorFullRed);
    }

    /**
     * 设置可提现余额
     */
    private void setWithdrawAvailable(String value) {
        String tip = getString(R.string.withdraw_available_colon);
        setTextView(tvWithdrawAvailable, tip, value, R.color.colorFullRed);
    }

    /**
     * 设置赠送的余额
     */
    private void setBalancePresent(String value) {
        String tip = getString(R.string.balance_present_colon);
        setTextView(tvBalancePresent, tip, value, R.color.colorFullRed);
    }

    /**
     * 设置赠送规则
     */
    private void setPresentRule(String value) {
        String tip = getString(R.string.present_rule_colon);
        setTextView(tvPresentRule, tip, value, R.color.colorDark2);
    }

    /**
     * 设置使用期限
     */
    private void setUsePeriod(String value) {
        String tip = getString(R.string.use_period_colon);
        setTextView(tvUsePeriod, tip, value, R.color.colorDark2);
    }

    /**
     * 设置可用余额提示
     */
    private void setBalanceAvailableTip(String value) {
        String tip = getString(R.string.balance_available_colon);
        setTextView(tvBalanceAvailableTip, tip, value, R.color.colorDark2);
    }

    /**
     * 设置赠送的余额提示
     */
    private void setBalancePresentTip(String value) {
        String tip = getString(R.string.present_balance_colon);
        setTextView(tvBalancePresentTip, tip, value, R.color.colorDark2);
    }

    /**
     * 设置可提现余额提示
     */
    private void setWithdrawAvailableTip(String value) {
        String tip = getString(R.string.withdraw_available_colon);
        setTextView(tvWithdrawAvailableTip, tip, value, R.color.colorDark2);
    }
}
