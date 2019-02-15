package com.xiaolian.amigo.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ObjectsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.ui.order.adaptor.OrderAdaptor;
import com.xiaolian.amigo.ui.order.intf.IOrderPresenter;
import com.xiaolian.amigo.ui.order.intf.IOrderView;
import com.xiaolian.amigo.ui.wallet.WalletConstant;
import com.xiaolian.amigo.ui.wallet.adaptor.BillListAdaptor;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

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
    BillListAdaptor adaptor;

    private List<BillListAdaptor.BillListAdaptorWrapper> items = new ArrayList<>();


    private boolean refreshFlag = false;

    private Integer deviceType;
    private Integer year;
    private Integer month;
    private Integer action;
    private Integer orderStatus;


    @Override
    public void addMore(List<BillListAdaptor.BillListAdaptorWrapper> wrappers) {
        if (refreshFlag) {
            refreshFlag = false;
            this.items.clear();
        }
        this.items.addAll(wrappers);
        adaptor.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        presenter.clearObservers();
        items.clear();
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
        adaptor = new BillListAdaptor(this, R.layout.item_balance_detail_record, items);
        adaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                BillListAdaptor.BillListAdaptorWrapper item = items.get(position);
                String orderName = getDeviceNameWithType(item.getType());
                if (Device.getDevice(item.getType()) == Device.WASHER || Device.getDevice(item.getType())==Device.DRYER2) {
                    startActivity(new Intent(OrderActivity.this, NormalOrderActivity.class)
                            .putExtra(OrderConstant.KEY_ORDER_ID, item.getId())
                            .putExtra(OrderConstant.KEY_ORDER_TITLE, orderName));
                } else {
                    // 跳转至订单详情
                    Intent intent = new Intent(OrderActivity.this, OrderDetailActivity.class);
                    intent.putExtra(Constant.EXTRA_KEY, item.getId());
                    intent.putExtra(OrderConstant.KEY_ORDER_TITLE, orderName);
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
//        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 14)));
        recyclerView.setAdapter(adaptor);
    }

    private String getDeviceNameWithType(int type) {
//        1 - 热水澡， 2 - 饮水机，3 - 吹风机，4 - 洗衣机，
        if (type == 1) {
            return "热水澡消费";
        } else if (type == 2) {
            return "饮水机消费";
        } else if (type == 3) {
            return "吹风机消费";
        } else if (type == 4) {
            return "洗衣机消费";
        }
        return "烘干机消费";
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
            title = title.concat("消费");
        }
        if (orderStatus == 3) {
            title = title.concat("实际消费");
        } else if (orderStatus == 4) {
            title = title.concat("消费退款");
        }
        if (ObjectsCompat.equals(action, WalletConstant.ACTION_MAX_ORDER)) {
            title = title.concat("单笔最贵消费");
            enableLoadMore(false);
        }
        setToolBarTitle(title);
    }

    @Override
    public void showErrorView() {
        super.showErrorView();
        orders.clear();
        adaptor.notifyDataSetChanged();
    }
}
