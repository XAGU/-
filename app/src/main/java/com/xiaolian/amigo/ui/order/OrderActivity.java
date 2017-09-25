package com.xiaolian.amigo.ui.order;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.common.config.SpaceItemDecoration;
import com.xiaolian.amigo.tmp.common.util.ScreenUtils;
import com.xiaolian.amigo.ui.order.adaptor.OrderAdaptor;
import com.xiaolian.amigo.ui.order.intf.IOrderPresenter;
import com.xiaolian.amigo.ui.order.intf.IOrderView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 订单列表
 * <p>
 * Created by caidong on 2017/9/14.
 */
public class OrderActivity extends OrderBaseListActivity implements IOrderView {

    @Inject
    IOrderPresenter<IOrderView> presenter;
    // 订单列表
    List<OrderAdaptor.OrderWrapper> orders = new ArrayList<OrderAdaptor.OrderWrapper>();
    // 订单列表recycleView适配器
    OrderAdaptor adaptor;

    @Override
    protected void setUp() {

    }

    @Override
    protected RecyclerView.Adapter getAdaptor() {
        adaptor = new OrderAdaptor(orders);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 14)));
        return adaptor;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_order;
    }

    @Override
    protected void initPresenter() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(OrderActivity.this);

    }

    @Override
    public void addMore(List<OrderAdaptor.OrderWrapper> orders) {
        this.orders.addAll(orders);
        adaptor.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        presenter.clearObservers();
        orders.clear();
        super.onDestroy();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 1;
        presenter.requestOrders(Constant.PAGE_START_NUM);
        orders.clear();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void onLoadMore() {
        presenter.requestOrders(page);
    }
}
