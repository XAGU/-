package com.xiaolian.amigo.ui.repair;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.util.ScreenUtils;
import com.xiaolian.amigo.ui.repair.adaptor.RepairAdaptor;
import com.xiaolian.amigo.ui.repair.intf.IRepairPresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 报修记录
 * <p>
 * Created by caidong on 2017/9/18.
 */
public class RepairActivity extends RepairBaseListActivity implements IRepairView {
    public static final String INTENT_KEY_LAST_REPAIR_TIME = "intent_last_repair_time";

    @Inject
    IRepairPresenter<IRepairView> presenter;

    List<RepairAdaptor.RepairWrapper> repairs = new ArrayList<>();

    RepairAdaptor adapter;
    Long lastRepairTime;

    @Override
    protected void setUp() {
        if (getIntent() != null) {
            lastRepairTime = getIntent().getLongExtra(INTENT_KEY_LAST_REPAIR_TIME, -1);
        }
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
    protected void onRefresh() {
        page = Constant.PAGE_START_NUM;
        repairs.clear();
        presenter.requestRepairs(page);
    }

    @Override
    public void onLoadMore() {
        presenter.requestRepairs(page);
    }

    @Override
    protected void setRecyclerView(RecyclerView recyclerView) {
        adapter = new RepairAdaptor(repairs);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 14)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected int setTitle() {
        return R.string.repair_record;
    }

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        if (lastRepairTime != null && lastRepairTime != -1) {
            presenter.setLastRepairTime(lastRepairTime);
        }
    }
}
