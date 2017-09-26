package com.xiaolian.amigo.ui.bonus;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.bonus.adaptor.BonusAdaptor;
import com.xiaolian.amigo.ui.bonus.adaptor.ExpiredBonusAdaptor;
import com.xiaolian.amigo.ui.bonus.intf.IBonusPresenter;
import com.xiaolian.amigo.ui.bonus.intf.IBonusView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

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

//    @Override
//    protected int getLayout() {
//        return R.layout.activity_bonus_expired;
//    }

    @Override
    protected void onRefresh() {
        page = Constant.PAGE_START_NUM;
        presenter.requestExpiredBonusList(page);
        bonuses.clear();

    }

    @Override
    public void onLoadMore() {
        presenter.requestExpiredBonusList(page);
    }

    @Override
    protected void setRecyclerView(RecyclerView recyclerView) {
        adaptor = new ExpiredBonusAdaptor(this, R.layout.item_bonus_expired, bonuses);
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected int setTitle() {
        return R.string.expried_bonus;
 }

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(ExpiredBonusActivity.this);
        presenter.requestExpiredBonusList(page);
    }

    @Override
    public void addMore(List<BonusAdaptor.BonusWrapper> bonuses) {
        this.bonuses.addAll(bonuses);
        adaptor.notifyDataSetChanged();
    }
}
