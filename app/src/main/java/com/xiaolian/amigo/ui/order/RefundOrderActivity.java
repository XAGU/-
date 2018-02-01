package com.xiaolian.amigo.ui.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.order.intf.IRefundOrderPresenter;
import com.xiaolian.amigo.ui.order.intf.IRefundOrderView;

import javax.inject.Inject;

/**
 * 退款账单
 *
 * @author zcd
 * @date 17/10/23
 */

public class RefundOrderActivity extends OrderBaseActivity implements IRefundOrderView {
    @Inject
    IRefundOrderPresenter<IRefundOrderView> presenter;

    private TextView tvAmount;
    private TextView tvReasonContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_order);
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        bindView();
        setUp();
    }

    private void bindView() {
        findViewById(R.id.left_oper).setOnClickListener(v -> gotoOrderDetail());
        findViewById(R.id.right_oper).setOnClickListener(v -> gotoScanQRCode());
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());

        tvAmount = findViewById(R.id.tv_amount);
        tvReasonContent = findViewById(R.id.tv_reason_content);
    }

    private void gotoScanQRCode() {

    }

    private void gotoOrderDetail() {

    }

    @Override
    protected void setUp() {
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
