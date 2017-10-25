package com.xiaolian.amigo.ui.wallet;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.PayWay;
import com.xiaolian.amigo.ui.wallet.adaptor.RechargeTypeAdaptor;
import com.xiaolian.amigo.util.PayUtil;
import com.xiaolian.amigo.util.ScreenUtils;
import com.xiaolian.amigo.ui.wallet.adaptor.BonusItemDelegate;
import com.xiaolian.amigo.ui.wallet.adaptor.DiscountItemDelegate;
import com.xiaolian.amigo.ui.wallet.adaptor.NormalItemDelegate;
import com.xiaolian.amigo.ui.wallet.adaptor.RechargeAdaptor;
import com.xiaolian.amigo.ui.wallet.intf.IRechargePresenter;
import com.xiaolian.amigo.ui.wallet.intf.IRechargeView;
import com.xiaolian.amigo.ui.widget.GridSpacesItemDecoration;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.Data;

/**
 * 我的钱包
 * <p>
 * Created by caidong on 2017/9/7.
 */
public class RechargeActivity extends WalletBaseActivity implements IRechargeView {

    @Inject
    IRechargePresenter<IRechargeView> presenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.recyclerView1)
    RecyclerView recyclerView1;

    @BindView(R.id.bt_submit)
    Button bt_submit;

    // 充值金额列表
    List<RechargeAdaptor.RechargeWrapper> recharges = new ArrayList<>();

    // 充值方式列表
    List<RechargeTypeAdaptor.RechargeWrapper> rechargeTypes = new ArrayList<RechargeTypeAdaptor.RechargeWrapper>() {
        {
            add(new RechargeTypeAdaptor.RechargeWrapper(R.drawable.ic_alipay, "支付宝", true));
        }
    };

    RechargeAdaptor adaptor;
    RechargeTypeAdaptor typeAdaptor;
    private int rechargeSelectedPosition = -1;
    private int rechargeTypeSelectedPosition = 0;


    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        adaptor = new RechargeAdaptor(this, recharges);
        adaptor.addItemViewDelegate(new NormalItemDelegate());
        adaptor.addItemViewDelegate(new BonusItemDelegate());
        adaptor.addItemViewDelegate(new DiscountItemDelegate());
        adaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (rechargeSelectedPosition != -1) {
                    recharges.get(rechargeSelectedPosition).setSelected(false);
                    recharges.get(position).setSelected(true);
                } else {
                    recharges.get(position).setSelected(true);
                }
                rechargeSelectedPosition = position;
                adaptor.notifyDataSetChanged();
                toggleSubmitButton();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new GridSpacesItemDecoration(3, ScreenUtils.dpToPxInt(this, 10), false));
        recyclerView.setAdapter(adaptor);

        typeAdaptor = new RechargeTypeAdaptor(this, R.layout.item_recharge_type, rechargeTypes);
        recyclerView1.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));
        recyclerView1.addItemDecoration(new GridSpacesItemDecoration(2, ScreenUtils.dpToPxInt(this, 10), false));
        recyclerView1.setAdapter(typeAdaptor);

        presenter.onAttach(RechargeActivity.this);
        presenter.getRechargeList();

    }

    @Override
    protected int setTitle() {
        return R.string.recharge;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_wallet_recharge2;
    }

    @Override
    public void addMore(List<RechargeAdaptor.RechargeWrapper> rechargeWrappers) {
        recharges.addAll(rechargeWrappers);
        adaptor.notifyDataSetChanged();
    }

    @OnClick(R.id.bt_submit)
    public void recharge() {
        presenter.recharge(recharges.get(rechargeSelectedPosition).getId());
    }

    private void toggleSubmitButton() {
        if (rechargeSelectedPosition != -1 && rechargeTypeSelectedPosition != -1) {
            bt_submit.setEnabled(true);
        }
    }

    @Override
    public void back() {
        onBackPressed();
    }

    @Override
    public void alipay(String reqArgs) {
        PayUtil.alpay(this, reqArgs);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvnet(PayEvent event) {
        switch (event.getType()) {
            case ALIAPY:
                presenter.parseAlipayResult(event.getAlipayResult().get("resultStatus"),
                        event.getAlipayResult().get("result"),
                        event.getAlipayResult().get("memo"));
                break;
            case WECHAT:
                break;
        }
    }

    @Data
    public static class PayEvent {
        private PayWay type;
        private Map<String, String> alipayResult;

        public PayEvent(PayWay type, Map<String, String> alipayResult) {
            this.type = type;
            this.alipayResult = alipayResult;
        }
    }
}
