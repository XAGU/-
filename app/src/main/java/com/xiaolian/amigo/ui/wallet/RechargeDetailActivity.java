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
import com.xiaolian.amigo.data.enumeration.RechargeStatus;
import com.xiaolian.amigo.data.enumeration.WithdrawOperationType;
import com.xiaolian.amigo.data.network.model.dto.response.FundsDTO;
import com.xiaolian.amigo.ui.base.WebActivity;
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

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Long id;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(RechargeDetailActivity.this);

        adapter = new WithdrawRechargeDetailAdapter(this, R.layout.item_withdral_recharge_detail, items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(this, RecycleViewDivider.VERTICAL_LIST));
        recyclerView.setAdapter(adapter);

        if (id == -1) {
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
        return R.string.recharge_detail;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_recharge_detail;
    }

    @Override
    public void render(FundsDTO data) {
        tv_amount.setText("¥" + data.getAmount());
        tv_status.setText(RechargeStatus.getRechargeStatus(data.getStatus()).getDesc());
        tv_status.setTextColor(
                ContextCompat.getColor(this,RechargeStatus.getRechargeStatus(data.getStatus()).getColorRes()));
        if (CommonUtil.equals(data.getStatus(), RechargeStatus.AUDIT_FAIL)
                && !TextUtils.isEmpty(data.getReason())) {
            ll_reason.setVisibility(View.VISIBLE);
            tv_reason_content.setText(data.getReason());
            tv_reason.setText("未通过原因");
        }

        items.add(new WithdrawRechargeDetailAdapter.Item("提现方式：",
                WithdrawOperationType.getOperationType(data.getOperationType()).getDesc()));
        items.add(new WithdrawRechargeDetailAdapter.Item("充值账号：",
                data.getThirdAccountName()));
        items.add(new WithdrawRechargeDetailAdapter.Item("充值时间：",
                TimeUtils.millis2String(data.getCreateTime())));
        items.add(new WithdrawRechargeDetailAdapter.Item("流水号：",
                data.getOrderNo()));
        items.add(new WithdrawRechargeDetailAdapter.Item(null, null));
    }

    @OnClick(R.id.left_oper)
    public void onLeftOper() {
        startActivity(new Intent(this, WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_HELP));
    }

    @OnClick(R.id.right_oper)
    public void onRightOper() {
        startActivity(new Intent(this, WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_COMPLAINT));
    }
}
