package com.xiaolian.amigo.ui.wallet;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.xiaolian.amigo.R;
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

    private DecimalFormat df = new DecimalFormat("###.##");

    @Override
    protected void initView() {
        setMainBackground(R.color.white);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(WalletActivity.this);
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

    // 充值
    @OnClick({R.id.iv_recharge, R.id.rl_recharge})
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

    // 充值提现记录
    @OnClick(R.id.rl_withdrawal_record)
    void withdrawalRecord() {
        startActivity(new Intent(this, WithdrawalRecordActivity.class));
    }

    @Override
    public void setBalanceText(Double balance) {
        tv_balance.setText(String.format(Locale.getDefault(), "¥%s", df.format(balance)));
    }

    @Override
    public void setPrepayText(Double prepay) {
        tv_prepay.setText(String.format(Locale.getDefault(), "¥%s", df.format(prepay)));
    }

    @Override
    public void gotoWithDraw() {
        startActivity(new Intent(this, WithdrawalActivity.class)
                .putExtra(Constant.EXTRA_KEY, tv_balance.getText().toString().replace("¥", "")));
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
        dialog.setCancelVisible(false);
        dialog.setOkText(getString(R.string.i_know));
        dialog.setOnOkClickListener(Dialog::dismiss
//                startActivity(new Intent(this, WithdrawalActivity.class)
//                        .putExtra(Constant.EXTRA_KEY, tv_balance.getText().toString().replace("¥", "")))
        );
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
