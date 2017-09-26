package com.xiaolian.amigo.ui.base;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.base.intf.IBaseListView;
import com.xiaolian.amigo.ui.widget.recyclerview.IRecyclerView;
import com.xiaolian.amigo.util.Constant;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 统一添加toolbar的ListActivity
 * 未完成
 * <p>
 * Created by zcd on 9/25/17.
 */

public abstract class BaseToolBarListActivity extends BaseActivity implements IBaseListView {

    private IRecyclerView recyclerView;
    private BGARefreshLayout mRefreshLayout;
    private LinearLayout ll_footer;
    private LinearLayout ll_header;
    private RelativeLayout rl_empty;
    private CoordinatorLayout cl_main;
    private TextView tv_toolbar_title;
    private TextView tv_toolbar_title2;
    private View v_divide;
    private TextView tv_toolbar_sub_title;
    protected int page = Constant.PAGE_START_NUM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_toolbar_list);
        recyclerView = (IRecyclerView) findViewById(R.id.recyclerView);
        cl_main = (CoordinatorLayout) findViewById(R.id.cl_main);
        rl_empty = (RelativeLayout) findViewById(R.id.rl_empty);
        initToolBar();
        initFooter();
        initRecyclerView();
        initInject();
        initView();
    }

    private void initRecyclerView() {
        setRecyclerView(recyclerView);
        recyclerView.setEmptyView(rl_empty);
        recyclerView.setLoadingListener(new IRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                BaseToolBarListActivity.this.onRefresh();
            }

            @Override
            public void onLoadMore() {
                BaseToolBarListActivity.this.onLoadMore();
            }
        });
    }

    protected IRecyclerView getRecyclerView() {
        return recyclerView;
    }

    protected abstract void onRefresh();

    protected abstract void onLoadMore();

    private void initFooter() {
        ll_footer = (LinearLayout) findViewById(R.id.ll_footer);
        if (setFooterLayout() > 0) {
            View layout = LayoutInflater.from(this).inflate(setFooterLayout(), null, true);
            ll_footer.addView(layout);
            ll_footer.setVisibility(View.VISIBLE);
        }
    }

    protected LinearLayout getFooter() {
        return ll_footer;
    }

    protected @LayoutRes int setFooterLayout() {
        return 0;
    }

    private void initToolBar() {
        ll_header = (LinearLayout) findViewById(R.id.ll_header);
        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title2 = (TextView) findViewById(R.id.tv_toolbar_title2);
        tv_toolbar_sub_title = (TextView) findViewById(R.id.tv_toolbar_sub_title);
        v_divide = findViewById(R.id.v_divide);
        setToolBarTitle(setTitle());
        if (setSubTitle() > 0) {
            tv_toolbar_sub_title.setVisibility(View.VISIBLE);
            setToolBarSubTitle(setSubTitle());
        }
        if (setTitle2() > 0) {
            tv_toolbar_title2.setVisibility(View.VISIBLE);
            v_divide.setVisibility(View.VISIBLE);
            tv_toolbar_title2.setText(setTitle2());
        }
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
    }

    protected void setToolBarTitle(@StringRes int res) {
        tv_toolbar_title.setText(res);
    }

    protected void setHeaderBackground(@ColorRes int color) {
        ll_header.setBackgroundResource(color);
    }

    protected void setMainBackground(@ColorRes int color) {
        cl_main.setBackgroundResource(color);
    }

    protected void setToolBarSubTitle(String subTitle) {
        tv_toolbar_sub_title.setText(subTitle);
    }

    protected void setToolBarSubTitle(@StringRes int res) {
        tv_toolbar_sub_title.setText(res);
    }

    protected abstract void initView();

    protected abstract void initInject();

    @Override
    public void addPage() {
        page ++;
    }

    @Override
    public void setLoadMoreComplete() {
        getRecyclerView().loadMoreComplete();
    }

    @Override
    public void setRefreshComplete() {
        getRecyclerView().refreshComplete();
    }
}
