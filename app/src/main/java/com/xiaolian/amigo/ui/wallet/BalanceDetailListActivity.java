package com.xiaolian.amigo.ui.wallet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.R;

import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.enumeration.WithdrawOperationType;
import com.xiaolian.amigo.data.network.model.order.Order;
import com.xiaolian.amigo.data.network.model.userbill.UserMonthlyBillRespDTO;
import com.xiaolian.amigo.di.componet.BalanceDetailListActivityComponent;
import com.xiaolian.amigo.di.module.BalanceDetailListActivityModule;
import com.xiaolian.amigo.di.componet.DaggerBalanceDetailListActivityComponent;
import com.xiaolian.amigo.ui.base.BaseActivity;
import com.xiaolian.amigo.ui.login.RegisterStep1Fragment;
import com.xiaolian.amigo.ui.order.NormalOrderActivity;
import com.xiaolian.amigo.ui.order.OrderActivity;
import com.xiaolian.amigo.ui.order.OrderConstant;
import com.xiaolian.amigo.ui.order.OrderDetailActivity;
import com.xiaolian.amigo.ui.wallet.adaptor.BillListAdaptor;
import com.xiaolian.amigo.ui.wallet.adaptor.PrepayAdaptor;
import com.xiaolian.amigo.ui.wallet.intf.IBalanceDetailListView;
import com.xiaolian.amigo.ui.wallet.intf.IBalanceDetailListPresenter;
import com.xiaolian.amigo.util.Constant;

import java.util.List;

import javax.inject.Inject;

public class BalanceDetailListActivity extends BaseActivity implements IBalanceDetailListView {

    public static final int REQUEST_CODE_DETAIL = 0x1201;

    private static final String FRAGMENT_TAG_LIST_DETAIL = "listdetail";

    private static final String FRAGMENT_TAG_LIST_STATISTICS = "statistics";

    private BalanceDetailListActivityComponent mActivityComponent;

    @Inject
    IBalanceDetailListPresenter<IBalanceDetailListView> presenter;

    //账单按钮
    TextView leftTitle;

    //统计按钮
    TextView rightTitle;

    //返回按钮
    ImageView iv_back;

    //账单内容页面
    BalanceListFragment balanceListFragment;

    //统计内容页面
    BalanceStatisticsFragment balanceStatisticsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponent = DaggerBalanceDetailListActivityComponent.builder()
                .balanceDetailListActivityModule(new BalanceDetailListActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();

        setContentView(R.layout.activity_balance_detail_list);
        getActivityComponent().inject(this);
        presenter.onAttach(this);

        leftTitle = findViewById(R.id.tv_title);
        rightTitle = findViewById(R.id.tv_title2);
        iv_back = findViewById(R.id.iv_back);

        iv_back.setOnClickListener(v -> back());
        leftTitle.setOnClickListener(v -> showBalanceDetailListView());
        rightTitle.setOnClickListener(v -> showStatisticsView());

        showBalanceDetailListView(); //默认显示账单页面
    }

    @Override
    protected void setUp() {

    }

    public BalanceDetailListActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    //返回上一个页面
    private void back() {
        this.finish();
    }

    //跳转到充值提现
    public void gotoBillRechargeWithdrawActivity(int type, Long id) {
        startActivityForResult(new Intent(BalanceDetailListActivity.this,
                        WithdrawOperationType.getOperationType(type)
                                .getClz())
                        .putExtra(Constant.EXTRA_KEY, id),
                REQUEST_CODE_DETAIL);
    }

    public void gotoBillDetailActivity(int type, Long id, int status) {

        if (status == 100) /*预付未找零*/{
            startActivity(new Intent(BalanceDetailListActivity.this, PrepayOrderActivity.class)
                    .putExtra(OrderConstant.KEY_ORDER_ID, id)
                    .putExtra(OrderConstant.KEY_ORDER_TITLE, BillListAdaptor.getTypeName(type, status)));
        } else if (Device.getDevice(type) == Device.WASHER || Device.getDevice(type)==Device.DRYER2) {
            startActivity(new Intent(BalanceDetailListActivity.this, NormalOrderActivity.class)
                    .putExtra(OrderConstant.KEY_ORDER_ID, id)
                    .putExtra(OrderConstant.KEY_ORDER_TITLE, BillListAdaptor.getTypeName(type, status)));
        } else {
            // 跳转至订单详情
            Intent intent = new Intent(BalanceDetailListActivity.this, OrderDetailActivity.class);
            intent.putExtra(Constant.EXTRA_KEY, id);
            intent.putExtra(OrderConstant.KEY_ORDER_TITLE, BillListAdaptor.getTypeName(type, status));
            startActivity(intent);
        }
    }


        //点击左边的按钮展示账单
    private void showBalanceDetailListView() {
        leftTitle.setTextColor(Color.parseColor("#222222"));
        rightTitle.setTextColor(Color.parseColor("#bbbbbb"));
        if (balanceListFragment == null) {
            balanceListFragment = new BalanceListFragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (balanceStatisticsFragment != null && balanceStatisticsFragment.isAdded()) {
            transaction = transaction.hide(balanceStatisticsFragment);
        }
        if (!balanceListFragment.isAdded()) {
            transaction.add(R.id.ll_main, balanceListFragment, FRAGMENT_TAG_LIST_DETAIL).commit();
        } else {
            transaction.show(balanceListFragment).commit();
        }
    }

    //点击右边的按钮展示统计
    private void showStatisticsView() {
        rightTitle.setTextColor(Color.parseColor("#222222"));
        leftTitle.setTextColor(Color.parseColor("#bbbbbb"));
        if (balanceStatisticsFragment == null) {
            balanceStatisticsFragment = new BalanceStatisticsFragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (balanceListFragment != null && balanceListFragment.isAdded()) {
            transaction = transaction.hide(balanceListFragment);
        }
        if (!balanceStatisticsFragment.isAdded()) {
            transaction.add(R.id.ll_main, balanceStatisticsFragment, FRAGMENT_TAG_LIST_STATISTICS).commit();
        } else {
            transaction.show(balanceStatisticsFragment).commit();
        }
    }

    @Override
    public void addMore(List<BillListAdaptor.BillListAdaptorWrapper> wrappers) {
        balanceListFragment.addMore(wrappers);
    }

    @Override
    public void render(UserMonthlyBillRespDTO data) {
        balanceStatisticsFragment.render(data);
    }

    @Override
    public void setLoadMoreComplete() {
        balanceListFragment.setLoadMoreComplete();
    }

    @Override
    public void setRefreshComplete() {
        balanceListFragment.setRefreshComplete();
    }

    @Override
    public void showEmptyView() {
        balanceListFragment.showEmptyView();
    }

    @Override
    public void showEmptyView(String tip, int colorRes) {
        balanceListFragment.showEmptyView(tip, colorRes);
    }

    @Override
    public void showEmptyView(int tipRes, int colorRes) {
        balanceListFragment.showEmptyView(tipRes, colorRes);
    }

    @Override
    public void showEmptyView(int tipRes) {
        balanceListFragment.showEmptyView(tipRes);
    }

    @Override
    public void hideEmptyView() {
        balanceListFragment.hideEmptyView();
    }

    @Override
    public void showErrorView() {
        balanceListFragment.showErrorView();
    }

    @Override
    public void showErrorView(int colorRes) {
        balanceListFragment.showErrorView(colorRes);
    }

    @Override
    public void hideErrorView() {
        balanceListFragment.hideErrorView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_DETAIL) {
            balanceListFragment.onRefresh();
        }
    }
}



