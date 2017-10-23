package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.wallet.intf.IRechargeDetailView;

/**
 * 充值详情
 * <p>
 * Created by zcd on 10/23/17.
 */

public class RechargeDetailActivity extends WalletBaseActivity implements IRechargeDetailView {
    @Override
    protected void initView() {

    }

    @Override
    protected int setTitle() {
        return 0;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_recharge_detail;
    }
}
