package com.xiaolian.amigo.ui.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.userbill.UserMonthlyBillConsumeRespDTO;
import com.xiaolian.amigo.data.network.model.userbill.UserMonthlyBillRespDTO;
import com.xiaolian.amigo.ui.order.OrderActivity;
import com.xiaolian.amigo.ui.widget.MonthlyBillView;
import com.xiaolian.amigo.ui.widget.dialog.YearMonthPickerDialog;
import com.xiaolian.amigo.util.Constant;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BalanceStatisticsFragment extends Fragment implements MonthlyBillView.OnSelectedListener {
    @BindView(R.id.monthlyBillView)
    MonthlyBillView monthlyBillView;

    @BindView(R.id.tv_monthly_order_date)
    TextView tvMonthlyOrderDate;

    @BindView(R.id.tv_total_consume)
    TextView tvTotalConsume;

    @BindView(R.id.tv_max_consume)
    TextView tvMaxConsume;

    @BindView(R.id.tv_total_consume_withdraw)
    TextView tvConsumWithdraw;//新增消费退款字段

    @BindView(R.id.tv_total_recharge)
    TextView tvTotalRecharge;

    @BindView(R.id.tv_total_withdraw)
    TextView tvTotalWithdraw;

    private Integer currentYear;
    private Integer currentMonth;
    YearMonthPickerDialog yearMonthPickerDialog;

    private DecimalFormat df = new DecimalFormat("###.##");

    private List<MonthlyBillView.ViewData> dataList = new ArrayList<MonthlyBillView.ViewData>(){
        {
//            add(new MonthlyBillView.ViewData(50.2, Device.HEATER.getColorRes(), Device.HEATER.getDesc()));
//            add(new MonthlyBillView.ViewData(3, Device.WASHER.getColorRes(), Device.WASHER.getDesc()));
//            add(new MonthlyBillView.ViewData(10.01, Device.DISPENSER.getColorRes(), Device.DISPENSER.getDesc()));
//            add(new MonthlyBillView.ViewData(6.10, Device.DRYER.getColorRes(), Device.DRYER.getDesc()));
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_balance_statistics_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        monthlyBillView.setOnSelectedListener(this);
        initData();
    }

    private void initData() {
        Calendar cal = Calendar.getInstance();
        currentYear = cal.get(Calendar.YEAR);
        currentMonth = cal.get(Calendar.MONTH )+1;
        ((BalanceDetailListActivity)getActivity()).presenter.getMonthlyBill(currentYear, currentMonth);
    }

    @OnClick(R.id.tv_max_consume)
    public void gotoMonthlyMaxOrder() {
        Bundle bundle = new Bundle();
        bundle.putInt(WalletConstant.KEY_YEAR, currentYear);
        bundle.putInt(WalletConstant.KEY_MONTH, currentMonth);
        bundle.putInt(WalletConstant.KEY_ORDER_STATUS, 2);
        bundle.putInt(WalletConstant.KEY_MAX_ORDER, WalletConstant.ACTION_MAX_ORDER);
        startActivity(new Intent(getActivity(), OrderActivity.class)
                .putExtra(Constant.DATA_BUNDLE, bundle));
    }

    @OnClick(R.id.tv_total_consume)
    public void gotoMonthlyOrder() {
        Bundle bundle = new Bundle();
        bundle.putInt(WalletConstant.KEY_YEAR, currentYear);
        bundle.putInt(WalletConstant.KEY_MONTH, currentMonth);
        bundle.putInt(WalletConstant.KEY_ORDER_STATUS, 2);
        startActivity(new Intent(getActivity(), OrderActivity.class)
                .putExtra(Constant.DATA_BUNDLE, bundle));
    }

    @OnClick(R.id.tv_total_consume_withdraw)
    public void gotoMonthlyOrderWithdraw() {
        Bundle bundle = new Bundle();
        bundle.putInt(WalletConstant.KEY_YEAR, currentYear);
        bundle.putInt(WalletConstant.KEY_MONTH, currentMonth);
        bundle.putInt(WalletConstant.KEY_ORDER_STATUS, 3);
        startActivity(new Intent(getActivity(), OrderActivity.class)
                .putExtra(Constant.DATA_BUNDLE, bundle));
    }

    @OnClick(R.id.tv_total_recharge)
    public void gotoRechargeList() {
        Bundle bundle = new Bundle();
        bundle.putInt(WalletConstant.KEY_YEAR, currentYear);
        bundle.putInt(WalletConstant.KEY_MONTH, currentMonth);
        bundle.putInt(WalletConstant.KEY_FUNDS_TYPE, 1);
        startActivity(new Intent(getActivity(), WithdrawalRecordActivity.class)
                .putExtra(Constant.DATA_BUNDLE, bundle));
    }

    @OnClick(R.id.tv_total_withdraw)
    public void gotoWithdrawList() {
        Bundle bundle = new Bundle();
        bundle.putInt(WalletConstant.KEY_YEAR, currentYear);
        bundle.putInt(WalletConstant.KEY_MONTH, currentMonth);
        bundle.putInt(WalletConstant.KEY_FUNDS_TYPE, 2);
        startActivity(new Intent(getActivity(), WithdrawalRecordActivity.class)
                .putExtra(Constant.DATA_BUNDLE, bundle));
    }

    @OnClick(R.id.tv_monthly_order_date)
    public void showDatePick() {
        if (yearMonthPickerDialog == null) {
            Long timestamps = ((BalanceDetailListActivity)getActivity()).presenter.getAccountCreateTime();
            yearMonthPickerDialog = new YearMonthPickerDialog(getActivity(), timestamps);
        }
        yearMonthPickerDialog.setOnItemSelectedListener((picker, date) -> {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            currentYear = cal.get(Calendar.YEAR);
            currentMonth = cal.get(Calendar.MONTH) + 1;
//            tvMonthlyOrderDate.setText(String.format(Locale.getDefault(), "%d年%d月", currentYear, currentMonth));
            ((BalanceDetailListActivity)getActivity()).presenter.getMonthlyBill(currentYear, currentMonth);
        });
        yearMonthPickerDialog.show();
    }


    //获取数据后的回调
    public void render(UserMonthlyBillRespDTO data) {
        dataList.clear();
        for (UserMonthlyBillConsumeRespDTO consume : data.getConsumeTypes()) {
            dataList.add(new MonthlyBillView.ViewData(consume.getConsume(),
                    Device.getDevice(consume.getConsumeType()).getColorRes(),
                    consume.getConsumeTypeName(), consume.getConsumeType()));
        }
        if (dataList.isEmpty()) {
            dataList.add(new MonthlyBillView.ViewData(1, R.color.colorDarkB, "", 1));
            monthlyBillView.setHasData(false);
            monthlyBillView.setData(dataList);
        } else {
            monthlyBillView.setHasData(true);
            monthlyBillView.setData(dataList);
        }
        tvMonthlyOrderDate.setText(String.format(Locale.getDefault(), "%d年%d月", currentYear, currentMonth));
        tvTotalConsume.setText(String.format(Locale.getDefault(), "实际消费：¥%s", df.format(data.getTotalConsume())));
        tvMaxConsume.setText(String.format(Locale.getDefault(), "单笔消费最贵：¥%s", df.format(data.getMaxConsume())));
        tvTotalRecharge.setText(String.format(Locale.getDefault(), "余额充值：¥%s", df.format(data.getTotalRecharge())));
        tvTotalWithdraw.setText(String.format(Locale.getDefault(), "余额退款：¥%s", df.format(data.getTotalWithdraw())));
        tvConsumWithdraw.setText(String.format(Locale.getDefault(), "消费退款：¥%s", df.format(data.getRefunds())));
    }

    @Override
    public void onSelected(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(WalletConstant.KEY_YEAR, currentYear);
        bundle.putInt(WalletConstant.KEY_MONTH, currentMonth);
        bundle.putInt(WalletConstant.KEY_DEVICE_TYPE, dataList.get(position).getDeviceType());
        startActivity(new Intent(getActivity(), OrderActivity.class)
                .putExtra(Constant.DATA_BUNDLE, bundle));
    }
}
