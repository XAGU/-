package com.xiaolian.amigo.ui.widget.pageloader;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * RecyclerView PageLoader
 * @author zcd
 */
public final class RecyclerPageLoader extends PageLoader {

    private final RecyclerView recyclerView;
    private final Callbacks callbacks;
    private final int visibleThreshold;
    private WrapperAdapter wrapperAdapter;


    private final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            checkLoadMore(dx, dy);
        }
    };

    private final RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            wrapperAdapter.notifyDataSetChanged();
            onAdapterDataChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            wrapperAdapter.notifyItemRangeChanged(positionStart, itemCount);
            onAdapterDataChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            wrapperAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
            onAdapterDataChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            wrapperAdapter.notifyItemRangeInserted(positionStart, itemCount);
            onAdapterDataChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            wrapperAdapter.notifyItemRangeRemoved(positionStart, itemCount);
            onAdapterDataChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            wrapperAdapter.notifyItemMoved(fromPosition, toPosition);
            onAdapterDataChanged();
        }
    };


    RecyclerPageLoader(RecyclerView recyclerView,
                       Callbacks callbacks,
                       int visibleThreshold,
                       boolean showLoadMoreItem,
                       LoadMoreItemCreator loadMoreItemCreator) {
        this.recyclerView = recyclerView;
        this.callbacks = callbacks;
        this.visibleThreshold = visibleThreshold;
        recyclerView.addOnScrollListener(onScrollListener);
        if (showLoadMoreItem) {
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            wrapperAdapter = new WrapperAdapter(adapter, loadMoreItemCreator);
            adapter.registerAdapterDataObserver(dataObserver);
            recyclerView.setAdapter(wrapperAdapter);
        }
        checkLoadMore();
    }

    @Override
    public void setHasDataToLoad(boolean hasDataToLoad) {
        if (wrapperAdapter != null) {
            wrapperAdapter.showLoadMore(hasDataToLoad);
        }
    }

    @Override
    public void unbind() {
        recyclerView.removeOnScrollListener(onScrollListener);
        if (recyclerView.getAdapter() instanceof WrapperAdapter) {
            WrapperAdapter wrapperAdapter = (WrapperAdapter) recyclerView.getAdapter();
            RecyclerView.Adapter adapter = wrapperAdapter.getWrappedAdapter();
            adapter.unregisterAdapterDataObserver(dataObserver);
            recyclerView.setAdapter(adapter);
        }
    }

    void checkLoadMore(int dx, int dy) {
        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = recyclerView.getLayoutManager().getItemCount();
        int firstVisibleItem;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        } else {
            throw new IllegalStateException("只支持LinearLayout");
        }
        if ((totalItemCount > visibleItemCount && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold))
                && (dx > 0 || dy > 0)
                || visibleItemCount == 0
                ) {
            if (!isLoadMore() && isHasMore()) {
                setLoadMore(true);
                callbacks.onLoadMore();
                showLoadMoreView();
            }
        }
    }

    void checkLoadMore() {
        checkLoadMore(0, 0);
    }

    public void setLoadMore(boolean loadMore) {
        super.setLoadMore(loadMore);
        if (!loadMore) {
            hideLoadMoreView();
        } else {
            showLoadMoreView();
        }
    }


    private void onAdapterDataChanged() {
        wrapperAdapter.showLoadMore(isHasMore());
        wrapperAdapter.hideLoadMoreView();
//        wrapperAdapter.notifyDataChange();
//        checkLoadMore();
    }


    public void showLoadMoreView() {
        wrapperAdapter.showLoadMoreView();
    }

    public void showNoMoreDataView() {
        wrapperAdapter.showNoMoreDataView();
    }

    public void hideLoadMoreView() {
        wrapperAdapter.hideLoadMoreView();
    }


        public static class Builder {
        private final RecyclerView recyclerView;
        private final Callbacks callbacks;
        private boolean showLoadMoreItem = true;
        private int visibleThreshold = 2;
        private LoadMoreItemCreator loadMoreItemCreator;

        public Builder(RecyclerView recyclerView, Callbacks callbacks) {
            this.recyclerView = recyclerView;
            this.callbacks = callbacks;
        }

        public Builder setVisibleThreshold(int threshold) {
            this.visibleThreshold = threshold;
            return this;
        }

        public Builder showLoadMoreItem(boolean showLoadMoreItem) {
            this.showLoadMoreItem = showLoadMoreItem;
            return this;
        }

        public Builder setLoadMoreItemCreator(LoadMoreItemCreator creator) {
            this.loadMoreItemCreator = creator;
            return this;
        }

        public PageLoader build() {
            if (recyclerView.getAdapter() == null) {
                throw new IllegalStateException("需要设置Adapter");
            }
            if (recyclerView.getLayoutManager() == null) {
                throw new IllegalStateException("需要设置LayoutManager");
            }
            if (loadMoreItemCreator == null) {
                loadMoreItemCreator = LoadMoreItemCreator.DEFAULT;
            }
            return new RecyclerPageLoader(recyclerView, callbacks, visibleThreshold, showLoadMoreItem,
                    loadMoreItemCreator);
        }
    }
}
