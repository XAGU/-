package com.xiaolian.amigo.activity.wallet;

import android.os.Bundle;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.activity.BaseActivity;

import butterknife.ButterKnife;

/**
 * 待找零账单
 * <p>
 * Created by caidong on 2017/9/8.
 */

public class PrepayOrderActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_prepay_order);
        ButterKnife.bind(this);
    }
}
