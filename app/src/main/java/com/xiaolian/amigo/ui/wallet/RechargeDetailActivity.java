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
import com.xiaolian.amigo.data.network.model.funds.FundsDTO;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.wallet.adaptor.TitleContentCopyDelegate;
import com.xiaolian.amigo.ui.wallet.adaptor.TitleContentDelegate;
import com.xiaolian.amigo.ui.wallet.adaptor.WithdrawRechargeDetailAdapter;
import com.xiaolian.amigo.ui.wallet.intf.IRechargeDetailPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IRechargeDetailView;
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
 * 充值详情
 * <p>
 * Created by zcd on 10/23/17.
 */

public class RechargeDetailActivity extends WalletBaseActivity implements IRechargeDetailView {
    @Inject
    IRechargeDetailPresenter<IRechargeDetailView> presenter;

    private List<WithdrawRechargeDetailAdapter.Item> items = new ArrayList<>();

    private WithdrawRechargeDetailAdapter adapter;

    @BindView(R.id.left_oper)
    TextView left_oper;
    @BindView(R.id.right_oper)
    TextView right_oper;

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

    @BindView(R.id.tv_reason_top)
    TextView tv_reason_top;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    // orderId
    private Long id;
    private String orderNo;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(RechargeDetailActivity.this);

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
        presenter.requestData(id);
    }

    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null) {
            id = getIntent().getLongExtra(Constant.EXTRA_KEY, Constant.INVALID_ID);
        }
    }

    @Override
    protected int setTitle() {
        return R.string.recharge_detail;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_recharge_detail;
    }

    @Override
    public void render(FundsDTO data) {
        orderNo = data.getOrderNo();
        tv_amount.setText(getString(R.string.money_format, data.getAmount()));
        if (data.getInstead() != null && data.getInstead()) {
            tv_status.setText(RechargeStatus.BEHALF_OF_RECHARGE.getDesc());
            tv_status.setTextColor(
                    ContextCompat.getColor(this,RechargeStatus.BEHALF_OF_RECHARGE.getColorRes()));
            tv_reason_top.setVisibility(View.VISIBLE);
            tv_reason_top.setText("笑联工作人员代充值");
            left_oper.setText(RechargeStatus.BEHALF_OF_RECHARGE.getNextOperations()[0]);
            right_oper.setText(RechargeStatus.BEHALF_OF_RECHARGE.getNextOperations()[1]);
            left_oper.setOnClickListener(v ->
                    startActivity(new Intent(this, WebActivity.class)
                        .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_HELP)));
            right_oper.setOnClickListener(v ->
                    presenter.complaint(id, ComplaintType.RECHARGE.getType()));
            items.add(new WithdrawRechargeDetailAdapter.Item("被充值手机号：",
                    presenter.getMobile(), 1));
            items.add(new WithdrawRechargeDetailAdapter.Item("被充值时间：",
                    TimeUtils.millis2String(data.getCreateTime()), 1));
            items.add(new WithdrawRechargeDetailAdapter.Item("流水号：",
                    data.getOrderNo(), 2));
            items.add(new WithdrawRechargeDetailAdapter.Item(null, null, 1));
            adapter.notifyDataSetChanged();
            return;
        }
        tv_status.setText(RechargeStatus.getRechargeStatus(data.getStatus()).getDesc());
        tv_status.setTextColor(
                ContextCompat.getColor(this,RechargeStatus.getRechargeStatus(data.getStatus()).getColorRes()));
        if (CommonUtil.equals(data.getStatus(), RechargeStatus.AUDIT_FAIL.getType())
                && !TextUtils.isEmpty(data.getReason())) {
            ll_reason.setVisibility(View.VISIBLE);
            tv_reason_content.setText(data.getReason());
            tv_reason.setText(getString(R.string.unpass_reason));
        }

        if (RechargeStatus.getRechargeStatus(data.getStatus())
                == RechargeStatus.RECHARGE_FAIL && !TextUtils.isEmpty(data.getReason())) {
            tv_reason_top.setVisibility(View.VISIBLE);
            tv_reason_top.setText(data.getReason());
        }

        left_oper.setText(RechargeStatus.getRechargeStatus(data.getStatus()).getNextOperations()[0]);
        right_oper.setText(RechargeStatus.getRechargeStatus(data.getStatus()).getNextOperations()[1]);
        left_oper.setOnClickListener((v) -> {
            switch (RechargeStatus.getRechargeStatus(data.getStatus())) {
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
            switch (RechargeStatus.getRechargeStatus(data.getStatus())) {
                case AUDIT_PENDING:
                    startActivity(new Intent(this, WebActivity.class)
                            .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_HELP));
                    break;
                case AUDIT_FAIL:
                case RECHARGE_FAIL:
                    CommonUtil.call(RechargeDetailActivity.this, data.getCsMobile());
                    break;
                default:
                    presenter.complaint(id, ComplaintType.RECHARGE.getType());
                    break;
            }
        });

        items.add(new WithdrawRechargeDetailAdapter.Item("充值方式：",
                PayWay.getPayWay(
                        data.getThirdAccountType() == null ? 0 : data.getThirdAccountType()
                ).getDesc(), 1));
        items.add(new WithdrawRechargeDetailAdapter.Item("充值账号：",
                data.getThirdAccountName(), 1));
        items.add(new WithdrawRechargeDetailAdapter.Item("充值时间：",
                TimeUtils.millis2String(data.getCreateTime()), 1));
        items.add(new WithdrawRechargeDetailAdapter.Item("流水号：",
                data.getOrderNo(), 2));
        items.add(new WithdrawRechargeDetailAdapter.Item(null, null, 1));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void toComplaint() {
        startActivity(new Intent(this, WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_COMPLAINT
                        + "?token=" + presenter.getToken()
                        + "&orderId=" + id
                        + "&orderNo=" + orderNo
                        + "&orderType=" + ComplaintType.RECHARGE.getType()));
    }

    @OnClick(R.id.left_oper)
    public void onLeftOper() {
        startActivity(new Intent(this, WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_HELP));
    }

    @OnClick(R.id.right_oper)
    public void onRightOper() {
        startActivity(new Intent(this, WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_COMPLAINT
                        + "?token=" + presenter.getToken()
                        + "&orderId=" + id
                        + "&orderNo=" + orderNo
                        + "&orderType=" + ComplaintType.RECHARGE.getType()));
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
