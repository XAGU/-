package com.xiaolian.amigo.ui.device.bathroom;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.bathroom.QueryBathOrderListRespDTO;
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

import static com.xiaolian.amigo.data.enumeration.BathroomOperationStatus.BOOKING_SUCCESS;
import static com.xiaolian.amigo.data.enumeration.BathroomOperationStatus.CANCEL;
import static com.xiaolian.amigo.data.enumeration.BathroomOperationStatus.EXPIRED;
import static com.xiaolian.amigo.data.enumeration.BathroomOperationStatus.FINISHED;

/**
 * 预约记录
 *
 * @author zcd
 * @date 18/6/29
 */
public class BookingRecordActivity extends BathroomBaseActivity implements IBookingRecordView {

    private static final String TAG = BookingRecordActivity.class.getSimpleName();
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
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;

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
//        presenter.getBookingRecordList();
    }

    private void initRecyclerView() {
        mockData(records);
        Log.e(TAG, "initRecyclerView: " + records.get(0).getOrder().getStatus() );
        refreshLayout.setRefreshHeader(new RefreshLayoutHeader(this));
        refreshLayout.setRefreshFooter(new RefreshLayoutFooter(this));
        adapter = new BookingRecordAdapter(this, records);
        adapter.addItemViewDelegate(new BookingRecordItemDelegate(this));
        adapter.addItemViewDelegate(new BookingRecordSummaryDelegate(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    private void mockData(List<BookingRecordAdapter.BookingRecordWrapper> records) {
        QueryBathOrderListRespDTO.OrdersBean ordersBean ;
        BookingRecordAdapter.BookingRecordWrapper wrapper3 = new BookingRecordAdapter.BookingRecordWrapper();
        ordersBean = new QueryBathOrderListRespDTO.OrdersBean();
        wrapper3.setAmount("1");
        wrapper3.setCreateTime(1332089719L);
        wrapper3.setLeftBottomText("浴室房间 215");
        wrapper3.setRecord(true);
        ordersBean.setStatus(CANCEL.getCode());
        wrapper3.setOrder(ordersBean );
        wrapper3.setRightText("取消预约");
        records.add(wrapper3);


        BookingRecordAdapter.BookingRecordWrapper wrapper1 = new BookingRecordAdapter.BookingRecordWrapper();
        ordersBean = new QueryBathOrderListRespDTO.OrdersBean();
        wrapper1.setAmount("1");

        wrapper1.setCreateTime(System.currentTimeMillis() + 200000L);
        wrapper1.setLeftBottomText("浴室房间 215");
        wrapper1.setRecord(true);
        ordersBean.setStatus(BOOKING_SUCCESS.getCode());
        wrapper1.setOrder(ordersBean );
        wrapper1.setRightText("等待洗浴");
        records.add(wrapper1);

        BookingRecordAdapter.BookingRecordWrapper wrapper2 = new BookingRecordAdapter.BookingRecordWrapper();
        ordersBean = new QueryBathOrderListRespDTO.OrdersBean();
        wrapper2.setAmount("1");
        wrapper2.setCreateTime(1532089719L);
        wrapper2.setLeftBottomText("浴室房间 215");
        wrapper2.setRecord(true);
        ordersBean.setStatus(FINISHED.getCode());
        wrapper2.setOrder(ordersBean );
        wrapper2.setRightText("洗浴结束");
        records.add(wrapper2);

        BookingRecordAdapter.BookingRecordWrapper wrapper4 = new BookingRecordAdapter.BookingRecordWrapper();
        ordersBean = new QueryBathOrderListRespDTO.OrdersBean();
        wrapper4.setAmount("1");
        wrapper4.setCreateTime(1132089719L);
        wrapper4.setLeftBottomText("浴室房间 215");
        wrapper4.setRecord(true);
        ordersBean.setStatus(EXPIRED.getCode());
        wrapper4.setOrder(ordersBean );
        wrapper4.setRightText("预约超时");
        records.add(wrapper4);
    }

    @Override
    public void setRefreshComplete() {
        refreshLayout.finishRefresh();
    }

    @Override
    public void setLoadMoreComplete() {
        refreshLayout.finishLoadMore();
    }

    @Override
    public void showErrorView() {
        rlError.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        rlEmpty.setVisibility(View.GONE);
    }

    @Override
    public void hideErrorView() {
        rlError.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyView() {
        rlEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void addMore(List<QueryBathOrderListRespDTO.OrdersBean> ordersBeanList) {
        for (QueryBathOrderListRespDTO.OrdersBean bean : ordersBeanList) {
            BookingRecordAdapter.BookingRecordWrapper wrapper = new BookingRecordAdapter.BookingRecordWrapper();
            wrapper.setOrder(bean);
            wrapper.setRecord(true);
            this.records.add(wrapper);
        }
        adapter.notifyDataSetChanged();
    }
}
