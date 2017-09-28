package com.xiaolian.amigo.ui.wallet;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.ScreenUtils;
import com.xiaolian.amigo.ui.wallet.adaptor.BonusItemDelegate;
import com.xiaolian.amigo.ui.wallet.adaptor.DiscountItemDelegate;
import com.xiaolian.amigo.ui.wallet.adaptor.NormalItemDelegate;
import com.xiaolian.amigo.ui.wallet.adaptor.RechargeAdaptor;
import com.xiaolian.amigo.ui.wallet.intf.IRechargePresenter;
import com.xiaolian.amigo.ui.wallet.intf.IRechargeView;
import com.xiaolian.amigo.ui.widget.GridSpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    // 充值金额列表
    List<RechargeAdaptor.RechargeWapper> recharges = new ArrayList<>();

    RechargeAdaptor adaptor;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        adaptor = new RechargeAdaptor(this, recharges);
        adaptor.addItemViewDelegate(new NormalItemDelegate());
        adaptor.addItemViewDelegate(new BonusItemDelegate());
        adaptor.addItemViewDelegate(new DiscountItemDelegate());

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new GridSpacesItemDecoration(3, ScreenUtils.dpToPxInt(this, 10), false));
        recyclerView.setAdapter(adaptor);

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
    public void addMore(List<RechargeAdaptor.RechargeWapper> rechargeWappers) {
        recharges.addAll(rechargeWappers);
        adaptor.notifyDataSetChanged();
    }
}
