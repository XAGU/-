package com.xiaolian.amigo.activity.bonus;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.activity.bonus.viewmodel.Bonus;
import com.xiaolian.amigo.base.BaseActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;

/**
 * 我的红包
 * <p>
 * Created by caidong on 2017/9/8.
 */
public class BonusActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

    static List<Bonus> bonuses = new ArrayList<Bonus>() {
        {
            add(new Bonus(1, 1, "xxxx", "yyyy", 3));
//            add(new Bonus(1, 1, "xxxx", "yyyy", 3));
//            add(new Bonus(1, 1, "xxxx", "yyyy", 3));
//            add(new Bonus(1, 1, "xxxx", "yyyy", 3));
//            add(new Bonus(1, 1, "xxxx", "yyyy", 3));
//            add(new Bonus(1, 1, "xxxx", "yyyy", 3));
//            add(new Bonus(1, 1, "xxxx", "yyyy", 3));
//            add(new Bonus(1, 1, "xxxx", "yyyy", 3));
//            add(new Bonus(1, 1, "xxxx", "yyyy", 3));
//            add(new Bonus(1, 1, "xxxx", "yyyy", 3));
        }
    };

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

    CommonAdapter<Bonus> adapter;
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

        BGAStickinessRefreshViewHolder stickinessRefreshViewHolder = new BGAStickinessRefreshViewHolder(this, true);
        stickinessRefreshViewHolder.setStickinessColor(R.color.colorPrimary);
        stickinessRefreshViewHolder.setRotateImage(R.drawable.default_avatar);
        mRefreshLayout.setRefreshViewHolder(stickinessRefreshViewHolder);

//        mRefreshLayout.setPullDownRefreshEnable(false);


        adapter = new CommonAdapter<Bonus>(this, R.layout.item_bonus, bonuses) {
            @Override
            protected void convert(ViewHolder viewHolder, Bonus bonus, int position) {
                viewHolder.setText(R.id.tv_amount, bonus.getAmount().toString());
                viewHolder.setText(R.id.tv_type, bonus.getType().toString());
                viewHolder.setText(R.id.tv_time_end, bonus.getTimeEnd());
                viewHolder.setText(R.id.tv_desc, bonus.getDesc());
                viewHolder.setText(R.id.tv_time_left, bonus.getTimeLeft().toString());
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        mRefreshLayout.setDelegate(this);


//        ILoadingLayout loadingLayout = sc_refresh.getLoadingLayoutProxy();
//        loadingLayout.setLastUpdatedLabel("上次刷新时间");
//        loadingLayout.setPullLabel("下拉刷新");
//        loadingLayout.setReleaseLabel("松开即可刷新");
//
//        ScrollView scrollView = sc_refresh.getRefreshableView();
//        sc_refresh.setOnRefreshListener(this);
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
                mHandler.sendEmptyMessageDelayed(0, 2000);
//                Log.i(TAG, "上拉加载更多完成");
            }
        }.execute();
        return true;
    }

//    // 兑换红包
//    @OnClick(R.id.tv_exchage)
//    void exchange() {
//        startActivity(this, BonusExchangeActivity.class);
//    }

//    // 查看过期红包
//    @OnClick(R.id.tv_expired_entry)
//    void queryExpiredBonus() {
//        startActivity(this, ExpiredBonusActivity.class);
//    }

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
