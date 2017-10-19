package com.xiaolian.amigo.ui.wallet;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.wallet.adaptor.WithdrawalAdaptor;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawRecordPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalRecordView;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 充值提现记录
 * <p>
 * Created by zcd on 10/17/17.
 */

public class WithdrawalRecordActivity extends WalletBaseListActivity implements IWithdrawalRecordView {
    @Inject
    IWithdrawRecordPresenter<IWithdrawalRecordView> presenter;

    private List<WithdrawalAdaptor.WithdrawalWrapper> items = new ArrayList<>();
    private WithdrawalAdaptor adaptor;

    @Override
    protected void onRefresh() {
        page = Constant.PAGE_START_NUM;
        presenter.requestWithdrawalRecord(page);
        items.clear();
    }

    @Override
    protected void onLoadMore() {
        presenter.requestWithdrawalRecord(page);
    }

    @Override
    protected void setRecyclerView(RecyclerView recyclerView) {
        adaptor = new WithdrawalAdaptor(this, R.layout.item_withdrawal_record, items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 10)));
    }

    @Override
    protected int setTitle() {
        return R.string.withdrawal_record;
    }

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(WithdrawalRecordActivity.this);
    }

    @Override
    public void addMore(List<WithdrawalAdaptor.WithdrawalWrapper> wrappers) {
        items.addAll(wrappers);
        adaptor.notifyDataSetChanged();
    }
}
