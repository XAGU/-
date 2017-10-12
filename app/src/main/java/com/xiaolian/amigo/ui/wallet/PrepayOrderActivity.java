package com.xiaolian.amigo.ui.wallet;

import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.wallet.adaptor.PrepayAdaptor;
import com.xiaolian.amigo.ui.wallet.intf.IPrepayOrderPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IPrepayOrderView;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 待找零账单
 * <p>
 * Created by zcd on 10/12/17.
 */

public class PrepayOrderActivity extends WalletBaseActivity implements IPrepayOrderView {
    @Inject
    IPrepayOrderPresenter<IPrepayOrderView> presenter;

    /**
     * 使用时间
     */
    @BindView(R.id.tv_time)
    TextView tv_time;

    /**
     * 设备位置
     */
    @BindView(R.id.tv_location)
    TextView tv_location;

    /**
     * 订单号
     */
    @BindView(R.id.tv_order_no)
    TextView tv_order_no;

    /**
     * 支付方式
     */
    @BindView(R.id.tv_pay_method)
    TextView tv_pay_method;

    private PrepayAdaptor.OrderWrapper orderWrapper;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(PrepayOrderActivity.this);

        if (orderWrapper != null) {
            tv_time.setText(CommonUtil.stampToDate(orderWrapper.getTime()));
            tv_location.setText(orderWrapper.getOrder().getLocation());
            tv_order_no.setText(orderWrapper.getOrder().getOrderNo());
            tv_pay_method.setText(orderWrapper.getOrder().getPaymentType() == 1 ?
                    "余额支付" : "红包支付");
        }
    }

    @Override
    protected void setUp() {
        super.setUp();
        orderWrapper = (PrepayAdaptor.OrderWrapper) getIntent().getSerializableExtra(Constant.EXTRA_KEY);
    }

    @Override
    protected int setTitle() {
        return R.string.prepay_order;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_prepay_order;
    }
}
