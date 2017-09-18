package com.xiaolian.amigo.ui.bonus;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.bonus.intf.IBonusPresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 红包Activity
 * @author zcd
 */

public class BonusActivity extends BonusBaseActivity implements IBonusView {

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
//            add(new Bonus(1, 1, "xxxx", "yyyy", 3));
//            add(new Bonus(1, 1, "xxxx", "yyyy", 3));
        }
    };

    @Inject
    IBonusPresenter<IBonusView> presenter;

    CommonAdapter<Bonus> adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(BonusActivity.this);
    }


    @Override
    public void onLoadMore() {
        loadStart();
        if (adaptor.getItemCount() > 10) {
            showNoMoreDataView();
        } else {
            showLoadMoreView();
        }
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {

                //int position = mAdapter.getItemCount();
//                            mHandler.sendEmptyMessageDelayed(0, 200);
                bonuses.add(new Bonus(2, 2, "xxxx", "yyyy", 3));
                adaptor.notifyDataSetChanged();
                hideLoadMoreView();
                //java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter positionViewHolder
                //mAdapter.notifyItemRangeInserted(mAdapter.getItemCount() - 5, 5);
                //mRecyclerView.scrollToPosition(position);
                loadComplete();

            }
        }, 1500);

    }


    @Override
    public boolean hasLoadedAll() {
        return false;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        adaptor = new CommonAdapter<Bonus>(this, R.layout.item_bonus, bonuses) {
            @Override
            protected void convert(ViewHolder holder, Bonus bonus, int position) {

                holder.setText(R.id.tv_amount, bonus.amount.toString());
                holder.setText(R.id.tv_type, bonus.type.toString());
                holder.setText(R.id.tv_time_end, bonus.timeEnd);
                holder.setText(R.id.tv_desc, bonus.desc);
                holder.setText(R.id.tv_time_left, bonus.timeLeft.toString());
            }
        };
        return adaptor;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_bonus;
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
//                Log.i(TAG, "上拉加载更多完成");
            }
        }.execute();
        return true;
    }

    public static class Bonus {
        // 红包类型
        Integer type;
        // 红包金额
        Integer amount;
        // 到期时间
        String timeEnd;
        // 描述信息
        String desc;
        // 剩余时间
        Integer timeLeft;

        public Bonus(Integer type, Integer amount, String timeEnd, String desc, Integer timeLeft) {
            this.type = type;
            this.amount = amount;
            this.timeEnd = timeEnd;
            this.desc = desc;
            this.timeLeft = timeLeft;
        }
    }
}
