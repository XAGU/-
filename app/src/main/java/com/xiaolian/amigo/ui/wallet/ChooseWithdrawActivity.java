package com.xiaolian.amigo.ui.wallet;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.PayWay;
import com.xiaolian.amigo.ui.wallet.adaptor.ChooseWithdrawAdapter;
import com.xiaolian.amigo.ui.wallet.intf.IChooseWithdrawPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IChooseWithdrawView;
import com.xiaolian.amigo.ui.widget.RecycleViewDivider;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择提现账户
 *
 * @author zcd
 * @date 17/10/27
 */

public class ChooseWithdrawActivity extends WalletBaseActivity implements IChooseWithdrawView {
    private static final String TAG = ChooseWithdrawActivity.class.getSimpleName();
    @Inject
    IChooseWithdrawPresenter<IChooseWithdrawView> presenter;

    private List<ChooseWithdrawAdapter.Item> items = new ArrayList<>();
    private ChooseWithdrawAdapter adapter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(ChooseWithdrawActivity.this);

        adapter = new ChooseWithdrawAdapter(this, R.layout.item_choose_withdraw, items);
        adapter.setOnItemClickListener(position -> {
            Intent resultIntent = new Intent(ChooseWithdrawActivity.this, WithdrawalActivity.class);
            resultIntent.putExtra(Constant.EXTRA_KEY, items.get(position));
            setResult(RESULT_OK, resultIntent);
            finish();
        });
        adapter.setOnItemLongClickListener(() -> {
            onSuccess("请左滑操作");
        });
        adapter.setOnDeleteListener(position -> {
            try {
                presenter.deleteAccount(items.get(position).getId());
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.wtf(TAG, "数组越界", e);
            } catch (Exception e) {
                Log.wtf(TAG, e);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(this, RecycleViewDivider.VERTICAL_LIST));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        this.items.clear();
        presenter.requestAccounts(PayWay.ALIAPY.getType());
    }

    @OnClick(R.id.tv_add)
    public void addAccount() {
        startActivity(new Intent(this, AddAccountActivity.class));
    }

    @Override
    protected int setTitle() {
        return R.string.choose_alipay_account;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_choose_withdraw;
    }

    @Override
    public void addMore(List<ChooseWithdrawAdapter.Item> items) {
        this.items.addAll(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void refreshList() {
        refresh();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
