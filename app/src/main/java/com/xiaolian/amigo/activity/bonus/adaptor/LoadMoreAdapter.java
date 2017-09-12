package com.xiaolian.amigo.activity.bonus.adaptor;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

/**
 * Created by adamzfc on 9/12/17.
 */

public class LoadMoreAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * 是否正在加载
     */
    private boolean isLoading;
    /**
     * 上拉加载更多控件
     */
    protected View mLoadMoreFooterView;
    /**
     * 底部加载更多提示控件
     */
    protected TextView mFooterStatusTv;
    /**
     * 底部加载更多菊花控件
     */
    protected ImageView mFooterChrysanthemumIv;
    /**
     * 底部加载更多菊花drawable
     */
    protected AnimationDrawable mFooterChrysanthemumAd;
    /**
     * 正在加载更多时的文本
     */
    protected String mLodingMoreText = "加载中...";

    /**
     * normal type
     */
    private final int VIEW_TYPE_ITEM = 0;
    /**
     * loading type
     */
    private final int VIEW_TYPE_LOADING = 1;

    private int lastVisibleItem, totalItemCount;
    private int visibleThreshold = 5;

    private RecyclerView.Adapter mInnerAdapter;

    public LoadMoreAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setLoaded() {
        isLoading = false;
    }

    public View getLoadMoreFooterView() {
        if (mLoadMoreFooterView == null) {
//            mLoadMoreFooterView = View.inflate(mContext, R.layout.view_normal_refresh_footer, null);
            mLoadMoreFooterView.setBackgroundColor(Color.TRANSPARENT);
//            if (mLoadMoreBackgroundColorRes != -1) {
//                mLoadMoreFooterView.setBackgroundResource(mLoadMoreBackgroundColorRes);
//            }
//            if (mLoadMoreBackgroundDrawableRes != -1) {
//                mLoadMoreFooterView.setBackgroundResource(mLoadMoreBackgroundDrawableRes);
//            }
            mFooterStatusTv = (TextView) mLoadMoreFooterView.findViewById(R.id.tv_normal_refresh_footer_status);
            mFooterChrysanthemumIv = (ImageView) mLoadMoreFooterView.findViewById(R.id.iv_normal_refresh_footer_chrysanthemum);
            mFooterChrysanthemumAd = (AnimationDrawable) mFooterChrysanthemumIv.getDrawable();
            mFooterStatusTv.setText(mLodingMoreText);
        }
        return mLoadMoreFooterView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING)
        {
            ViewHolder holder;
//            if (mLoadMoreView != null)
//            {
//                holder = ViewHolder.createViewHolder(parent.getContext(), mLoadMoreView);
//            } else
//            {
//                holder = ViewHolder.createViewHolder(parent.getContext(), parent, mLoadMoreLayoutId);
//            }
//            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private OnLoadMoreListener onLoadMoreListener;
    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
