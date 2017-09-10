package com.xiaolian.amigo.activity.bonus;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.activity.BaseActivity;
import com.xiaolian.amigo.activity.bonus.adaptor.BonusAdaptor;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的红包
 * <p>
 * Created by caidong on 2017/9/8.
 */
public class BonusActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2 {

    static List<BonusAdaptor.Bonus> bonuses = new ArrayList<BonusAdaptor.Bonus>() {
        {
            add(new BonusAdaptor.Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new BonusAdaptor.Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new BonusAdaptor.Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new BonusAdaptor.Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new BonusAdaptor.Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new BonusAdaptor.Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new BonusAdaptor.Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new BonusAdaptor.Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new BonusAdaptor.Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new BonusAdaptor.Bonus(1, 1, "xxxx", "yyyy", 3));
        }
    };

    @BindView(R.id.sv_scrollview)
    PullToRefreshScrollView sv_scrollview;
    BonusAdaptor adapter;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    bonuses.add(0, new BonusAdaptor.Bonus(2, 2, "xxxx", "yyyy", 3));
                    adapter.notifyDataSetChanged();
                    sv_scrollview.onRefreshComplete();
                    break;
            }
        }
    };

    @BindView(R.id.lv_bonuses)
    ListView lv_bonuses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus);
        ButterKnife.bind(this);

        adapter = new BonusAdaptor(this, R.layout.item_bonus, bonuses);
        lv_bonuses.setAdapter(adapter);

        sv_scrollview.getLoadingLayoutProxy().setLastUpdatedLabel(
                "上次刷新时间" );
        sv_scrollview.getLoadingLayoutProxy()
                .setPullLabel( "下拉刷新");
        sv_scrollview.getLoadingLayoutProxy().setReleaseLabel(
                "松开即可刷新" );

        sv_scrollview.setOnRefreshListener(this);
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

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        mHandler.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        mHandler.sendEmptyMessageDelayed(0, 2000);
    }
}
