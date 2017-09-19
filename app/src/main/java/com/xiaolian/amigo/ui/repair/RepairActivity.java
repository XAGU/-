package com.xiaolian.amigo.ui.repair;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.common.config.SpaceItemDecoration;
import com.xiaolian.amigo.tmp.common.util.ScreenUtils;
import com.xiaolian.amigo.ui.order.OrderActivity;
import com.xiaolian.amigo.ui.order.adaptor.OrderAdaptor;
import com.xiaolian.amigo.ui.order.intf.IOrderPresenter;
import com.xiaolian.amigo.ui.order.intf.IOrderView;
import com.xiaolian.amigo.ui.repair.adaptor.RepairAdaptor;
import com.xiaolian.amigo.ui.repair.intf.IRepairPresenter;
import com.xiaolian.amigo.ui.repair.intf.IRepairView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 报修记录
 * <p>
 * Created by caidong on 2017/9/18.
 */
public class RepairActivity extends RepairBaseActivity implements IRepairView {

    @Inject
    IRepairPresenter<IRepairView> presenter;
    @BindView(R.id.rv_repairs)
    RecyclerView rv_repairs;

    List<RepairAdaptor.RepairWrapper> repairs = new ArrayList<>();

    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(this);

        adapter = new RepairAdaptor(repairs);
        rv_repairs.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 14)));
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_repairs.setLayoutManager(manager);
        rv_repairs.setAdapter(adapter);

        presenter.requestRepairs(Constant.PAGE_START_NUM);
    }

    @Override
    protected void setUp() {

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
}
