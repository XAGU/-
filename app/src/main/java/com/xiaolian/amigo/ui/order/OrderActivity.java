package com.xiaolian.amigo.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ObjectsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.ui.order.adaptor.OrderAdaptor;
import com.xiaolian.amigo.ui.order.intf.IOrderPresenter;
import com.xiaolian.amigo.ui.order.intf.IOrderView;
import com.xiaolian.amigo.ui.wallet.WalletConstant;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 订单列表
 *
 * @author caidong
 * @date 17/9/14
 */
public class OrderActivity extends OrderBaseListActivity implements IOrderView {
    static final int INVALID_INT = -1;

    @Inject
    IOrderPresenter<IOrderView> presenter;
    /**
     * 订单列表
     */
    List<OrderAdaptor.OrderWrapper> orders = new ArrayList<>();
    /**
     * 订单列表recycleView适配器
     */
    OrderAdaptor adaptor;


    private boolean refreshFlag = false;

    private Integer deviceType;
    private Integer year;
    private Integer month;
    private Integer action;
    private Integer orderStatus;


    @Override
    public void addMore(List<OrderAdaptor.OrderWrapper> orders) {
        if (refreshFlag) {
            refreshFlag = false;
            this.orders.clear();
        }
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
    protected void setUp() {
        super.setUp();
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra(Constant.DATA_BUNDLE);
            if (bundle == null) {
                return;
            }
            year = bundle.getInt(WalletConstant.KEY_YEAR, INVALID_INT);
            month = bundle.getInt(WalletConstant.KEY_MONTH,  INVALID_INT);
            deviceType = bundle.getInt(WalletConstant.KEY_DEVICE_TYPE, INVALID_INT);
            action = bundle.getInt(WalletConstant.KEY_MAX_ORDER, INVALID_INT);
            orderStatus = bundle.getInt(WalletConstant.KEY_ORDER_STATUS, 2);
        }
    }

    @Override
    protected void onRefresh() {
        page = Constant.PAGE_START_NUM;
        presenter.requestOrders(Constant.PAGE_START_NUM, deviceType, year, month, action, orderStatus);
        refreshFlag = true;
//        orders.clear();
    }

    @Override
    public void onLoadMore() {
        presenter.requestOrders(page, deviceType, year, month, action, orderStatus);
    }

    @Override
    protected void setRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptor = new OrderAdaptor(orders);
        adaptor.setOrderDetailClickListener((order) -> {
            if (Device.getDevice(order.getDeviceType()) == Device.WASHER || Device.getDevice(order.getDeviceType())==Device.DRYER2) {
                startActivity(new Intent(OrderActivity.this, NormalOrderActivity.class)
                        .putExtra(OrderConstant.KEY_ORDER_ID, order.getId()));
            } else {
                // 跳转至订单详情
                Intent intent = new Intent(OrderActivity.this, OrderDetailActivity.class);
                intent.putExtra(Constant.EXTRA_KEY, order.getId());
                startActivity(intent);
            }
        });
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
        String title = "";
        if (year != null
                && year != INVALID_INT
                && month != null
                && month != INVALID_INT) {
            title = ""
                    .concat(String.valueOf(year))
                    .concat("年")
                    .concat(String.valueOf(month))
                    .concat("月");
        }
        if (deviceType != null
                && deviceType != INVALID_INT) {
            title = title.concat(Device.getDevice(deviceType).getDesc());
        }
        if (ObjectsCompat.equals(action, WalletConstant.ACTION_MAX_ORDER)) {
            title = title.concat("单笔最贵");
            enableLoadMore(false);
        }
        setToolBarTitle(title.concat("消费记录"));
    }

    @Override
    public void showErrorView() {
        super.showErrorView();
        orders.clear();
        adaptor.notifyDataSetChanged();
    }
}
