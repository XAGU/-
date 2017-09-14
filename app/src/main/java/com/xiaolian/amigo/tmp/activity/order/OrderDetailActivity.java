package com.xiaolian.amigo.tmp.activity.order;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.activity.order.adaptor.OrderAdaptor;
import com.xiaolian.amigo.tmp.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 消费记录
 * <p>
 * Created by caidong on 2017/9/8.
 */
public class OrderDetailActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);

    }
}
