package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.data.network.model.userbill.UserMonthlyBillRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.wallet.adaptor.BillListAdaptor;
import com.xiaolian.amigo.ui.wallet.adaptor.WithdrawalAdaptor;

import java.util.List;

public interface IBalanceDetailListView extends IBaseView {
    void render(UserMonthlyBillRespDTO data);

    void setLoadMoreComplete();

    void setRefreshComplete();

    void showEmptyView();

    void showEmptyView(String tip, int colorRes);

    void showEmptyView(int tipRes, int colorRes);

    void showEmptyView(int tipRes);

    void hideEmptyView();

    void showErrorView();

    void showErrorView(int colorRes);

    void hideErrorView();

    /**
     * 加载提现记录列表
     *
     * @param wrappers 提现记录
     */
    void addMore(List<BillListAdaptor.BillListAdaptorWrapper> wrappers);

    /**
     * 刷新账单列表
     * @param wrappers
     */
    void onRefresh(List<BillListAdaptor.BillListAdaptorWrapper> wrappers);
}
