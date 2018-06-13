package com.xiaolian.amigo.ui.base;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.base.intf.IBaseListView;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutFooter;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutHeader;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;

/**
 * 统一添加toolbar的ListActivity
 *
 * @author zcd
 * @date 17/9/25
 */

public abstract class BaseToolBarListActivity extends BaseActivity implements IBaseListView {

    private RecyclerView recyclerView;
    private LinearLayout llFooter;
    private LinearLayout llHeader;
    private RelativeLayout rlEmpty;
    private TextView tvEmptyTip;
    private RelativeLayout rlError;
    private AppBarLayout appBarLayout;
    private CoordinatorLayout clMain;
    private TextView tv_toolbar_title;
    private TextView tv_toolbar_title2;
    private View v_divide;
    private TextView tv_toolbar_sub_title;
    private Toolbar toolBar;
    //toolbar
    protected TextView tvTitle,tvTitleSecond,tvTitleThird;
    private View viewLine;

    protected int page = Constant.PAGE_START_NUM;
    private SmartRefreshLayout refreshLayout;
    private boolean autoRefresh = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_toolbar_list);
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        appBarLayout = findViewById(R.id.app_bar_layout);
        clMain = findViewById(R.id.cl_main);
        rlEmpty = findViewById(R.id.rl_empty);
        tvEmptyTip = findViewById(R.id.tv_empty_tip);
        rlError = findViewById(R.id.rl_error);
        toolBar = findViewById(R.id.tool_bar);
        setUp();
        initToolBar();
        initFooter();
        initRecyclerView();
        initInject();
        initView();

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            //Log.d("STATE", appBarLayout.getTotalScrollRange() +"//"+ verticalOffset+"//"+tv_toolbar_title.getHeight());
            if (verticalOffset < -(tv_toolbar_title.getHeight() + llHeader.getPaddingTop())) {
                tvTitle.setVisibility(View.VISIBLE);
                tvTitleSecond.setVisibility(View.VISIBLE);
                tvTitleThird.setVisibility(View.VISIBLE);
                viewLine.setVisibility(View.VISIBLE);
            } else {
                tvTitle.setVisibility(View.GONE);
                tvTitleSecond.setVisibility(View.GONE);
                tvTitleThird.setVisibility(View.GONE);
                viewLine.setVisibility(View.GONE);
            }
        });

    }

    private void initRecyclerView() {
        setRecyclerView(recyclerView);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                BaseToolBarListActivity.this.onLoadMore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                BaseToolBarListActivity.this.onRefresh();
            }
        });
        refreshLayout.setRefreshHeader(new RefreshLayoutHeader(this));
        refreshLayout.setRefreshFooter(new RefreshLayoutFooter(this));
        refreshLayout.setReboundDuration(200);
        if (autoRefresh) {
            refreshLayout.autoRefresh(20);
        }
    }

    protected void enableLoadMore(boolean enable) {
        refreshLayout.setEnableLoadMore(enable);
    }

    protected void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    protected SmartRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    protected RecyclerView getRecyclerView() {
        return recyclerView;
    }

    protected abstract void onRefresh();

    protected abstract void onLoadMore();

    private void initFooter() {
        llFooter = findViewById(R.id.ll_footer);
        if (setFooterLayout() > 0) {
            View layout = LayoutInflater.from(this).inflate(setFooterLayout(), null, true);
            llFooter.addView(layout);
            llFooter.setVisibility(View.VISIBLE);
        }
        setFooter();
    }

    protected void setFooter() {
        // do nth
    }

    protected LinearLayout getFooter() {
        return llFooter;
    }

    protected @LayoutRes
    int setFooterLayout() {
        return 0;
    }

    private void initToolBar() {
        llHeader = findViewById(R.id.ll_header);
        tv_toolbar_title = findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title2 = findViewById(R.id.tv_toolbar_title2);
        tv_toolbar_sub_title = findViewById(R.id.tv_toolbar_sub_title);
        v_divide = findViewById(R.id.v_divide);
        //toolbar
        tvTitle = findViewById(R.id.tv_title);
        tvTitleSecond = findViewById(R.id.tv_title_second);
        tvTitleThird = findViewById(R.id.tv_title_third);
        viewLine = findViewById(R.id.view_line);

        setToolBarTitle(setTitle());
        if (setSubTitle() > 0) {
            tv_toolbar_sub_title.setVisibility(View.VISIBLE);
            setToolBarSubTitle(setSubTitle());
        }
        if (setTitle2() > 0) {
            tv_toolbar_title2.setVisibility(View.VISIBLE);
            v_divide.setVisibility(View.VISIBLE);
            tv_toolbar_title2.setText(setTitle2());

            //tvTitleSecond.setVisibility(View.VISIBLE);
            tvTitleSecond.setText(setTitle2());
        }
    }

    protected void setToolbarBackgroundColor(@ColorRes int res) {
        toolBar.setBackgroundResource(res);
    }

    protected int setTitle2() {
        return 0;
    }

    protected abstract void setRecyclerView(RecyclerView recyclerView);

    protected abstract int setTitle();

    protected TextView getToolBarTitle() {
        return tv_toolbar_title;
    }

    protected TextView getToolBarTitle2() {
        return tv_toolbar_title2;
    }

    protected int setSubTitle() {
        return 0;
    }

    protected TextView getSubTitle() {
        return tv_toolbar_sub_title;
    }

    protected void setToolBarTitle(String title) {
        tv_toolbar_title.setText(title);
        tvTitle.setText(title);
    }

    protected void setToolBarTitle(@StringRes int res) {
        tv_toolbar_title.setText(res);
        tvTitle.setText(res);
    }

    protected void setHeaderBackground(@ColorRes int color) {
        llHeader.setBackgroundResource(color);
    }

    protected void setRefreshLayoutMargin(int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams marginLayoutParams =
                (ViewGroup.MarginLayoutParams) refreshLayout.getLayoutParams();
        marginLayoutParams.setMargins(left, top, right, bottom);
        refreshLayout.setLayoutParams(marginLayoutParams);
    }

    protected void setRecyclerViewMargin(int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams marginLayoutParams =
                (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
        marginLayoutParams.setMargins(left, top, right, bottom);
        recyclerView.setLayoutParams(marginLayoutParams);
    }

    protected void setMainBackground(@ColorRes int color) {
        clMain.setBackgroundResource(color);
    }

    protected void setToolBarSubTitle(String subTitle) {
        tv_toolbar_sub_title.setText(subTitle);
        tvTitleThird.setText(subTitle);
    }

    protected void setToolBarSubTitle(@StringRes int res) {
        tv_toolbar_sub_title.setText(res);
        tvTitleThird.setText(res);
    }

    protected abstract void initView();

    protected abstract void initInject();

    @Override
    public void addPage() {
        page++;
    }

    @Override
    public void setLoadMoreComplete() {
        refreshLayout.finishLoadmore();
    }

    @Override
    public void setRefreshComplete() {
        refreshLayout.finishRefresh(300);
    }

    @Override
    protected void setUp() {

    }

    @Override
    public void showEmptyView() {
        showEmptyView(getString(R.string.empty_tip), R.color.colorBackgroundGray);
    }

    @Override
    public void showEmptyView(int tipRes, int colorRes) {
        showEmptyView(getString(tipRes), colorRes);
    }

    @Override
    public void showEmptyView(int tipRes) {
        showEmptyView(tipRes, R.color.colorBackgroundGray);
    }

    @Override
    public void showEmptyView(String tip, int colorRes) {
        if (page == Constant.PAGE_START_NUM) {
            rlEmpty.setVisibility(View.VISIBLE);
            rlEmpty.setBackgroundResource(colorRes);
            tvEmptyTip.setText(tip);
        }
    }

    @Override
    public void hideEmptyView() {
        rlEmpty.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView() {
        showErrorView(R.color.colorBackgroundGray);
    }

    @Override
    public void showErrorView(int colorRes) {
        rlError.setVisibility(View.VISIBLE);
        rlError.setBackgroundResource(colorRes);
    }

    @Override
    public void hideErrorView() {
        rlError.setVisibility(View.GONE);
    }

}
