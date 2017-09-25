package com.xiaolian.amigo.ui.repair;

import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.common.config.SpaceItemDecoration;
import com.xiaolian.amigo.tmp.common.util.ScreenUtils;
import com.xiaolian.amigo.ui.repair.adaptor.RepairAdaptor;
import com.xiaolian.amigo.ui.repair.intf.IRepairPresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 报修记录
 * <p>
 * Created by caidong on 2017/9/18.
 */
public class RepairActivity extends RepairBaseListActivity implements IRepairView {

    @Inject
    IRepairPresenter<IRepairView> presenter;

    List<RepairAdaptor.RepairWrapper> repairs = new ArrayList<>();

    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;

    @Override
    protected void initPresenter() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(this);
    }

    @Override
    protected void setUp() {

    }

    @Override
    protected RecyclerView.Adapter getAdaptor() {
        adapter = new RepairAdaptor(repairs);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 14)));
        return adapter;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_repair;
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        presenter.clearObservers();
        repairs.clear();
        super.onDestroy();
    }

    @Override
    public void addMore(List<RepairAdaptor.RepairWrapper> repairs) {
        this.repairs.addAll(repairs);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = Constant.PAGE_START_NUM;
        repairs.clear();
        setRefreshing(true);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void onLoadMore() {
        presenter.requestRepairs(page);
    }
}
