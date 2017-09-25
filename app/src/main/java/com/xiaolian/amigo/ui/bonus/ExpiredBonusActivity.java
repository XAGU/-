package com.xiaolian.amigo.ui.bonus;

import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.bonus.adaptor.BonusAdaptor;
import com.xiaolian.amigo.ui.bonus.adaptor.ExpiredBonusAdaptor;
import com.xiaolian.amigo.ui.bonus.intf.IBonusPresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 过期红包
 * <p>
 * Created by caidong on 2017/9/8.
 */
public class ExpiredBonusActivity extends BonusBaseListActivity implements IBonusView {


    @Inject
    IBonusPresenter<IBonusView> presenter;

    // 订单列表
    List<BonusAdaptor.BonusWrapper> bonuses = new ArrayList<>();

    ExpiredBonusAdaptor adaptor;

    @Override
    protected void initPresenter() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(ExpiredBonusActivity.this);
    }

    protected RecyclerView.Adapter getAdaptor() {
        adaptor = new ExpiredBonusAdaptor(this, R.layout.item_bonus_expired, bonuses);
        return adaptor;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_bonus_expired;
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 1;
        setRefreshing(true);
        presenter.requestExpiredBonusList(page);
        bonuses.clear();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void onLoadMore() {
        presenter.requestExpiredBonusList(page);
    }

    @Override
    public void addMore(List<BonusAdaptor.BonusWrapper> bonuses) {
        this.bonuses.addAll(bonuses);
        adaptor.notifyDataSetChanged();
    }
}
