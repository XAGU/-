package com.xiaolian.amigo.tmp.activity.device.waterfountain;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.base.BaseActivity;
import com.xiaolian.amigo.tmp.component.recyclerview.BaseLoadMoreFooterView;
import com.xiaolian.amigo.tmp.component.recyclerview.LinearLayoutWithRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;

/**
 * 附近饮水机页面
 * @author zcd
 */
public class ChooseWaterFountainActivity extends BaseActivity
        implements BGARefreshLayout.BGARefreshLayoutDelegate {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refreshLayout)
    BGARefreshLayout mRefreshLayout;

    private final int MAX_ITEM_COUNT = 20;

    static List<WaterFountainAdaptor.WaterFountain> funtationList = new ArrayList<WaterFountainAdaptor.WaterFountain>() {
        {
            add(new WaterFountainAdaptor.WaterFountain(1,  "xxxx"));
            add(new WaterFountainAdaptor.WaterFountain(1,  "xxxx"));
            add(new WaterFountainAdaptor.WaterFountain(1,  "xxxx"));
            add(new WaterFountainAdaptor.WaterFountain(1,  "xxxx"));
            add(new WaterFountainAdaptor.WaterFountain(1,  "xxxx"));
            add(new WaterFountainAdaptor.WaterFountain(1,  "xxxx"));
            add(new WaterFountainAdaptor.WaterFountain(1,  "xxxx"));
//            add(new BonusWrap(1, 1, "xxxx", "yyyy", 3));
//            add(new BonusWrap(1, 1, "xxxx", "yyyy", 3));
        }
    };

    WaterFountainAdaptor adapter;
    private LinearLayoutWithRecyclerOnScrollListener mLoadMoreListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_choose_water_fountain);
        ButterKnife.bind(this);
        initRecyclerView();
    }

    private void initRecyclerView() {
        BGAStickinessRefreshViewHolder stickinessRefreshViewHolder = new BGAStickinessRefreshViewHolder(this, false);
        stickinessRefreshViewHolder.setStickinessColor(R.color.colorPrimary);
        stickinessRefreshViewHolder.setRotateImage(R.drawable.default_avatar);
        mRefreshLayout.setRefreshViewHolder(stickinessRefreshViewHolder);

//        mRefreshLayout.setPullDownRefreshEnable(false);

        mRefreshLayout.setDelegate(this);

        adapter = new WaterFountainAdaptor(funtationList);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
//        LoadMoreWrapper<BonusWrap> loadMoreWrapper = new LoadMoreWrapper<>(adapter);
//        loadMoreWrapper.setLoadMoreView(R.layout.item_loadmore);

        adapter.setLoadMoreFooterView(new BaseLoadMoreFooterView(this) {
            @Override
            public int getLoadMoreLayoutResource() {
                return R.layout.view_default_load_more;
            }
        });
        mRecyclerView.setAdapter(adapter);
        mLoadMoreListener = new LinearLayoutWithRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int pagination, int pageSize) {
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter.getItemCount() < MAX_ITEM_COUNT) {
                            adapter.showLoadMoreView();
                        } else {
                            adapter.showNoMoreDataView();
                        }
                    }
                });
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //int position = mAdapter.getItemCount();
                        if (adapter.getItemCount() >= MAX_ITEM_COUNT) {
                            adapter.showNoMoreDataView();
                        } else {
//                            mHandler.sendEmptyMessageDelayed(0, 200);
                            adapter.append(new WaterFountainAdaptor.WaterFountain(2, "xxxx"));
                            adapter.hideFooterView();
                        }
                        //java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter positionViewHolder
                        //mAdapter.notifyItemRangeInserted(mAdapter.getItemCount() - 5, 5);
                        //mRecyclerView.scrollToPosition(position);
                        loadComplete();

                    }
                }, 1500);

            }
        };
        mRecyclerView.addOnScrollListener(mLoadMoreListener);
        adapter.setOnItemClickListener(new WaterFountainAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
