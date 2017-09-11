package com.xiaolian.amigo.activity.order;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.activity.BaseActivity;
import com.xiaolian.amigo.activity.order.adaptor.OrderAdaptor;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 消费记录
 * <p>
 * Created by caidong on 2017/9/8.
 */
public class OrderActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2 {

    static List<OrderAdaptor.Order> orders = new ArrayList<OrderAdaptor.Order>(5) {
        {
            add(new OrderAdaptor.Order(1, "", "", 1));
            add(new OrderAdaptor.Order(1, "", "", 1));
            add(new OrderAdaptor.Order(1, "", "", 1));
        }
    };

    @BindView(R.id.lv_orders)
    PullToRefreshListView lv_orders;
    @BindView(R.id.ll_header)
    LinearLayout ll_header;

    OrderAdaptor adapter;
    Context context;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    orders.add(0, new OrderAdaptor.Order(1, "", "", 1));
//                    ListViewUtil.setListViewHeightBasedOnChildren(lv_orders.getRefreshableView());
                    adapter.notifyDataSetChanged();
                    lv_orders.onRefreshComplete();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);

        adapter = new OrderAdaptor(this, R.layout.item_order, orders);
        lv_orders.setAdapter(adapter);

        ILoadingLayout loadingLayout = lv_orders.getLoadingLayoutProxy();
        loadingLayout.setLastUpdatedLabel("上次刷新时间");
        loadingLayout.setPullLabel("下拉刷新");
        loadingLayout.setReleaseLabel("松开即可刷新");

        lv_orders.setOnRefreshListener(this);
        // 查询账单详情
        lv_orders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(OrderActivity.this, OrderDetailActivity.class);
            }
        });
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        mHandler.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        mHandler.sendEmptyMessageDelayed(0, 2000);
    }

    // 查询账单详情
//    @OnItemClick(R.id.lv_orders)
//    void queryOrderDetail() {
//        startActivity(this, OrderDetailActivity.class);
//    }
}
