package com.xiaolian.amigo.ui.widget.pageloader;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

/**
 * RecyclerView.Adapter包装类
 * @author zcd
 */

public class WrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = WrapperAdapter.class.getSimpleName();
    /**
     * RecyclerView上加载进度条的item类型
     */
    private static final int VIEW_TYPE_LOADING = Integer.MAX_VALUE - 1111;

    private final RecyclerView.Adapter wrappedAdapter;
    private final LoadMoreItemCreator loadMoreItemCreator;
    private boolean showLoadMore = true;

    public WrapperAdapter(RecyclerView.Adapter wrappedAdapter, LoadMoreItemCreator loadMoreItemCreator) {
        this.wrappedAdapter = wrappedAdapter;
        this.loadMoreItemCreator = loadMoreItemCreator;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            return loadMoreItemCreator.onCreateViewHolder(parent, viewType);
        } else {
            return wrappedAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isLoadMoreItem(position)) {
            loadMoreItemCreator.onBindViewHolder(holder, position);
        } else {
            wrappedAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return showLoadMore ? wrappedAdapter.getItemCount() + 1 : wrappedAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return isLoadMoreItem(position) ? VIEW_TYPE_LOADING : wrappedAdapter.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return isLoadMoreItem(position) ? RecyclerView.NO_ID : wrappedAdapter.getItemId(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
        wrappedAdapter.setHasStableIds(hasStableIds);
    }

    public RecyclerView.Adapter getWrappedAdapter() {
        return wrappedAdapter;
    }


    public boolean isShowLoadMore() {
        return showLoadMore;
    }


    public void showLoadMoreView() {
        loadMoreItemCreator.showLoadMoreView();
        if (!this.showLoadMore) {
            this.showLoadMore = true;
            try {
                notifyItemInserted(getItemCount());
            } catch (Exception e) {
                Log.w(TAG, "notifyItemChanged failure");
                e.printStackTrace();
                notifyDataSetChanged();
            }
        }
    }

    public void showNoMoreDataView() {
        loadMoreItemCreator.showNoMoreDataView();
        if (!this.showLoadMore) {
            this.showLoadMore = true;
            try {
                notifyItemInserted(getItemCount());
            } catch (Exception e) {
                Log.w(TAG, "notifyItemChanged failure");
                e.printStackTrace();
                notifyDataSetChanged();
            }
        }
    }

    public void hideLoadMoreView() {
        if (showLoadMore) {
            this.showLoadMore = false;
            try {
                notifyItemRemoved(getItemCount() + 1);
            } catch (Exception e) {
                notifyDataSetChanged();
                Log.w(TAG, "notifyItemChanged failure");
                e.printStackTrace();
            }
        }
    }

    void showLoadMore(boolean showLoadMore) {
        if (this.showLoadMore != showLoadMore) {
            this.showLoadMore = showLoadMore;
            notifyDataSetChanged();
        }
    }

    void notifyDataChange() {
        wrappedAdapter.notifyDataSetChanged();
    }

    boolean isLoadMoreItem(int position) {
        return showLoadMore && position == getLoadMoreItemPosition();
    }

    private int getLoadMoreItemPosition() {
        return showLoadMore ? getItemCount() - 1 : -1;
    }
}
