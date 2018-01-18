package com.xiaolian.amigo.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.ComplaintType;
import com.xiaolian.amigo.data.network.model.funds.FundsDTO;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.order.intf.INormalOrderPresenter;
import com.xiaolian.amigo.ui.order.intf.INormalOrderView;
import com.xiaolian.amigo.ui.wallet.adaptor.TitleContentCopyDelegate;
import com.xiaolian.amigo.ui.wallet.adaptor.TitleContentDelegate;
import com.xiaolian.amigo.ui.wallet.adaptor.WithdrawRechargeDetailAdapter;
import com.xiaolian.amigo.ui.wallet.intf.IRechargeDetailPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IRechargeDetailView;
import com.xiaolian.amigo.ui.widget.RecycleViewDivider;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 消费账单
 * <p>
 * Created by zcd on 10/23/17.
 */

public class NormalOrderActivity extends OrderBaseActivity implements INormalOrderView {
    @Inject
    INormalOrderPresenter<INormalOrderView> presenter;

    private List<WithdrawRechargeDetailAdapter.Item> items = new ArrayList<>();

    private WithdrawRechargeDetailAdapter adapter;


    // orderId
    private Long id;
    private String orderNo;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_order);
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        bindView();
        setUp();
        initView();
    }

    private void bindView() {
        findViewById(R.id.left_oper).setOnClickListener(v -> onLeftOper());
        findViewById(R.id.right_oper).setOnClickListener(v -> onRightOper());
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void initView() {
        adapter = new WithdrawRechargeDetailAdapter(this, items);
        TitleContentCopyDelegate contentCopyDelegate = new TitleContentCopyDelegate();
        contentCopyDelegate.setCopyListener((string -> {
            CommonUtil.copy(string, getApplicationContext());
            onSuccess(R.string.copy_success);
        }));
        adapter.addItemViewDelegate(contentCopyDelegate);
        adapter.addItemViewDelegate(new TitleContentDelegate());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(this, RecycleViewDivider.VERTICAL_LIST));
        recyclerView.setAdapter(adapter);

        if (id == null || id == Constant.INVALID_ID) {
            onError("状态错误");
        }
//        presenter.requestData(id);
    }

    @Override
    protected void setUp() {
        if (getIntent() != null) {
            id = getIntent().getLongExtra(Constant.EXTRA_KEY, Constant.INVALID_ID);
        }
    }


//    @Override
//    public void render(FundsDTO data) {
//        orderNo = data.getOrderNo();
//        if (data.getInstead() != null && data.getInstead()) {
//            tv_status.setText(RechargeStatus.BEHALF_OF_RECHARGE.getDesc());
//            tv_status.setTextColor(
//                    ContextCompat.getColor(this,RechargeStatus.BEHALF_OF_RECHARGE.getColorRes()));
//            tv_reason_top.setVisibility(View.VISIBLE);
//            tv_reason_top.setText("笑联工作人员代充值");
//            left_oper.setText(RechargeStatus.BEHALF_OF_RECHARGE.getNextOperations()[0]);
//            right_oper.setText(RechargeStatus.BEHALF_OF_RECHARGE.getNextOperations()[1]);
//            left_oper.setOnClickListener(v ->
//                    startActivity(new Intent(this, WebActivity.class)
//                        .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_HELP)));
//            right_oper.setOnClickListener(v ->
//                    presenter.complaint(id, ComplaintType.RECHARGE.getType()));
//            items.add(new WithdrawRechargeDetailAdapter.Item("被充值手机号：",
//                    presenter.getMobile(), 1));
//            items.add(new WithdrawRechargeDetailAdapter.Item("被充值时间：",
//                    TimeUtils.millis2String(data.getCreateTime()), 1));
//            items.add(new WithdrawRechargeDetailAdapter.Item("流水号：",
//                    data.getOrderNo(), 2));
//            items.add(new WithdrawRechargeDetailAdapter.Item(null, null, 1));
//            adapter.notifyDataSetChanged();
//            return;
//        }
//        tv_status.setText(RechargeStatus.getRechargeStatus(data.getStatus()).getDesc());
//        tv_status.setTextColor(
//                ContextCompat.getColor(this,RechargeStatus.getRechargeStatus(data.getStatus()).getColorRes()));
//        if (CommonUtil.equals(data.getStatus(), RechargeStatus.AUDIT_FAIL.getType())
//                && !TextUtils.isEmpty(data.getReason())) {
//            ll_reason.setVisibility(View.VISIBLE);
//            tv_reason_content.setText(data.getReason());
//            tv_reason.setText(getString(R.string.unpass_reason));
//        }
//
//        if (RechargeStatus.getRechargeStatus(data.getStatus())
//                == RechargeStatus.RECHARGE_FAIL && !TextUtils.isEmpty(data.getReason())) {
//            tv_reason_top.setVisibility(View.VISIBLE);
//            tv_reason_top.setText(data.getReason());
//        }
//
//        left_oper.setText(RechargeStatus.getRechargeStatus(data.getStatus()).getNextOperations()[0]);
//        right_oper.setText(RechargeStatus.getRechargeStatus(data.getStatus()).getNextOperations()[1]);
//        left_oper.setOnClickListener((v) -> {
//            switch (RechargeStatus.getRechargeStatus(data.getStatus())) {
//                case AUDIT_PENDING:
//                    // 提现客服尽快处理
//                    presenter.remind(data.getId());
//                    break;
//                default:
//                    startActivity(new Intent(this, WebActivity.class)
//                            .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_HELP));
//                    break;
//            }
//        });
//        right_oper.setOnClickListener((v) -> {
//            switch (RechargeStatus.getRechargeStatus(data.getStatus())) {
//                case AUDIT_PENDING:
//                    startActivity(new Intent(this, WebActivity.class)
//                            .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_HELP));
//                    break;
//                case AUDIT_FAIL:
//                case RECHARGE_FAIL:
//                    CommonUtil.call(NormalOrderActivity.this, data.getCsMobile());
//                    break;
//                default:
//                    presenter.complaint(id, ComplaintType.RECHARGE.getType());
//                    break;
//            }
//        });
//
//        items.add(new WithdrawRechargeDetailAdapter.Item("充值方式：",
//                PayWay.getPayWay(
//                        data.getThirdAccountType() == null ? 0 : data.getThirdAccountType()
//                ).getDesc(), 1));
//        items.add(new WithdrawRechargeDetailAdapter.Item("充值账号：",
//                data.getThirdAccountName(), 1));
//        items.add(new WithdrawRechargeDetailAdapter.Item("充值时间：",
//                TimeUtils.millis2String(data.getCreateTime()), 1));
//        items.add(new WithdrawRechargeDetailAdapter.Item("流水号：",
//                data.getOrderNo(), 2));
//        items.add(new WithdrawRechargeDetailAdapter.Item(null, null, 1));
//        adapter.notifyDataSetChanged();
//    }

//    @Override
//    public void toComplaint() {
//        startActivity(new Intent(this, WebActivity.class)
//                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_COMPLAINT
//                        + "?token=" + presenter.getToken()
//                        + "&orderId=" + id
//                        + "&orderNo=" + orderNo
//                        + "&orderType=" + ComplaintType.WITHDRAW.getType()));
//    }

    public void onLeftOper() {
        startActivity(new Intent(this, WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_HELP));
    }

    public void onRightOper() {
//        startActivity(new Intent(this, WebActivity.class)
//                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_COMPLAINT
//                        + "?token=" + presenter.getToken()
//                        + "&orderId=" + id
//                        + "&orderNo=" + orderNo
//                        + "&orderType=" + ComplaintType.RECHARGE.getType()));
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
