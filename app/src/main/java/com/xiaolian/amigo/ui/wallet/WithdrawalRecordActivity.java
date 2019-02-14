package com.xiaolian.amigo.ui.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alipay.mobile.framework.service.annotation.OperationType;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.WithdrawOperationType;
import com.xiaolian.amigo.ui.wallet.adaptor.BillListAdaptor;
import com.xiaolian.amigo.ui.wallet.adaptor.WithdrawalAdaptor;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawRecordPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalRecordView;
import com.xiaolian.amigo.util.Constant;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 充值提现记录
 *
 * @author zcd
 * @date 17/10/17
 */

public class WithdrawalRecordActivity extends WalletBaseListActivity implements IWithdrawalRecordView {
    private static final int REQUEST_CODE_DETAIL = 0x1201;
    static final int INVALID_INT = -1;
    @Inject
    IWithdrawRecordPresenter<IWithdrawalRecordView> presenter;

    private List<BillListAdaptor.BillListAdaptorWrapper> items = new ArrayList<>();
    BillListAdaptor adaptor;

    private boolean refreshFlag = false;

    private Integer year;
    private Integer month;
    private Integer fundsType;

    @Override
    protected void onRefresh() {
        refreshFlag = true;
        page = Constant.PAGE_START_NUM;
        presenter.requestWithdrawalRecord(page, fundsType,
                year, month);
//        items.clear();
    }

    @Override
    protected void onLoadMore() {
        presenter.requestWithdrawalRecord(page, fundsType,
                year, month);
    }

    @Override
    protected void setRecyclerView(RecyclerView recyclerView) {
        adaptor = new BillListAdaptor(this, R.layout.item_balance_detail_record, items);
        adaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivityForResult(new Intent(WithdrawalRecordActivity.this,
                                WithdrawOperationType.getOperationType(items.get(position).getType())
                                        .getClz())
                                .putExtra(Constant.EXTRA_KEY, items.get(position).getId()),
                        REQUEST_CODE_DETAIL);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);
//        recyclerView.addItemDecoration(new SpaceItemDecoration(
//                ScreenUtils.dpToPxInt(this, 14)));
    }

    @Override
    protected int setTitle() {
        return R.string.withdrawal_record;
    }

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(WithdrawalRecordActivity.this);

        String title = "";
        if (year != null
                && year != INVALID_INT
                && month != null
                && month != INVALID_INT) {
            title = ""
                    .concat(String.valueOf(year))
                    .concat("年")
                    .concat(String.valueOf(month))
                    .concat("月");
        }

//        if (fundsType != null
//                && fundsType != INVALID_INT) {
//            title = title.concat(WithdrawOperationType.getOperationType(fundsType).getDesc());
//        } else {
//            title = title.concat("充值提现");
//        }
//        setToolBarTitle(title.concat("记录"));

        if (fundsType == 1) {
            setToolBarTitle(title + "余额充值");
        } else if (fundsType == 2) {
            setToolBarTitle(title + "余额退款");
        }
    }

    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra(Constant.DATA_BUNDLE);
            if (bundle == null) {
                return;
            }
            fundsType = bundle.getInt(WalletConstant.KEY_FUNDS_TYPE, INVALID_INT);
            year = bundle.getInt(WalletConstant.KEY_YEAR, INVALID_INT);
            month = bundle.getInt(WalletConstant.KEY_MONTH, INVALID_INT);
        }
    }

    @Override
    public void addMore(List<BillListAdaptor.BillListAdaptorWrapper> wrappers) {
        if (refreshFlag) {
            refreshFlag = false;
            items.clear();
        }
        items.addAll(wrappers);
        adaptor.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_DETAIL) {
            onRefresh();
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
