package com.xiaolian.amigo.ui.wallet;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.activity.wallet.PrepayActivity;
import com.xiaolian.amigo.tmp.activity.wallet.WithdrawalActivty;
import com.xiaolian.amigo.ui.wallet.intf.IWalletPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWalletView;
import com.xiaolian.amigo.ui.widget.dialog.AvailabilityDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的钱包
 * <p>
 * Created by zcd on 2017/9/18.
 */
public class WalletActivity extends WalletBaseActivity implements IWalletView {

    @Inject
    IWalletPresenter<IWalletView> presenter;

    /**
     * 余额
     */
    @BindView(R.id.tv_balance)
    TextView tv_balance;

    /**
     * 预付金额
     */
    @BindView(R.id.tv_prepay)
    TextView tv_prepay;

    @Override
    protected void initView() {
        setMainBackground(R.color.white);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(WalletActivity.this);

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

    // 充值
    @OnClick(R.id.iv_recharge)
    void recharge() {
        startActivity(this, RechargeActivity.class);
    }

    // 查看预付金额列表
    @OnClick(R.id.rl_prepay)
    void prepay() {
        startActivity(this, PrepayActivity.class);
    }

    // 提现
    @OnClick(R.id.rl_withdrawal)
    void withdrawal() {
        presenter.queryWithdrawTimeValid();
    }

    @Override
    public void setBalanceText(String balance) {
        tv_balance.setText(balance);
    }

    @Override
    public void setPrepayText(String prepay) {
        tv_prepay.setText(prepay);
    }

    @Override
    public void gotoWithDraw() {
        startActivity(this, WithdrawalActivty.class);
    }

    @Override
    public void showTimeValidDialog(String title, String remark) {
        AvailabilityDialog dialog = new AvailabilityDialog(this);
        if (TextUtils.isEmpty(title)) {
            dialog.setTip(getString(R.string.withdraw_tip));
        } else {
            dialog.setTip(title);
        }
        dialog.setSubTipVisible(false);
        dialog.setOkText(getString(R.string.ok));
        dialog.setOnOkClickListener(dialog1 -> {
            startActivity(this, WithdrawalActivty.class);
        });
        dialog.show();
    }
}
