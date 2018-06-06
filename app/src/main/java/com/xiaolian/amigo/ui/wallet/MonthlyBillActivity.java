package com.xiaolian.amigo.ui.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.userbill.UserMonthlyBillConsumeRespDTO;
import com.xiaolian.amigo.data.network.model.userbill.UserMonthlyBillRespDTO;
import com.xiaolian.amigo.ui.order.OrderActivity;
import com.xiaolian.amigo.ui.wallet.intf.IMonthlyBillPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IMonthlyBillView;
import com.xiaolian.amigo.ui.widget.MonthlyBillView;
import com.xiaolian.amigo.util.Constant;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zcd
 * @date 18/6/1
 */
public class MonthlyBillActivity extends WalletBaseActivity implements IMonthlyBillView,
    MonthlyBillView.OnSelectedListener {

    @Inject
    IMonthlyBillPresenter<IMonthlyBillView> presenter;

    @BindView(R.id.monthlyBillView)
    MonthlyBillView monthlyBillView;

    @BindView(R.id.tv_monthly_order_date)
    TextView tvMonthlyOrderDate;

    @BindView(R.id.tv_total_consume)
    TextView tvTotalConsume;

    @BindView(R.id.tv_max_consume)
    TextView tvMaxConsume;

    @BindView(R.id.tv_total_recharge)
    TextView tvTotalRecharge;

    @BindView(R.id.tv_total_withdraw)
    TextView tvTotalWithdraw;

    private Integer currentYear;
    private Integer currentMonth;

    private DecimalFormat df = new DecimalFormat("###.##");

    private List<MonthlyBillView.ViewData> dataList = new ArrayList<MonthlyBillView.ViewData>(){
        {
//            add(new MonthlyBillView.ViewData(50.2, Device.HEATER.getColorRes(), Device.HEATER.getDesc()));
//            add(new MonthlyBillView.ViewData(3, Device.WASHER.getColorRes(), Device.WASHER.getDesc()));
//            add(new MonthlyBillView.ViewData(10.01, Device.DISPENSER.getColorRes(), Device.DISPENSER.getDesc()));
//            add(new MonthlyBillView.ViewData(6.10, Device.DRYER.getColorRes(), Device.DRYER.getDesc()));
        }
    };

    @Override
    protected void initView() {
        setMainBackground(R.color.colorBackgroundGray);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        monthlyBillView.setOnSelectedListener(this);
        initData();
    }

    private void initData() {
        Calendar cal = Calendar.getInstance();
        currentYear = cal.get(Calendar.YEAR);
        currentMonth = cal.get(Calendar.MONTH )+1;
        presenter.getMonthlyBill(currentYear, currentMonth);
    }

    @Override
    protected int setTitle() {
        return R.string.bill_detail;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_monthly_bill;
    }

    @OnClick(R.id.tv_monthly_order_date)
    public void gotoMonthlyOrder() {
        Bundle bundle = new Bundle();
        bundle.putInt(WalletConstant.KEY_YEAR, currentYear);
        bundle.putInt(WalletConstant.KEY_MONTH, currentMonth);
        startActivity(new Intent(this, OrderActivity.class)
                .putExtra(Constant.DATA_BUNDLE, bundle));
    }

    @OnClick(R.id.tv_total_recharge)
    public void gotoRechargeList() {
        Bundle bundle = new Bundle();
        bundle.putInt(WalletConstant.KEY_YEAR, currentYear);
        bundle.putInt(WalletConstant.KEY_MONTH, currentMonth);
        bundle.putInt(WalletConstant.KEY_FUNDS_TYPE, 1);
        startActivity(new Intent(this, WithdrawalRecordActivity.class)
                .putExtra(Constant.DATA_BUNDLE, bundle));
    }

    @OnClick(R.id.tv_total_withdraw)
    public void gotoWithdrawList() {
        Bundle bundle = new Bundle();
        bundle.putInt(WalletConstant.KEY_YEAR, currentYear);
        bundle.putInt(WalletConstant.KEY_MONTH, currentMonth);
        bundle.putInt(WalletConstant.KEY_FUNDS_TYPE, 2);
        startActivity(new Intent(this, WithdrawalRecordActivity.class)
                .putExtra(Constant.DATA_BUNDLE, bundle));
    }

    @Override
    public void render(UserMonthlyBillRespDTO data) {
        dataList.clear();
        for (UserMonthlyBillConsumeRespDTO consume : data.getConsumeTypes()) {
            dataList.add(new MonthlyBillView.ViewData(consume.getConsume(),
                    Device.getDevice(consume.getConsumeType()).getColorRes(),
                    consume.getConsumeTypeName(), consume.getConsumeType()));
        }
        monthlyBillView.setData(dataList);
        tvMonthlyOrderDate.setText(String.format(Locale.getDefault(), "%d年%d月", currentYear, currentMonth));
        tvTotalConsume.setText(String.format(Locale.getDefault(), "消费总额：¥%s", df.format(data.getTotalConsume())));
        tvMaxConsume.setText(String.format(Locale.getDefault(), "单笔消费最贵：¥%s", df.format(data.getMaxConsume())));
        tvTotalRecharge.setText(String.format(Locale.getDefault(), "充值总额：¥%s", df.format(data.getTotalRecharge())));
        tvTotalWithdraw.setText(String.format(Locale.getDefault(), "提现总额：¥%s", df.format(data.getTotalWithdraw())));
    }

    @Override
    public void onSelected(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(WalletConstant.KEY_YEAR, currentYear);
        bundle.putInt(WalletConstant.KEY_MONTH, currentMonth);
        bundle.putInt(WalletConstant.KEY_DEVICE_TYPE, dataList.get(position).getDeviceType());
        startActivity(new Intent(this, OrderActivity.class)
                .putExtra(Constant.DATA_BUNDLE, bundle));
    }
}
