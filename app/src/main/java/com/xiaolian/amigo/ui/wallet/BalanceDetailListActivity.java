package com.xiaolian.amigo.ui.wallet;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.xiaolian.amigo.R;

import com.xiaolian.amigo.ui.base.BaseActivity;
import com.xiaolian.amigo.ui.wallet.intf.IBalanceDetailListView;
import com.xiaolian.amigo.ui.wallet.intf.IBalanceDetailListPresenter;

import javax.inject.Inject;

public class BalanceDetailListActivity extends BalanceDetailListBaseActivity implements IBalanceDetailListView {

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
        setContentView(R.layout.activity_balance_detail_list);
        getActivityComponent().inject(this);
        presenter.onAttach(this);

        leftTitle = findViewById(R.id.tv_title);
        rightTitle = findViewById(R.id.tv_title2);
        iv_back = findViewById(R.id.iv_back);

        iv_back.setOnClickListener(v -> back());
        leftTitle.setOnClickListener(v -> showBalanceDetailListView());
        rightTitle.setOnClickListener(v -> showStatisticsView());

        balanceListFragment = new BalanceListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.ll_main, balanceListFragment).commit();
    }

    //返回上一个页面
    private void back() {
        this.finish();
    }

    //点击左边的按钮展示账单
    private void showBalanceDetailListView() {
        leftTitle.setTextColor(Color.parseColor("#222222"));
        rightTitle.setTextColor(Color.parseColor("#bbbbbb"));
    }

    //点击右边的按钮展示统计
    private void showStatisticsView() {
        rightTitle.setTextColor(Color.parseColor("#222222"));
        leftTitle.setTextColor(Color.parseColor("#bbbbbb"));
    }



//    @Override
//    protected void initView() {
//        setUnBinder(ButterKnife.bind(this));
//        getActivityComponent().inject(this);
//        presenter.onAttach(BalanceDetailListActivity.this);
//    }
//
//
//    @Override
//    protected int setTitle() {
//        return 0;
//    }
//
//    @Override
//    protected int setLayout() {
//        return R.layout.activity_balance_detail_list;
//    }
}
