package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.wallet.intf.IBalanceExplainView;

/**
 * 余额
 *
 * @author zcd
 * @date 18/4/23
 */
public class BalanceExplainActivity extends WalletBaseActivity implements IBalanceExplainView{
    @Override
    protected void initView() {

    }

    @Override
    protected int setTitle() {
        return R.string.balance_explain;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_balance_explain;
    }
}
