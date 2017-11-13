package com.xiaolian.amigo.ui.wallet;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.ComplaintType;
import com.xiaolian.amigo.data.enumeration.PayWay;
import com.xiaolian.amigo.data.enumeration.RechargeStatus;
import com.xiaolian.amigo.data.enumeration.WithdrawalStatus;
import com.xiaolian.amigo.data.network.model.dto.response.FundsDTO;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.wallet.adaptor.TitleContentCopyDelegate;
import com.xiaolian.amigo.ui.wallet.adaptor.TitleContentDelegate;
import com.xiaolian.amigo.ui.wallet.adaptor.WithdrawRechargeDetailAdapter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalDetailPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalDetailView;
import com.xiaolian.amigo.ui.widget.RecycleViewDivider;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 提现详情
 * <p>
 * Created by zcd on 10/23/17.
 */

public class WithdrawalDetailActivity extends WalletBaseActivity implements IWithdrawalDetailView {

    @Inject
    IWithdrawalDetailPresenter<IWithdrawalDetailView> presenter;

    private List<WithdrawRechargeDetailAdapter.Item> items = new ArrayList<>();

    private WithdrawRechargeDetailAdapter adapter;

    @BindView(R.id.left_oper)
    TextView left_oper;
    @BindView(R.id.right_oper)
    TextView right_oper;
    @BindView(R.id.ll_bottom)
    LinearLayout ll_bottom;

    @BindView(R.id.tv_amount)
    TextView tv_amount;
    @BindView(R.id.tv_status)
    TextView tv_status;

    @BindView(R.id.ll_reason)
    LinearLayout ll_reason;

    @BindView(R.id.tv_reason)
    TextView tv_reason;

    @BindView(R.id.tv_reason_content)
    TextView tv_reason_content;

    @BindView(R.id.tv_cancel_withdraw)
    TextView tv_cancel_withdraw;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Long id;
    private String orderNo;
    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(WithdrawalDetailActivity.this);

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

        if (id == null || id == -1) {
            onError("状态错误");
        }
        presenter.requestData(id);
    }

    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null) {
            id = getIntent().getLongExtra(Constant.EXTRA_KEY, -1);
        }
    }

    @Override
    protected int setTitle() {
        return R.string.withdraw_detail;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_recharge_detail;
    }

    @Override
    public void render(FundsDTO data) {
        orderNo = data.getOrderNo();
        tv_amount.setText(getString(R.string.money_format, data.getAmount()));
        tv_status.setText(WithdrawalStatus.getWithdrawalStatus(data.getStatus()).getDesc());
        tv_status.setTextColor(
                ContextCompat.getColor(this,WithdrawalStatus.getWithdrawalStatus(data.getStatus()).getColorRes()));
        if (WithdrawalStatus.getWithdrawalStatus(data.getStatus()) == WithdrawalStatus.AUDIT_PENDING) {
            tv_cancel_withdraw.setVisibility(View.VISIBLE);
        } else {
            tv_cancel_withdraw.setVisibility(View.GONE);
        }
        if (CommonUtil.equals(data.getStatus(), WithdrawalStatus.AUDIT_FAIL)
                && !TextUtils.isEmpty(data.getReason())) {
            ll_reason.setVisibility(View.VISIBLE);
            tv_reason_content.setText(data.getReason());
            tv_reason.setText(getString(R.string.unpass_reason));
        }
        if (WithdrawalStatus.getWithdrawalStatus(data.getStatus()) == WithdrawalStatus.WITHDRAWAL_CANCEL) {
            ll_bottom.setVisibility(View.GONE);
        } else {
            ll_bottom.setVisibility(View.VISIBLE);
            left_oper.setText(WithdrawalStatus.getWithdrawalStatus(data.getStatus()).getNextOperations()[0]);
            right_oper.setText(WithdrawalStatus.getWithdrawalStatus(data.getStatus()).getNextOperations()[1]);
            left_oper.setOnClickListener((v) -> {
                switch (WithdrawalStatus.getWithdrawalStatus(data.getStatus())) {
                    case AUDIT_PENDING:
                        // 提现客服尽快处理
                        presenter.remind(data.getId());
                        break;
                    default:
                        startActivity(new Intent(this, WebActivity.class)
                                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_HELP));
                        break;
                }
            });
            right_oper.setOnClickListener((v) -> {
                switch (WithdrawalStatus.getWithdrawalStatus(data.getStatus())) {
                    case AUDIT_PENDING:
                        startActivity(new Intent(this, WebActivity.class)
                                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_HELP));
                        break;
                    case AUDIT_FAIL:
                    case WITHDRAWAL_FAIL:
                        CommonUtil.call(WithdrawalDetailActivity.this, data.getCsMobile());
                        break;
                    default:
                        presenter.complaint(id, ComplaintType.WITHDRAW.getType());
                        break;
                }
            });

        }
        items.add(new WithdrawRechargeDetailAdapter.Item(getString(R.string.withdraw_way_colon),
                PayWay.getPayWay(data.getThirdAccountType()).getDesc(), 1));
        items.add(new WithdrawRechargeDetailAdapter.Item(getString(R.string.withdraw_account_colon),
                data.getThirdAccountName(), 1));
        items.add(new WithdrawRechargeDetailAdapter.Item(getString(R.string.withdraw_time_colon),
                TimeUtils.millis2String(data.getCreateTime()), 1));
        items.add(new WithdrawRechargeDetailAdapter.Item(getString(R.string.serial_number_colon),
                data.getOrderNo(), 2));
        items.add(new WithdrawRechargeDetailAdapter.Item(null, null, 1));
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.tv_cancel_withdraw)
    void onCancelWithdrawClick() {
        presenter.cancelWithdraw(id);
    }

    @Override
    public void gotoBack() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public void toComplaint() {
        startActivity(new Intent(this, WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_COMPLAINT
                        + "?token=" + presenter.getToken()
                        + "&orderId=" + id
                        + "&orderNo=" + orderNo
                        + "&orderType=" + ComplaintType.WITHDRAW.getType()));
    }
}
