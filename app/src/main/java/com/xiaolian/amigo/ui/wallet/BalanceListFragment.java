package com.xiaolian.amigo.ui.wallet;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.xiaolian.amigo.data.enumeration.WithdrawOperationType;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.wallet.adaptor.BillListAdaptor;
import com.xiaolian.amigo.ui.wallet.adaptor.WithdrawalAdaptor;
import com.xiaolian.amigo.ui.widget.dialog.YearMonthPickerDialog;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutFooter;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutHeader;
import com.xiaolian.amigo.ui.widget.popWindow.BillFilterStatusPopupWindow;
import com.xiaolian.amigo.ui.widget.popWindow.BillFilterTypePopupWindow;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.TimeUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiaolian.amigo.util.Constant.FROM_LOCATION;

public class BalanceListFragment extends Fragment {

    private RecyclerView recyclerView;
    private RelativeLayout rlEmpty;
    private TextView tvEmptyTip;
    private RelativeLayout rlError;

    private TextView tvMonthlyOrderDate;

    private TextView tvFilterStatus;

    private TextView tvFilterType;

    YearMonthPickerDialog yearMonthPickerDialog;

    private SmartRefreshLayout refreshLayout;

    private List<BillListAdaptor.BillListAdaptorWrapper> items = new ArrayList<>();

    //临时存放最新加载的数据，不是当月的数据不加载到items中，而是临时存放在这里
    private List<BillListAdaptor.BillListAdaptorWrapper> tempItems = new ArrayList<>();

    private BillListAdaptor adaptor;

    /**
     * 显示选择状态的popwindow
     */
    private BillFilterStatusPopupWindow filterStatusPopupWindow;

    /**
     * 显示选择类型的popwindow
     */
    private BillFilterTypePopupWindow filterTypePopupWindow;


    private String timeStr;

    private Long lastId;

    private Integer billType;

    private Integer billStatus;

    private LinearLayout rlFilterContentView ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_balance_list_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        rlEmpty = view.findViewById(R.id.rl_empty);
        tvEmptyTip = view.findViewById(R.id.tv_empty_tip);
        rlError = view.findViewById(R.id.rl_error);
        tvMonthlyOrderDate = view.findViewById(R.id.tv_filter_date);
        tvFilterType = view.findViewById(R.id.tv_filter_type);
        tvFilterStatus = view.findViewById(R.id.tv_filter_status);
        rlFilterContentView = view.findViewById(R.id.tv_filter_content);

        initRecyclerView();
        initTimeStr();
        initPop();
    }

    private void initTimeStr() {
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        int currentMonth = cal.get(Calendar.MONTH )+1;
        timeStr = String.valueOf(currentYear * 100 + currentMonth);
        tvMonthlyOrderDate.setText(String.format(Locale.getDefault(), "%d年%d月", currentYear, currentMonth));
    }


    public void initPop() {
        if (filterStatusPopupWindow == null) {
            filterStatusPopupWindow = new BillFilterStatusPopupWindow(getActivity());
            filterStatusPopupWindow.setPopFilterClickListener(new BillFilterStatusPopupWindow.PopFilterClickListener() {
                @Override
                public void click(int status, CharSequence name) {
                    tvFilterStatus.setTextColor(Color.parseColor("#FF5555"));
                    tvFilterStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.incomedown, 0);
                    if (billStatus!=null && billStatus == status) /*选择的是一样的就不加载*/{
                        return;
                    }
                    tvMonthlyOrderDate.setText(timeStr); //每次重新选择后都需要把日期还原
                    items.clear();
                    tempItems.clear();
                    lastId = null;
                    adaptor.notifyDataSetChanged();
                    /*选择的是新数据，需要把已有的数据清空*/
                    billStatus = status;
                    if (billStatus == 0) {
                        billStatus = null;
                    }
                    refreshLayout.autoRefresh();
                    tvFilterStatus.setText(name);
                }
            });

            filterStatusPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    filterStatusPopupWindow.setBackgroundAlpha(1.0f);
                    if (tvFilterStatus.getText().toString().equalsIgnoreCase("筛选")) {
                        tvFilterStatus.setTextColor(Color.parseColor("#222222"));
                        tvFilterStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.spread, 0);
                    } else {
                        tvFilterStatus.setTextColor(Color.parseColor("#FF5555"));
                        tvFilterStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.incomedown, 0);
                    }
                }
            });
        }

        if (filterTypePopupWindow == null) {
            filterTypePopupWindow = new BillFilterTypePopupWindow(getActivity());
            //设置配置的服务
            filterTypePopupWindow.setBillItems(((BalanceDetailListActivity)getActivity()).presenter.getSchoolBizList());
            filterTypePopupWindow.setPopFilterClickListener(new BillFilterTypePopupWindow.PopFilterClickListener() {
                @Override
                public void click(int type, String name) {
                    tvFilterType.setTextColor(Color.parseColor("#FF5555"));
                    tvFilterType.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.incomedown, 0);
                    if (billType != null && billType == type) /*选择的是一样的就不加载*/{
                        return;
                    }
                    tvMonthlyOrderDate.setText(timeStr); //每次重新选择后都需要把日期还原
                    items.clear();
                    tempItems.clear();
                    lastId = null;
                    adaptor.notifyDataSetChanged();
                    /*选择的是新数据，需要把已有的数据清空*/
                    billType = type;
                    if (billType == 0) {
                        billType = null;
                    }
                    refreshLayout.autoRefresh();
                    tvFilterType.setText(name);
                }
            });
            filterTypePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    filterTypePopupWindow.setBackgroundAlpha(1.0f);
                    if (tvFilterType.getText().toString().equalsIgnoreCase("分类")) {
                        tvFilterType.setTextColor(Color.parseColor("#222222"));
                        tvFilterType.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.spread, 0);
                    } else {
                        tvFilterType.setTextColor(Color.parseColor("#FF5555"));
                        tvFilterType.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.incomedown, 0);
                    }
                }
            });
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (filterStatusPopupWindow != null && filterStatusPopupWindow.isShowing()){
            filterStatusPopupWindow.dismiss();
        }

        if (filterTypePopupWindow != null && filterTypePopupWindow.isShowing()){
            filterTypePopupWindow.dismiss();
        }
    }

    private void initRecyclerView() {
        setRecyclerView(recyclerView);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                BalanceListFragment.this.onLoadMore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                BalanceListFragment.this.onRefresh();
            }
        });
        refreshLayout.setRefreshHeader(new RefreshLayoutHeader(getActivity()));
        refreshLayout.setRefreshFooter(new RefreshLayoutFooter(getActivity()));
        refreshLayout.setReboundDuration(200);
        refreshLayout.autoRefresh(20);
    }

    @OnClick(R.id.tv_filter_status)
    public void showFilterStatus() {
        filterStatusPopupWindow.showUp(rlFilterContentView);
        filterStatusPopupWindow.setBackgroundAlpha(0.45f);
        tvFilterStatus.setTextColor(Color.parseColor("#FF5555"));
        tvFilterStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.income, 0);

    }

    @OnClick(R.id.tv_filter_type)
    public void showFilterType() {
        filterTypePopupWindow.showUp(rlFilterContentView);
        filterTypePopupWindow.setBackgroundAlpha(0.45f);
        tvFilterType.setTextColor(Color.parseColor("#FF5555"));
        tvFilterType.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.income, 0);
    }

    @OnClick(R.id.tv_filter_date)
    public void showDatePick() {
        if (yearMonthPickerDialog == null) {
             Long timestamps = ((BalanceDetailListActivity)getActivity()).presenter.getAccountCreateTime();
            yearMonthPickerDialog = new YearMonthPickerDialog(getActivity(), timestamps);
        }
        tvMonthlyOrderDate.setTextColor(Color.parseColor("#FF5555"));
        tvMonthlyOrderDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.income, 0);
        yearMonthPickerDialog.setOnItemSelectedListener((picker, date) -> {
           Calendar cal = Calendar.getInstance();
           cal.setTime(date);
           int currentYear = cal.get(Calendar.YEAR);
           int currentMonth = cal.get(Calendar.MONTH) + 1;

           tvMonthlyOrderDate.setTextColor(Color.parseColor("#FF5555"));
           tvMonthlyOrderDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.incomedown, 0);
           tvMonthlyOrderDate.setText(String.format(Locale.getDefault(), "%d年%d月", currentYear, currentMonth));

          String newTimeStr = String.valueOf(currentYear * 100 + currentMonth);
           if (timeStr.equalsIgnoreCase(newTimeStr)) {
              return;//相同不用请求新数据
          }
          timeStr = newTimeStr;
          items.clear();
          tempItems.clear();
          lastId = null;
          adaptor.notifyDataSetChanged();
          tvMonthlyOrderDate.setText(String.format(Locale.getDefault(), "%d年%d月", currentYear, currentMonth));
            refreshLayout.autoRefresh();
        });
        yearMonthPickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                tvMonthlyOrderDate.setTextColor(Color.parseColor("#FF5555"));
                tvMonthlyOrderDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.incomedown, 0);
            }
        });
        yearMonthPickerDialog.show();
}


    protected void setRecyclerView(RecyclerView recyclerView) {
        adaptor = new BillListAdaptor(getActivity(), R.layout.item_withdrawal_record, items);
        adaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                BillListAdaptor.BillListAdaptorWrapper item = items.get(position);
                //点击后跳转到账单详情
                if (item.getType() == BillListAdaptor.XLFilterContentViewBillTypeRecharge || item.getType() == BillListAdaptor.XLFilterContentViewBillTypeWithdraw) /*余额充值、退款跳转*/{
                    ((BalanceDetailListActivity)getActivity()).gotoBillRechargeWithdrawActivity(item.getType(), item.getId());
                } else /*跳转到消费账单页面（包含预付待找零）*/{
                    ((BalanceDetailListActivity)getActivity()).gotoBillDetailActivity(item.getType(), item.getId(), item.getStatus());
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adaptor);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    if (items.size() <= 0) {
                        return;
                    }
                    BillListAdaptor.BillListAdaptorWrapper item = items.get(firstItemPosition);
                    Calendar cal = Calendar.getInstance();
                    Date date = TimeUtils.millis2Date(item.getCreateTime());
                    cal.setTime(date);
                    int currentYear = cal.get(Calendar.YEAR);
                    int currentMonth = cal.get(Calendar.MONTH) + 1;
                    tvMonthlyOrderDate.setText(String.format(Locale.getDefault(), "%d年%d月", currentYear, currentMonth));
                }
            }
        });

    }

    void onRefresh() {
        if (items.size() > 0) {
            BillListAdaptor.BillListAdaptorWrapper item = items.get(0);
            lastId = item.getDetailId();
        }

        if (tempItems.size() > 0) {
            BillListAdaptor.BillListAdaptorWrapper item = tempItems.get(0);
            lastId = item.getDetailId();
        }

        ((BalanceDetailListActivity)getActivity()).presenter.getUserBillList(timeStr, billType, billStatus, lastId, true, 20);

    }

    void onLoadMore() {
        if (items.size() > 0) {
            BillListAdaptor.BillListAdaptorWrapper item = items.get(items.size()-1);
            lastId = item.getDetailId();
        }

        if (tempItems.size() > 0) {
            BillListAdaptor.BillListAdaptorWrapper item = tempItems.get(tempItems.size()-1);
            lastId = item.getDetailId();
        }

        ((BalanceDetailListActivity)getActivity()).presenter.getUserBillList(timeStr, billType, billStatus, lastId, false, 20);
    }

    public void setLoadMoreComplete() {
        refreshLayout.finishLoadMore();
    }

    public void setRefreshComplete() {
        refreshLayout.finishRefresh(300);
    }

    public void addMore(List<BillListAdaptor.BillListAdaptorWrapper> wrappers) {

        if (wrappers.size() <= 0 && tempItems.size() <= 0)  /*没有新的数据，并且没有临时存储的数据*/ {
            if (items.size() <= 0) {
                showEmptyView(R.string.empty_tip_1);
            }
            return;
        }

        if (items.size() <= 0)  /*第一次请求数据*/{
            //1、如果最新的一条不是当前选择的月份，则不展示出来，留到下次上拉或者下拉的时候再展示
            String newTimeStr = wrappers.size() > 0 ? TimeUtils.millis2String(wrappers.get(0).getCreateTime(), TimeUtils.MY_DATE_YEARMON_FORMAT) : timeStr;
            if (timeStr.equalsIgnoreCase(newTimeStr) || tempItems.size() > 0)/*最新的为当前月份的数据，获取是加载数据进来*/ {
                items.addAll(wrappers);
                //把老数据加进去，下拉加载最新的，上拉加载旧的
                BillListAdaptor.BillListAdaptorWrapper newItem = wrappers.size() > 0 ? wrappers.get(0) : null;
                BillListAdaptor.BillListAdaptorWrapper oldItem = tempItems.size() > 0 ? tempItems.get(tempItems.size()-1): null;
                if (newItem !=null && oldItem !=null && newItem.getCreateTime() > oldItem.getCreateTime()) /*加载的是新数据， 放在底部*/{
                    items.addAll(tempItems);
                } else {
                    items.addAll(0, tempItems);
                }
                tempItems.clear();
                adaptor.notifyDataSetChanged();
            } else /*最新的不是当前月份的数据*/{
                //把数据放到临时存储的一个地方
                tempItems.addAll(wrappers);
                showEmptyView(R.string.empty_tip_1);
            }
            return;
        }
        //取新加载的数据的第一条和已有的数据的最后一条做比较，新加载的时间戳大则表示拉取的最新的，否则拉取的是历史数据
        BillListAdaptor.BillListAdaptorWrapper newItem = wrappers.get(0);
        BillListAdaptor.BillListAdaptorWrapper oldItem = items.get(items.size()-1);
        if (newItem.getCreateTime() > oldItem.getCreateTime()) /*加载的是新数据， 放在底部*/{
            items.addAll(0,wrappers);
        } else {
            items.addAll(wrappers);
        }
        adaptor.notifyDataSetChanged();
    }

    public void showEmptyView() {
        showEmptyView(getString(R.string.empty_tip), R.color.colorBackgroundGray);
    }

    public void showEmptyView(int tipRes, int colorRes) {
        showEmptyView(getString(tipRes), colorRes);
    }

    public void showEmptyView(int tipRes) {
        showEmptyView(tipRes, R.color.colorBackgroundGray);
    }

    public void showEmptyView(String tip, int colorRes) {
        if (items.size() <= 0) {
            rlEmpty.setVisibility(View.VISIBLE);
//            rlEmpty.setBackgroundResource(colorRes);
            tvEmptyTip.setText(tip);
        }
    }

    public void hideEmptyView() {
        rlEmpty.setVisibility(View.GONE);
    }

    public void showErrorView() {
        showErrorView(R.color.colorBackgroundGray);
    }

    public void showErrorView(int colorRes) {
        rlError.setVisibility(View.VISIBLE);
        rlError.setBackgroundResource(colorRes);
    }

    public void hideErrorView() {
        rlError.setVisibility(View.GONE);
    }

}
