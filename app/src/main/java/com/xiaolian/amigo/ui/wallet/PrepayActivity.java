package com.xiaolian.amigo.ui.wallet;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.wallet.adaptor.PrepayAdaptor;
import com.xiaolian.amigo.ui.wallet.intf.IPrepayPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IPrepayView;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 预付金额
 * <p>
 * Created by zcd on 10/10/17.
 */
public class PrepayActivity extends WalletBaseListActivity implements IPrepayView {

    private List<PrepayAdaptor.OrderWrapper> orders = new ArrayList<>();
    private PrepayAdaptor adaptor;
    @Inject
    IPrepayPresenter<IPrepayView> presenter;

    @Override
    protected void onRefresh() {
        page = 1;
        presenter.requestPrepay(Constant.PAGE_START_NUM);
        orders.clear();
    }

    @Override
    protected void onLoadMore() {
        presenter.requestPrepay(page);
    }

    @Override
    protected void setRecyclerView(RecyclerView recyclerView) {
        adaptor = new PrepayAdaptor(this, R.layout.item_wallet_prepay, orders);
        adaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(new Intent(PrepayActivity.this, PrepayOrderActivity.class)
                                .putExtra(Constant.EXTRA_KEY, orders.get(position)));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 10)));
    }

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(PrepayActivity.this);
    }

    @Override
    protected int setTitle() {
        return R.string.prepay_money;
    }

    @Override
    public void addMore(List<PrepayAdaptor.OrderWrapper> orders) {
        this.orders.addAll(orders);
        adaptor.notifyDataSetChanged();
    }

//    // 查看待找零账单
//    @OnItemClick(lv_prepays)
//    void queryPrepayOrder() {
//        startActivity(this, PrepayOrderActivity.class);
//    }
}
