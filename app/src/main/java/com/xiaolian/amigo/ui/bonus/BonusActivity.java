package com.xiaolian.amigo.ui.bonus;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.bonus.adaptor.BonusAdaptor;
import com.xiaolian.amigo.ui.bonus.intf.IBonusPresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 红包Activity
 * @author zcd
 */

public class BonusActivity extends BonusBaseListActivity implements IBonusView {

    @Inject
    IBonusPresenter<IBonusView> presenter;

    // 订单列表
    List<BonusAdaptor.BonusWrapper> bonuses = new ArrayList<>();

    BonusAdaptor adaptor;


    @Override
    protected void onRefresh() {
        page = Constant.PAGE_START_NUM;
        bonuses.clear();
        presenter.requestBonusList(page);
    }

    @Override
    public void onLoadMore() {
        presenter.requestBonusList(page);
    }

    @Override
    protected void setRecyclerView(RecyclerView recyclerView) {
        adaptor = new BonusAdaptor(this, R.layout.item_bonus, bonuses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);
    }

    @Override
    protected int setTitle() {
        return R.string.my_bonus;
    }

    @Override
    protected int setSubTitle() {
        return R.string.exchange_bonus;
    }

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(BonusActivity.this);
        getSubTitle().setOnClickListener(v -> startActivity(new Intent(BonusActivity.this, BonusExchangeActivity.class)));
        getFooter().findViewById(R.id.tv_expired_entry).setOnClickListener(v -> {
            startActivity(new Intent(this, ExpiredBonusActivity.class));
        });
        presenter.requestBonusList(page);
    }

    protected RecyclerView.Adapter getAdaptor() {
        adaptor = new BonusAdaptor(this, R.layout.item_bonus, bonuses);
        return adaptor;
    }

    @Override
    protected int setFooterLayout() {
        return R.layout.footer_bonus;
    }

    //    @Override
//    protected int getLayout() {
//        return R.layout.activity_bonus;
//    }

//    @Override
//    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
//        page = 1;
//        setRefreshing(true);
//    }

//    @Override
//    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
//        return false;
//    }

    @Override
    public void addMore(List<BonusAdaptor.BonusWrapper> bonuses) {
        this.bonuses.addAll(bonuses);
        adaptor.notifyDataSetChanged();
    }

    @Override
    public void setLoadMoreComplete() {
        getRecyclerView().loadMoreComplete();
    }

    @Override
    public void setRefreshComplete() {
        getRecyclerView().refreshComplete();
    }

}
