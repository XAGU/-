package com.xiaolian.amigo.ui.order;

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

import butterknife.ButterKnife;

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

//    @Override
//    protected RecyclerView.Adapter getAdaptor() {
//        adaptor = new OrderAdaptor(orders);
//        return adaptor;
//    }
//
//    @Override
//    protected int getLayout() {
//        return R.layout.activity_order;
//    }
//
//    @Override
//    protected void initPresenter() {
//
//    }

    @Override
    public void addMore(List<OrderAdaptor.OrderWrapper> orders) {
        this.orders.addAll(orders);
        adaptor.notifyDataSetChanged();
    }

    @Override
    public void setLoadMoreComplete() {
        getRecyclerView().loadMoreComplete();
    }

    @Override
    public void setRefreshComplete() {
        getRecyclerView().refreshComplete();
    }

    @Override
    public void addPage() {
        page ++;
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        presenter.clearObservers();
        orders.clear();
        super.onDestroy();
    }

//    @Override
//    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
//    }
//
//    @Override
//    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
//        return false;
//    }

    @Override
    protected void onRefresh() {
        page = 1;
        presenter.requestOrders(Constant.PAGE_START_NUM);
        orders.clear();
    }

    @Override
    public void onLoadMore() {
        presenter.requestOrders(page);
    }

    @Override
    protected void setRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptor = new OrderAdaptor(orders);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 14)));
        recyclerView.setAdapter(adaptor);
    }

    @Override
    protected int setTitle() {
        return R.string.order_record;
    }

    @Override
    protected void initView() {

        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(OrderActivity.this);
        presenter.requestOrders(page);
    }
}
