package com.xiaolian.amigo.activity.wallet;

import android.os.Bundle;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * 提现
 * <p>
 * Created by caidong on 2017/9/7.
 */
public class WithdrawalActivty extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_withdrawal);
        ButterKnife.bind(this);
    }
}
