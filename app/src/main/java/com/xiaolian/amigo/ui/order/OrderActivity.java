package com.xiaolian.amigo.ui.order;

import android.os.Bundle;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.order.intf.IOrderPresenter;
import com.xiaolian.amigo.ui.order.intf.IOrderView;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by caidong on 2017/9/14.
 */

public class OrderActivity extends OrderBaseActivity implements IOrderView {

    @Inject
    IOrderPresenter<IOrderView> presenter;

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
        presenter.requestNetWork(1);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        presenter.clearObservers();
        super.onDestroy();
    }
}
