package com.xiaolian.amigo.ui.device.bathroom;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.bathroom.adapter.BookingRecordAdapter;
import com.xiaolian.amigo.ui.device.bathroom.adapter.BookingRecordItemDelegate;
import com.xiaolian.amigo.ui.device.bathroom.adapter.BookingRecordSummaryDelegate;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingRecordPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingRecordView;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutFooter;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutHeader;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 预约记录
 *
 * @author zcd
 * @date 18/6/29
 */
public class BookingRecordActivity extends BathroomBaseActivity implements IBookingRecordView {

    @Inject
    IBookingRecordPresenter<IBookingRecordView> presenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.ll_header)
    LinearLayout llHeader;

    @BindView(R.id.view_line)
    View viewLine;

    private BookingRecordAdapter adapter;
    private List<BookingRecordAdapter.BookingRecordWrapper> records = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_record);
        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);
        presenter.onAttach(this);

        initView();
    }

    private void initView() {

        initRecyclerView();

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            //Log.d("STATE", appBarLayout.getTotalScrollRange() +"//"+ verticalOffset+"//"+tv_toolbar_title.getHeight());
            if (verticalOffset < -(tvToolbarTitle.getHeight() + llHeader.getPaddingTop())) {
                tvTitle.setVisibility(View.VISIBLE);
                viewLine.setVisibility(View.VISIBLE);
            } else {
                tvTitle.setVisibility(View.GONE);
                viewLine.setVisibility(View.GONE);
            }
        });
    }

    private void initRecyclerView() {

        refreshLayout.setRefreshHeader(new RefreshLayoutHeader(this));
        refreshLayout.setRefreshFooter(new RefreshLayoutFooter(this));

        adapter = new BookingRecordAdapter(this, records);
        adapter.addItemViewDelegate(new BookingRecordItemDelegate());
        adapter.addItemViewDelegate(new BookingRecordSummaryDelegate());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mockData(records);
        recyclerView.setAdapter(adapter);
    }

    private void mockData(List<BookingRecordAdapter.BookingRecordWrapper> records) {

        BookingRecordAdapter.BookingRecordWrapper wrapper3 = new BookingRecordAdapter.BookingRecordWrapper();
        wrapper3.setBookingTime(1);
        wrapper3.setMissedBookingTime(2);
        records.add(wrapper3);

        BookingRecordAdapter.BookingRecordWrapper wrapper1 = new BookingRecordAdapter.BookingRecordWrapper();
        wrapper1.setAmount("1");
        wrapper1.setCreateTime(123L);
        wrapper1.setLeftBottomText("浴室房间 215");
        wrapper1.setRecord(true);
        wrapper1.setRightText("等待洗浴");
        records.add(wrapper1);

        BookingRecordAdapter.BookingRecordWrapper wrapper2 = new BookingRecordAdapter.BookingRecordWrapper();
        wrapper2.setAmount("1");
        wrapper2.setCreateTime(123L);
        wrapper2.setLeftBottomText("浴室房间 215");
        wrapper2.setRecord(true);
        wrapper2.setRightText("等待洗浴");
        records.add(wrapper2);

    }
}
