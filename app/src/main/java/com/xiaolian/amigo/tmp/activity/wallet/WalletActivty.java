package com.xiaolian.amigo.tmp.activity.wallet;

import android.os.Bundle;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.base.BaseActivity;


import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的钱包
 * <p>
 * Created by caidong on 2017/9/7.
 */
public class WalletActivty extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
    }

    // 充值
    @OnClick(R.id.iv_recharge)
    void recharge() {
        startActivity(this, RechargeActivty.class);
    }

    // 查看预付金额列表
    @OnClick(R.id.rl_prepay)
    void prepay() {
        startActivity(this, PrepayActivity.class);
    }

    // 提现
    @OnClick(R.id.rl_withdrawal)
    void withdrawal() {
        startActivity(this, WithdrawalActivty.class);
    }
}
