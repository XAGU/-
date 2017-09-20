package com.xiaolian.amigo.ui.device.geyser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.activity.bonus.viewmodel.Bonus;
import com.xiaolian.amigo.tmp.component.recyclerview.BaseLoadMoreFooterView;
import com.xiaolian.amigo.tmp.component.recyclerview.LinearLayoutWithRecyclerOnScrollListener;
import com.xiaolian.amigo.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;

/**
 * 选择红包页面
 * @author zcd
 */

public class ChooseBonusActivity extends BaseActivity
        implements BGARefreshLayout.BGARefreshLayoutDelegate {

    /**
     * 页面头部
     */
    @BindView(R.id.ll_header)
    LinearLayout ll_header;

    /**
     * 红包列表
     */
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    /**
     * 刷新加载控件
     */
    @BindView(R.id.refreshLayout)
    BGARefreshLayout mRefreshLayout;

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

    BonusAdaptor adapter;

    /**
     * 最大item个数
     */
    private final int MAX_ITEM_COUNT = 20;
    private LinearLayoutWithRecyclerOnScrollListener mLoadMoreListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_bonus);
        ButterKnife.bind(this);

        initRecyclerView();

    }

    @Override
    protected void setUp() {

    }

    private void initRecyclerView() {
        BGAStickinessRefreshViewHolder stickinessRefreshViewHolder = new BGAStickinessRefreshViewHolder(this, false);
        stickinessRefreshViewHolder.setStickinessColor(R.color.colorPrimary);
        stickinessRefreshViewHolder.setRotateImage(R.drawable.default_avatar);
        mRefreshLayout.setRefreshViewHolder(stickinessRefreshViewHolder);

//        mRefreshLayout.setPullDownRefreshEnable(false);

        mRefreshLayout.setDelegate(this);

        adapter = new BonusAdaptor(bonuses);

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
        adapter.setOnItemClickListener(new BonusAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                setResult(RESULT_OK);
                finish();
            }
        });
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
        return false;
    }
}
