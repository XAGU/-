package com.xiaolian.amigo.ui.device.bathroom;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.bathroom.adapter.BuyCodeRecordAdapter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBuyRecordPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBuyRecordView;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutFooter;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutHeader;
import com.xiaolian.amigo.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 购买记录
 *
 * @author zcd
 * @date 18/6/29
 */
public class BuyRecordActivity extends BathroomBaseActivity implements IBuyRecordView {

    @Inject
    IBuyRecordPresenter<IBuyRecordView> presenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.ll_header)
    LinearLayout llHeader;

    @BindView(R.id.view_line)
    View viewLine;

    private BuyCodeRecordAdapter adapter;

    private List<BuyCodeRecordAdapter.RecordWrapper> records = new ArrayList<BuyCodeRecordAdapter.RecordWrapper>() {
        {
            add(new BuyCodeRecordAdapter.RecordWrapper("1", "2", 1111L, "3"));
            add(new BuyCodeRecordAdapter.RecordWrapper("1", "2", 1111L, "3"));
            add(new BuyCodeRecordAdapter.RecordWrapper("1", "2", 1111L, "3"));
            add(new BuyCodeRecordAdapter.RecordWrapper("1", "2", 1111L, "3"));
            add(new BuyCodeRecordAdapter.RecordWrapper("1", "2", 1111L, "3"));
            add(new BuyCodeRecordAdapter.RecordWrapper("1", "2", 1111L, "3"));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_code_record);
        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);
        presenter.onAttach(this);

        initView();
    }

    private void initView() {
        initRecyclerView();

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            //Log.d("STATE", appBarLayout.getTotalScrollRange() +"//"+ verticalOffset+"//"+tv_toolbar_title.getHeight());
            if (verticalOffset < -(tvToolbarTitle.getHeight() + llHeader.getPaddingTop())) {
                tvTitle.setVisibility(View.VISIBLE);
                viewLine.setVisibility(View.VISIBLE);
            } else {
                tvTitle.setVisibility(View.GONE);
                viewLine.setVisibility(View.GONE);
            }
        });
    }

    private void initRecyclerView() {
        adapter = new BuyCodeRecordAdapter(this, R.layout.item_buy_code_record, records);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 10)));

        refreshLayout.setRefreshHeader(new RefreshLayoutHeader(this));
        refreshLayout.setRefreshFooter(new RefreshLayoutFooter(this));
    }
}
