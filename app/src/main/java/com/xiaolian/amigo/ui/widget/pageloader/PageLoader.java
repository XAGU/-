package com.xiaolian.amigo.ui.widget.pageloader;

import android.support.v7.widget.RecyclerView;

/**
 * 上拉加载控件
 * </p>
 * 现在只支持RecyclerView，并且LayoutManager必须为LinearLayoutManager
 *
 * @author zcd
 */
public abstract class PageLoader {
    private boolean isLoadMore = false;
    private boolean hasMore = true;

    public interface Callbacks {

        void onLoadMore();

    }

    public abstract void setHasDataToLoad(boolean hasDataToLoad);

    public abstract void unbind();

    public abstract void showLoadMoreView();

    public abstract void showNoMoreDataView();

    public abstract void hideLoadMoreView();

    public boolean isLoadMore() {
        return isLoadMore;
    }

    public void setLoadMore(boolean loadMore) {
        isLoadMore = loadMore;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public static RecyclerPageLoader.Builder with(RecyclerView recyclerView, Callbacks callbacks) {
        return new RecyclerPageLoader.Builder(recyclerView, callbacks);
    }


}
