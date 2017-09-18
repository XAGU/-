package com.xiaolian.amigo.ui.bonus;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.bonus.adaptor.BonusAdaptor;
import com.xiaolian.amigo.ui.bonus.intf.IBonusPresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 红包Activity
 * @author zcd
 */

public class BonusActivity extends BonusBaseActivity implements IBonusView {

    @Inject
    IBonusPresenter<IBonusView> presenter;

//    // 兑换红包
//    @OnClick(R.id.tv_exchage)
//    void exchange() {
//        startActivity(this, BonusExchangeActivity.class);
//    }
//
//    // 查看过期红包
//    @OnClick(R.id.tv_expired_entry)
//    void queryExpiredBonus() {
//        startActivity(new this, ExpiredBonusActivity.class);
//    }

    // 订单列表
    List<BonusAdaptor.BonusWrapper> bonuses = new ArrayList<>();

    BonusAdaptor adaptor;

    @Override
    protected void initPresenter() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(BonusActivity.this);
    }

    @Override
    protected void initData() {
        presenter.requestBonusList(page);
    }

    @Override
    public void onLoadMore() {
        loadStart();
        if (hasLoadedAll()) {
            showNoMoreDataView();
        } else {
            showLoadMoreView();
            presenter.requestBonusList(page);
        }
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        adaptor = new BonusAdaptor(this, R.layout.item_bonus, bonuses);
        return adaptor;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_bonus;
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 1;
        setLoadAll(false);
        setRefreshing(true);
        presenter.requestBonusList(page);
        bonuses.clear();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void addMore(List<BonusAdaptor.BonusWrapper> bonuses) {
        this.bonuses.addAll(bonuses);
        adaptor.notifyDataSetChanged();
    }

}
