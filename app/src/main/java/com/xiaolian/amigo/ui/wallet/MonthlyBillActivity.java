package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.ui.wallet.intf.IMonthlyBillPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IMonthlyBillView;
import com.xiaolian.amigo.ui.widget.MonthlyBillView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zcd
 * @date 18/6/1
 */
public class MonthlyBillActivity extends WalletBaseActivity implements IMonthlyBillView {

    @Inject
    IMonthlyBillPresenter<IMonthlyBillView> presenter;

    @BindView(R.id.monthlyBillView)
    MonthlyBillView monthlyBillView;

    private List<MonthlyBillView.ViewData> dataList = new ArrayList<MonthlyBillView.ViewData>(){
        {
            add(new MonthlyBillView.ViewData(1, Device.HEATER.getColorRes(), Device.HEATER.getDesc()));
            add(new MonthlyBillView.ViewData(1, Device.DISPENSER.getColorRes(), Device.DISPENSER.getDesc()));
            add(new MonthlyBillView.ViewData(1, Device.DRYER.getColorRes(), Device.DRYER.getDesc()));
        }
    };

    @Override
    protected void initView() {
        setMainBackground(R.color.colorBackgroundGray);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        monthlyBillView.setData(dataList);
    }

    @Override
    protected int setTitle() {
        return R.string.bill_detail;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_monthly_bill;
    }
}
