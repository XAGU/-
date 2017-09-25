package com.xiaolian.amigo.ui.bonus;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.bonus.adaptor.BonusAdaptor;
import com.xiaolian.amigo.ui.bonus.intf.IBonusPresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusView;

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

public class BonusActivity extends BonusBaseListActivity implements IBonusView {

    @Inject
    IBonusPresenter<IBonusView> presenter;

    // 兑换红包
    @OnClick(R.id.tv_exchage)
    void exchange() {
        startActivity(new Intent(this, BonusExchangeActivity.class));
    }

    // 查看过期红包
    @OnClick(R.id.tv_expired_entry)
    void queryExpiredBonus() {
        startActivity(new Intent(this, ExpiredBonusActivity.class));
    }

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
    public void onLoadMore() {
        presenter.requestBonusList(page);
    }

    protected RecyclerView.Adapter getAdaptor() {
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
        setRefreshing(true);
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
