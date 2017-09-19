package com.xiaolian.amigo.ui.base;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.base.intf.IBaseListView;
import com.xiaolian.amigo.ui.widget.pageloader.LoadMoreItemCreator;
import com.xiaolian.amigo.ui.widget.pageloader.PageLoader;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;

/**
 * 含有List的Activity继承此Activity
 * @author zcd
 */

public abstract class BaseListActivity extends BaseActivity
        implements PageLoader.Callbacks, BGARefreshLayout.BGARefreshLayoutDelegate, IBaseListView{


    protected RecyclerView mRecyclerView;

    protected BGARefreshLayout mRefreshLayout;

    protected int threshold = 5;
    protected boolean loading = false;
    protected boolean refreshing = false;
    protected int page = 1;
    protected boolean hasLoadedAll = false;

    private PageLoader pageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.refreshLayout);
        initInject();
        initPresenter();
        initRecyclerView();
        if (enableLoadMore()) {
            setupPageLoader();
        }
        initRefreshLayout();
        initData();
    }

    protected abstract void initData();

    protected abstract void initInject();

    protected abstract void initPresenter();

    private void initRefreshLayout() {
        BGAStickinessRefreshViewHolder stickinessRefreshViewHolder = new BGAStickinessRefreshViewHolder(this, false);
        stickinessRefreshViewHolder.setStickinessColor(R.color.colorPrimary);
        stickinessRefreshViewHolder.setRotateImage(R.drawable.default_avatar);
        mRefreshLayout.setRefreshViewHolder(stickinessRefreshViewHolder);

//        mRefreshLayout.setPullDownRefreshEnable(false);

        mRefreshLayout.setDelegate(this);
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public boolean isRefreshing() {
        return refreshing;
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        this.refreshing = refreshing;
        if (!refreshing) {
            mRefreshLayout.endRefreshing();
        }
    }

    @Override
    public boolean hasLoadedAll() {
        return hasLoadedAll;
    }

    @Override
    public void setLoadAll(boolean hasLoadedAll) {
        this.hasLoadedAll = hasLoadedAll;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    protected void setupPageLoader() {
        if (pageLoader != null) {
            pageLoader.unbind();
        }

        loading = false;
        pageLoader = PageLoader.with(mRecyclerView, this)
                .setLoadMoreItemCreator(getLoadMoreItemCreator())
                .showLoadMoreItem(getShowLoadMoreItem())
                .setVisibleThreshold(getVisibleThreshold())
                .build();
        hideLoadMoreView();

    }

    private void initRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(getAdaptor());
    }

    @Override
    protected void setUp() {

    }
    @Override
    public void showLoadMoreView() {
        if (pageLoader != null) {
            pageLoader.showLoadMoreView();
        }
    }

    @Override
    public void hideLoadMoreView() {
        if (pageLoader != null) {
            pageLoader.hideLoadMoreView();
        }
    }

    @Override
    public void showNoMoreDataView() {
        if (pageLoader != null) {
            pageLoader.showNoMoreDataView();
        }
    }

    @Override
    public void showLoading() {
        if (!isRefreshing()) {
            super.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    public void addPage() {
        page ++;
    }

    @Override
    public void loadStart() {
        loading = true;
    }

    @Override
    public void loadComplete() {
        loading = false;
    }

    protected abstract RecyclerView.Adapter getAdaptor();

    protected abstract int getLayout();

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
