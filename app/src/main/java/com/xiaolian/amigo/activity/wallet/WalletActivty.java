package com.xiaolian.amigo.activity.wallet;

import android.os.Bundle;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.activity.BaseActivity;

import butterknife.ButterKnife;

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
}
