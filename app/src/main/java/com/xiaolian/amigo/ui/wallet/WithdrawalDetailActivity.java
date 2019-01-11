package com.xiaolian.amigo.ui.wallet;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ObjectsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.ComplaintType;
import com.xiaolian.amigo.data.enumeration.PayWay;
import com.xiaolian.amigo.data.enumeration.WithdrawalStatus;
import com.xiaolian.amigo.data.network.model.funds.FundsDTO;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.wallet.adaptor.TitleContentCopyDelegate;
import com.xiaolian.amigo.ui.wallet.adaptor.TitleContentDelegate;
import com.xiaolian.amigo.ui.wallet.adaptor.WithdrawRechargeDetailAdapter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalDetailPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalDetailView;
import com.xiaolian.amigo.ui.widget.RecycleViewDivider;
import com.xiaolian.amigo.ui.widget.dialog.IOSAlertDialog;
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
 *
 * @author zcd
 * @date 17/10/23
 */

public class WithdrawalDetailActivity extends WalletBaseActivity implements IWithdrawalDetailView {

    @Inject
    IWithdrawalDetailPresenter<IWithdrawalDetailView> presenter;

    private List<WithdrawRechargeDetailAdapter.Item> items = new ArrayList<>();

    private WithdrawRechargeDetailAdapter adapter;

    @BindView(R.id.left_oper)
    TextView leftOper;
    @BindView(R.id.right_oper)
    TextView rightOper;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;

    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_status)
    TextView tvStatus;

    @BindView(R.id.ll_reason)
    LinearLayout llReason;

    @BindView(R.id.tv_reason)
    TextView tvReason;

    @BindView(R.id.tv_reason_content)
    TextView tvReasonContent;

    @BindView(R.id.tv_cancel_withdraw)
    TextView tvCancelWithdraw;

    @BindView(R.id.tv_reason_top)
    TextView tvReasonTop;

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
        }else {
            presenter.requestData(id);
        }
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
        tvAmount.setText(getString(R.string.money_format, data.getAmount()));
        tvStatus.setText(WithdrawalStatus.getWithdrawalStatus(data.getStatus()).getDesc());
        tvStatus.setTextColor(
                ContextCompat.getColor(this, WithdrawalStatus.getWithdrawalStatus(data.getStatus()).getColorRes()));
        if (WithdrawalStatus.getWithdrawalStatus(data.getStatus())
                == WithdrawalStatus.AUDIT_PENDING) {
            tvCancelWithdraw.setVisibility(View.VISIBLE);
        } else {
            tvCancelWithdraw.setVisibility(View.GONE);
        }
        if (ObjectsCompat.equals(data.getStatus(), WithdrawalStatus.AUDIT_FAIL.getType())
                && !TextUtils.isEmpty(data.getReason())) {
            llReason.setVisibility(View.VISIBLE);
            tvReasonContent.setText(data.getReason());
            tvReason.setText(getString(R.string.unpass_reason));
        }
        if (WithdrawalStatus.getWithdrawalStatus(data.getStatus())
                == WithdrawalStatus.WITHDRAWAL_FAIL && !TextUtils.isEmpty(data.getReason())) {
            tvReasonTop.setVisibility(View.VISIBLE);
            tvReasonTop.setText(data.getReason());
        }
        if (WithdrawalStatus.getWithdrawalStatus(data.getStatus()) == WithdrawalStatus.WITHDRAWAL_CANCEL) {
            llBottom.setVisibility(View.GONE);
        } else {
            llBottom.setVisibility(View.VISIBLE);
            leftOper.setText(WithdrawalStatus.getWithdrawalStatus(data.getStatus()).getNextOperations()[0]);
            rightOper.setText(WithdrawalStatus.getWithdrawalStatus(data.getStatus()).getNextOperations()[1]);
            leftOper.setOnClickListener((v) -> {
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
            rightOper.setOnClickListener((v) -> {
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
                PayWay.getPayWay(
                        data.getThirdAccountType() == null ?
                                0 : data.getThirdAccountType()
                ).getDesc(), 1));
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
        new IOSAlertDialog(this).builder()
                .setMsg("确认取消提现？")
                .setPositiveButton("确认", v -> presenter.cancelWithdraw(id))
                .setNegativeClickListener("取消", IOSAlertDialog::dismiss).show();
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
                        + "?accessToken=" + presenter.getAccessToken()
                        + "&refreshToken=" + presenter.getRefreshToken()
                        + "&orderId=" + id
                        + "&orderNo=" + orderNo
                        + "&orderType=" + ComplaintType.WITHDRAW.getType()));
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
