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

/**
 * 订单列表
 * <p>
 * Created by caidong on 2017/9/14.
 */
public class OrderActivity extends OrderBaseActivity implements IOrderView {

    @Inject
    IOrderPresenter<IOrderView> presenter;
    @BindView(R.id.rv_orders)
    RecyclerView rv_orders;

    // 订单列表
    List<OrderAdaptor.OrderWrapper> orders = new ArrayList<OrderAdaptor.OrderWrapper>();
    // 订单列表recycleView适配器
    OrderAdaptor adaptor;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(OrderActivity.this);

        adaptor = new OrderAdaptor(orders);
        rv_orders.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 14)));
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_orders.setLayoutManager(manager);
        rv_orders.setAdapter(adaptor);

        presenter.requestOrders(Constant.PAGE_START_NUM);
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
}
