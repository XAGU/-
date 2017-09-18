package com.xiaolian.amigo.tmp.activity.bonus;

import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.component.recyclerview.BaseLoadMoreFooterView;
import com.xiaolian.amigo.tmp.activity.bonus.adaptor.BonusAdaptor2;
import com.xiaolian.amigo.tmp.component.recyclerview.LinearLayoutWithRecyclerOnScrollListener;
import com.xiaolian.amigo.tmp.activity.bonus.viewmodel.Bonus;
import com.xiaolian.amigo.tmp.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;

/**
 * 我的红包
 * <p>
 * Created by caidong on 2017/9/8.
 */
public class BonusActivity extends BaseActivity
        implements BGARefreshLayout.BGARefreshLayoutDelegate
{

    static List<Bonus> bonuses = new ArrayList<Bonus>() {
        {
            add(new Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new Bonus(1, 1, "xxxx", "yyyy", 3));
//            add(new BonusWrap(1, 1, "xxxx", "yyyy", 3));
//            add(new BonusWrap(1, 1, "xxxx", "yyyy", 3));
        }
    };

    protected AnimationDrawable mFooterChrysanthemumAd;
    protected ImageView mFooterChrysanthemumIv;

//    @BindView(R.id.lv_bonuses)
//    ListView lv_bonuses;
//    @BindView(R.id.sc_refresh)
//    PullToRefreshScrollView sc_refresh;
    @BindView(R.id.ll_header)
    LinearLayout ll_header;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refreshLayout)
    BGARefreshLayout mRefreshLayout;

    BonusAdaptor2 adapter;
    private final int MAX_ITEM_COUNT = 20;
    private LinearLayoutWithRecyclerOnScrollListener mLoadMoreListener;
    float endY;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    bonuses.add(0, new Bonus(2, 2, "xxxx", "yyyy", 3));
                    adapter.notifyDataSetChanged();
//                    sc_refresh.onRefreshComplete();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus);
        ButterKnife.bind(this);

        initRecyclerView();

//        ILoadingLayout loadingLayout = sc_refresh.getLoadingLayoutProxy();
//        loadingLayout.setLastUpdatedLabel("上次刷新时间");
//        loadingLayout.setPullLabel("下拉刷新");
//        loadingLayout.setReleaseLabel("松开即可刷新");
//
//        ScrollView scrollView = sc_refresh.getRefreshableView();
//        sc_refresh.setOnRefreshListener(this);
    }


    private void initRecyclerView() {
        BGAStickinessRefreshViewHolder stickinessRefreshViewHolder = new BGAStickinessRefreshViewHolder(this, false);
        stickinessRefreshViewHolder.setStickinessColor(R.color.colorPrimary);
        stickinessRefreshViewHolder.setRotateImage(R.drawable.default_avatar);
        mRefreshLayout.setRefreshViewHolder(stickinessRefreshViewHolder);

//        mRefreshLayout.setPullDownRefreshEnable(false);

        mRefreshLayout.setDelegate(this);

        adapter = new BonusAdaptor2(bonuses);

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
                            adapter.append(new Bonus(2, 2, "xxxx", "yyyy", 3));
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

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
//                showLoadingDialog();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
//                dismissLoadingDialog();
                mRefreshLayout.endRefreshing();
//                mClickableLabelTv.setText("加载最新数据完成");
            }
        }.execute();

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
//                showLoadingDialog();
                if (mRecyclerView.getAdapter() != null && mRecyclerView.getAdapter().getItemCount() > 1) {
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 2);
                        }
                    });
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
//                dismissLoadingDialog();
                mRefreshLayout.endLoadingMore();
                mHandler.sendEmptyMessageDelayed(0, 200);
//                Log.i(TAG, "上拉加载更多完成");
            }
        }.execute();
        return true;
    }

    // 兑换红包
    @OnClick(R.id.tv_exchage)
    void exchange() {
        startActivity(this, BonusExchangeActivity.class);
    }

    // 查看过期红包
    @OnClick(R.id.tv_expired_entry)
    void queryExpiredBonus() {
        startActivity(this, ExpiredBonusActivity.class);
    }

//    @Override
//    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//        mHandler.sendEmptyMessageDelayed(0, 2000);
//    }
//
//    @Override
//    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
//        mHandler.sendEmptyMessageDelayed(0, 2000);
//    }
}
