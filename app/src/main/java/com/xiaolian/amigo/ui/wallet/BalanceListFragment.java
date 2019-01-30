package com.xiaolian.amigo.ui.wallet;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hmy.popwindow.PopWindow;
import com.hmy.popwindow.viewinterface.PopWindowListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.wallet.adaptor.BillListAdaptor;
import com.xiaolian.amigo.ui.widget.dialog.YearMonthPickerDialog;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutFooter;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutHeader;
import com.xiaolian.amigo.ui.widget.popWindow.BillFilterStatusPopupWindow;
import com.xiaolian.amigo.ui.widget.popWindow.BillFilterTypePopupWindow;
import com.xiaolian.amigo.util.TimeUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;


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


    private PopWindow mBillFilterStatusPopwindow ;

    private PopWindow mBillFilterTypesPopWindow ;

    private String timeStr;

    private Long lastId;

    private Integer billType;

    private Integer billStatus;

    /**
     * 筛选弹窗控件
     */
    private  TextView filterAllTextView  ;
    private  TextView filterOngoingTextView  ;
    private  TextView filterEndTextView ;


    /**
     * 分类弹窗控件
     */
    private TextView typeFilterAllTextView;
    private TextView filterRechargeTextView;

    private TextView filterWithdrawTextView;

    private TextView filterBillTotalTextView;

    private TextView filterBillItem1TextView;
    private TextView filterBillItem2TextView;
    private TextView filterBillItem3TextView;
    private TextView filterBillItem4TextView;
    private TextView filterBillItem5TextView;
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
    }

    private void initTimeStr() {
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        int currentMonth = cal.get(Calendar.MONTH )+1;
        timeStr = String.valueOf(currentYear * 100 + currentMonth);
        tvMonthlyOrderDate.setText(String.format(Locale.getDefault(), "%d年%d月", currentYear, currentMonth));
    }



    //============= 弹窗  ================
    private void initBillFilterStatusPopView(View contentView){
         filterAllTextView = contentView.findViewById(R.id.filter_status_all);
         filterOngoingTextView = contentView.findViewById(R.id.filter_status_ongoing);
         filterEndTextView = contentView.findViewById(R.id.filter_status_end);

        filterAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence name = ((TextView)v).getText();
                showSelectedStatus(0);
                popFilterStatusClick(0  , name);
                mBillFilterStatusPopwindow.dismiss();
            }
        });

        filterOngoingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence name = ((TextView)v).getText();
                showSelectedStatus(1);
                popFilterStatusClick(1 , name);
                mBillFilterStatusPopwindow.dismiss();
            }
        });

        filterEndTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence name = ((TextView)v).getText();
                showSelectedStatus(2);
                popFilterStatusClick(2 , name);
                mBillFilterStatusPopwindow.dismiss();
            }
        });
    }

    private void showSelectedStatus(int status) {
        if (status == 0) {
            filterAllTextView.setTextColor(Color.parseColor("#FF5555"));
            filterOngoingTextView.setTextColor(Color.parseColor("#222222"));
            filterEndTextView.setTextColor(Color.parseColor("#222222"));
        } else if (status == 1) {
            filterAllTextView.setTextColor(Color.parseColor("#222222"));
            filterOngoingTextView.setTextColor(Color.parseColor("#FF5555"));
            filterEndTextView.setTextColor(Color.parseColor("#222222"));
        } else {
            filterAllTextView.setTextColor(Color.parseColor("#222222"));
            filterOngoingTextView.setTextColor(Color.parseColor("#222222"));
            filterEndTextView.setTextColor(Color.parseColor("#FF5555"));
        }
    }


    private void popFilterStatusClick(int status, CharSequence name){
        tvFilterStatus.setTextColor(Color.parseColor("#FF5555"));
        tvFilterStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.incomedown, 0);
        if (billStatus!=null && billStatus == status) /*选择的是一样的就不加载*/{
            return;
        }
        items.clear();
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

    // ==========================================


    // =========== 分类选项弹窗 ===========

    private void initBillFilterTypesPopView(View contentView){
        typeFilterAllTextView = contentView.findViewById(R.id.filter_type_all);
        filterRechargeTextView = contentView.findViewById(R.id.filter_type_recharge);
        filterWithdrawTextView = contentView.findViewById(R.id.filter_type_withdraw);
        filterBillTotalTextView = contentView.findViewById(R.id.filter_type_bill);

        filterBillItem1TextView = contentView.findViewById(R.id.filter_type_bill_item1);
        filterBillItem2TextView = contentView.findViewById(R.id.filter_type_bill_item2);
        filterBillItem3TextView = contentView.findViewById(R.id.filter_type_bill_item3);
        filterBillItem4TextView = contentView.findViewById(R.id.filter_type_bill_item4);
        filterBillItem5TextView = contentView.findViewById(R.id.filter_type_bill_item5);

        typeFilterAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(((TextView)v).getText());
                showSelectedType(0);
                popFilterTypeClick(0, name);
                mBillFilterTypesPopWindow.dismiss();
            }
        });

        filterRechargeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(((TextView)v).getText());
                showSelectedType(1);
                popFilterTypeClick(1, name);
                mBillFilterTypesPopWindow.dismiss();
            }
        });

        filterWithdrawTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(((TextView)v).getText());
                showSelectedType(2);
                popFilterTypeClick(2, name);
                mBillFilterTypesPopWindow.dismiss();
            }
        });

        filterBillTotalTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(((TextView)v).getText());
                showSelectedType(9);
                popFilterTypeClick(9, name);
                mBillFilterTypesPopWindow.dismiss();
            }
        });

        filterBillItem1TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(((TextView)v).getText());
                showSelectedType(((Long)v.getTag()).intValue());
                popFilterTypeClick(((Long)v.getTag()).intValue(), name);
                mBillFilterTypesPopWindow.dismiss();
            }
        });

        filterBillItem2TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(((TextView)v).getText());
                showSelectedType(((Long)v.getTag()).intValue());
                popFilterTypeClick(((Long)v.getTag()).intValue(), name);
                mBillFilterTypesPopWindow.dismiss();
            }
        });

        filterBillItem3TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(((TextView)v).getText());
                showSelectedType(((Long)v.getTag()).intValue());
                popFilterTypeClick(((Long)v.getTag()).intValue(), name);
                mBillFilterTypesPopWindow.dismiss();
            }
        });

        filterBillItem4TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(((TextView)v).getText());
                showSelectedType(((Long)v.getTag()).intValue());
                popFilterTypeClick(((Long)v.getTag()).intValue(), name);
                mBillFilterTypesPopWindow.dismiss();
            }
        });

        filterBillItem5TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(((TextView)v).getText());
                showSelectedType(((Long)v.getTag()).intValue());
                popFilterTypeClick(((Long)v.getTag()).intValue(), name);
                mBillFilterTypesPopWindow.dismiss();
            }
        });
    }

    private void showSelectedType(int type) {
        if (type == 0) {
            typeFilterAllTextView.setTextColor(Color.parseColor("#FF5555"));
            filterRechargeTextView.setTextColor(Color.parseColor("#222222"));
            filterWithdrawTextView.setTextColor(Color.parseColor("#222222"));
            filterBillTotalTextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem1TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem2TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem3TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem4TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem5TextView.setTextColor(Color.parseColor("#222222"));

        } else if (type == 1) {
            typeFilterAllTextView.setTextColor(Color.parseColor("#222222"));
            filterRechargeTextView.setTextColor(Color.parseColor("#FF5555"));
            filterWithdrawTextView.setTextColor(Color.parseColor("#222222"));
            filterBillTotalTextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem1TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem2TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem3TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem4TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem5TextView.setTextColor(Color.parseColor("#222222"));

        } else if (type == 2) {
            typeFilterAllTextView.setTextColor(Color.parseColor("#222222"));
            filterRechargeTextView.setTextColor(Color.parseColor("#222222"));
            filterWithdrawTextView.setTextColor(Color.parseColor("#FF5555"));
            filterBillTotalTextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem1TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem2TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem3TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem4TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem5TextView.setTextColor(Color.parseColor("#222222"));
        } else if (type == (Long)filterBillItem1TextView.getTag()) {
            typeFilterAllTextView.setTextColor(Color.parseColor("#222222"));
            filterRechargeTextView.setTextColor(Color.parseColor("#222222"));
            filterWithdrawTextView.setTextColor(Color.parseColor("#222222"));
            filterBillTotalTextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem1TextView.setTextColor(Color.parseColor("#FF5555"));
            filterBillItem2TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem3TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem4TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem5TextView.setTextColor(Color.parseColor("#222222"));
        } else if (type == (Long)filterBillItem2TextView.getTag()) {
            typeFilterAllTextView.setTextColor(Color.parseColor("#222222"));
            filterRechargeTextView.setTextColor(Color.parseColor("#222222"));
            filterWithdrawTextView.setTextColor(Color.parseColor("#222222"));
            filterBillTotalTextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem1TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem2TextView.setTextColor(Color.parseColor("#FF5555"));
            filterBillItem3TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem4TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem5TextView.setTextColor(Color.parseColor("#222222"));
        } else if (type == (Long)filterBillItem3TextView.getTag()) {
            typeFilterAllTextView.setTextColor(Color.parseColor("#222222"));
            filterRechargeTextView.setTextColor(Color.parseColor("#222222"));
            filterWithdrawTextView.setTextColor(Color.parseColor("#222222"));
            filterBillTotalTextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem1TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem2TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem3TextView.setTextColor(Color.parseColor("#FF5555"));
            filterBillItem4TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem5TextView.setTextColor(Color.parseColor("#222222"));
        } else if (type == (Long)filterBillItem4TextView.getTag()) {
            typeFilterAllTextView.setTextColor(Color.parseColor("#222222"));
            filterRechargeTextView.setTextColor(Color.parseColor("#222222"));
            filterWithdrawTextView.setTextColor(Color.parseColor("#222222"));
            filterBillTotalTextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem1TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem2TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem3TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem4TextView.setTextColor(Color.parseColor("#FF5555"));
            filterBillItem5TextView.setTextColor(Color.parseColor("#222222"));
        } else if (type == (Long)filterBillItem5TextView.getTag()) {
            typeFilterAllTextView.setTextColor(Color.parseColor("#222222"));
            filterRechargeTextView.setTextColor(Color.parseColor("#222222"));
            filterWithdrawTextView.setTextColor(Color.parseColor("#222222"));
            filterBillTotalTextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem1TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem2TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem3TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem4TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem5TextView.setTextColor(Color.parseColor("#FF5555"));
        } else if (type == 9) {
            typeFilterAllTextView.setTextColor(Color.parseColor("#222222"));
            filterRechargeTextView.setTextColor(Color.parseColor("#222222"));
            filterWithdrawTextView.setTextColor(Color.parseColor("#222222"));
            filterBillTotalTextView.setTextColor(Color.parseColor("#FF5555"));
            filterBillItem1TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem2TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem3TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem4TextView.setTextColor(Color.parseColor("#222222"));
            filterBillItem5TextView.setTextColor(Color.parseColor("#222222"));
        }
    }

    public void popFilterTypeClick(int type, String name) {
        tvFilterType.setTextColor(Color.parseColor("#FF5555"));
        tvFilterType.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.incomedown, 0);
        if (billType != null && billType == type) /*选择的是一样的就不加载*/{
            return;
        }
        items.clear();
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

    // =============================




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
        if (mBillFilterStatusPopwindow == null) {
            View customView = View.inflate(getContext(), R.layout.pop_bill_filter_status, null);
            initBillFilterStatusPopView(customView);
            mBillFilterStatusPopwindow =  new PopWindow.Builder(getActivity())
                    .setStyle(PopWindow.PopWindowStyle.PopDown)
                    .setView(customView)
                    .setPopWindowListener(new PopWindowListener() {
                        @Override
                        public void show() {
                            tvFilterStatus.setTextColor(Color.parseColor("#FF5555"));
                            tvFilterStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.income, 0);
                        }

                        @Override
                        public void dismiss() {
                            tvFilterStatus.setTextColor(Color.parseColor("#FF5555"));
                            tvFilterStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.incomedown, 0);
                        }
                    }).create();
        }
        mBillFilterStatusPopwindow.show(rlFilterContentView);
    }

    @OnClick(R.id.tv_filter_type)
    public void showFilterType() {
        if (mBillFilterTypesPopWindow == null){
            View filterTypeView = View.inflate(getContext(), R.layout.pop_bill_filter_type, null);
            initBillFilterTypesPopView(filterTypeView);
            mBillFilterTypesPopWindow = new PopWindow.Builder(getActivity())
                    .setStyle(PopWindow.PopWindowStyle.PopDown)
                    .setView(filterTypeView)
                    .setPopWindowListener(new PopWindowListener() {
                        @Override
                        public void show() {
                            tvFilterType.setTextColor(Color.parseColor("#FF5555"));
                            tvFilterType.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.income, 0);
                        }

                        @Override
                        public void dismiss() {
                            tvFilterType.setTextColor(Color.parseColor("#FF5555"));
                            tvFilterType.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.incomedown, 0);
                        }
                    }).create();

        }
        mBillFilterTypesPopWindow.show(rlFilterContentView);
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
            return;
        }

        if (items.size() <= 0)  /*第一次请求数据*/{
            //1、如果最新的一条不是当前选择的月份，则不展示出来，留到下次上拉或者下拉的时候再展示
            String newTimeStr = TimeUtils.millis2String(wrappers.get(0).getCreateTime(), TimeUtils.MY_DATE_YEARMON_FORMAT);
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
            rlEmpty.setBackgroundResource(colorRes);
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
