package com.xiaolian.amigo.ui.widget.pageloader;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * 返回LoadMoreView的RecyclerView，通过WrapperAdapter使用
 * @author zcd
 */

public interface LoadMoreItemCreator {
    RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    void showLoadMoreView();

    void showNoMoreDataView();

    /**
     * 默认实现
     */
    LoadMoreItemCreator DEFAULT = new LoadMoreItemCreator() {

        DefaultLoadMoreView loadMoreView;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(loadMoreView = new DefaultLoadMoreView(parent.getContext())) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            // do nth
        }

        @Override
        public void showLoadMoreView() {
            if (loadMoreView != null) {
                loadMoreView.setVisibility(View.VISIBLE);
                loadMoreView.showLoading();
            }
        }

        @Override
        public void showNoMoreDataView() {
            if (loadMoreView != null) {
                loadMoreView.setVisibility(View.VISIBLE);
                loadMoreView.showNoMoreData();
            }
        }

    };
}
