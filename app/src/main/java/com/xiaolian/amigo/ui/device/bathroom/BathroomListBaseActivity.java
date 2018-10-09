package com.xiaolian.amigo.ui.device.bathroom;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutFooter;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutHeader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zcd
 * @date 18/7/12
 */
public abstract class BathroomListBaseActivity extends BathroomBaseActivity {

    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_toolbar_sub_title)
    TextView tvToolbarSubTitle;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_sub_title)
    TextView tvSubTitle;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.cl_main)
    CoordinatorLayout clMain;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bathroom_list);
        setUnBinder(ButterKnife.bind(this));
        initIntent();

        initView();
    }

    private void initView() {
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset < -(tvToolbarTitle.getHeight() + llHeader.getPaddingTop())) {
                tvTitle.setVisibility(View.VISIBLE);
                tvSubTitle.setVisibility(View.VISIBLE);
                viewLine.setVisibility(View.VISIBLE);
            } else {
                tvTitle.setVisibility(View.GONE);
                tvSubTitle.setVisibility(View.GONE);
                viewLine.setVisibility(View.GONE);
            }
        });
        setTitle();
        initRefreshLayout();
        initRecyclerView();
    }

    private void initRefreshLayout() {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                BathroomListBaseActivity.this.onLoadMore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                BathroomListBaseActivity.this.onRefresh();
            }
        });
        refreshLayout.setRefreshHeader(new RefreshLayoutHeader(this));
        refreshLayout.setRefreshFooter(new RefreshLayoutFooter(this));
        refreshLayout.setReboundDuration(200);
        refreshLayout.autoRefresh(20);
    }

    protected abstract void setTitle();

    protected abstract void initRecyclerView();

    protected abstract void onLoadMore();

    protected abstract void onRefresh();

    protected abstract void initIntent();

}
