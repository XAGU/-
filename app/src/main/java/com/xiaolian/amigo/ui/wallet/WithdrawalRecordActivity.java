package com.xiaolian.amigo.ui.wallet;

import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalRecordView;

/**
 * <p>
 * Created by zcd on 10/17/17.
 */

public class WithdrawalRecordActivity extends WalletBaseListActivity implements IWithdrawalRecordView {
    @Override
    protected void onRefresh() {

    }

    @Override
    protected void onLoadMore() {

    }

    @Override
    protected void setRecyclerView(RecyclerView recyclerView) {

    }

    @Override
    protected int setTitle() {
        return R.string.withdrawal_record;
    }

    @Override
    protected void initView() {

    }
}
