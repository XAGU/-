package com.xiaolian.amigo.ui.wallet;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.WithdrawOperationType;
import com.xiaolian.amigo.ui.wallet.adaptor.WithdrawalAdaptor;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawRecordPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalRecordView;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

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
    private static final int REQUEST_CODE_DETAIL = 0x1201;
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
        adaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivityForResult(new Intent(WithdrawalRecordActivity.this,
                        WithdrawOperationType.getOperationType(items.get(position).getType())
                            .getClz())
                    .putExtra(Constant.EXTRA_KEY, items.get(position).getId()),
                        REQUEST_CODE_DETAIL);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);
//        recyclerView.addItemDecoration(new SpaceItemDecoration(
//                ScreenUtils.dpToPxInt(this, 14)));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_DETAIL) {
            onRefresh();
        }
    }
}
