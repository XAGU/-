package com.xiaolian.amigo.ui.base;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.widget.pageloader.LoadMoreItemCreator;
import com.xiaolian.amigo.ui.widget.pageloader.PageLoader;

import butterknife.BindView;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 含有List的Activity继承此Activity
 * @author zcd
 */

public abstract class BaseListActivity extends BaseActivity implements PageLoader.Callbacks {


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refreshLayout)
    BGARefreshLayout mRefreshLayout;

    protected int threshold = 5;
    protected boolean loading = false;
    protected int page = 0;

    private PageLoader pageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRecyclerView();
        if (enableLoadMore()) {
            setupPageLoader();
        }
    }

    protected void setupPageLoader() {
        if (pageLoader != null) {
            pageLoader.unbind();
        }

        loading = false;
        page = 0;
        pageLoader = PageLoader.with(mRecyclerView, this)
                .setLoadMoreItemCreator(getLoadMoreItemCreator())
                .showLoadMoreItem(getShowLoadMoreItem())
                .setVisibleThreshold(getVisibleThreshold())
                .build();

    }

    private void initRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(getAdapter());
    }

    @Override
    protected void setUp() {

    }

    protected abstract RecyclerView.Adapter getAdapter();

    protected LoadMoreItemCreator getLoadMoreItemCreator() {
        return null;
    }

    /**
     * 子类重写设置是否显示上拉加载View 默认显示
     * @return 是否显示上拉加载View
     */
    protected boolean getShowLoadMoreItem() {
        return true;
    }

    /**
     * 子类重写设置是否开启上拉加载功能 默认开启
     * @return 是否开启上拉加载功能
     */
    protected boolean enableLoadMore() {
        return true;
    }

    /**
     * 子类重写设置visibleThreshold 默认为5
     * @return visibleThreshold
     */
    protected int getVisibleThreshold() {
        return 5;
    }
}
