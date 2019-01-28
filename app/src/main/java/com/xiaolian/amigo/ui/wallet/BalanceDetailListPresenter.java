package com.xiaolian.amigo.ui.wallet;

import android.util.Log;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.IMainDataManager;
import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.funds.FundsInListDTO;
import com.xiaolian.amigo.data.network.model.user.BriefSchoolBusiness;
import com.xiaolian.amigo.data.network.model.userbill.QueryBillListReqDTO;
import com.xiaolian.amigo.data.network.model.userbill.QueryBillListRespDTO;
import com.xiaolian.amigo.data.network.model.userbill.QueryMonthlyBillReqDTO;
import com.xiaolian.amigo.data.network.model.userbill.UserMonthlyBillRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.wallet.adaptor.BillListAdaptor;
import com.xiaolian.amigo.ui.wallet.adaptor.WithdrawalAdaptor;
import com.xiaolian.amigo.ui.wallet.intf.IBalanceDetailListView;
import com.xiaolian.amigo.ui.wallet.intf.IBalanceDetailListPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.OnClick;

public class BalanceDetailListPresenter<V extends IBalanceDetailListView> extends BasePresenter<V> implements IBalanceDetailListPresenter<V> {
    private IWalletDataManager walletDataManager;
    private IMainDataManager mainDataManager;

    @Inject
    BalanceDetailListPresenter(IWalletDataManager walletDataManager, IMainDataManager mainDataManager) {
        this.walletDataManager = walletDataManager;
        this.mainDataManager = mainDataManager;
    }



    @Override
    public void getMonthlyBill(int year, int month) {
        QueryMonthlyBillReqDTO reqDTO = new QueryMonthlyBillReqDTO();
        reqDTO.setYear(year);
        reqDTO.setMonth(month);
        addObserver(walletDataManager.getMonthlyBill(reqDTO),
                new NetworkObserver<ApiResult<UserMonthlyBillRespDTO>>() {

                    @Override
                    public void onReady(ApiResult<UserMonthlyBillRespDTO> result) {
                        if (null == result.getError()) {
                            if (result.getData() != null) {
                                getMvpView().render(result.getData());
                            }
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }
                });
    }

    @Override
    public void getUserBillList(String timeStr, Integer billType, Integer billStatus, Long lastId, boolean pagedown, int pageSize) {
        QueryBillListReqDTO queryBillListReqDTO = new QueryBillListReqDTO();
        queryBillListReqDTO.setDate(timeStr);
        queryBillListReqDTO.setLastId(lastId);
        queryBillListReqDTO.setPageDown(pagedown);
        queryBillListReqDTO.setStatus(billStatus);
        queryBillListReqDTO.setType(billType);
        queryBillListReqDTO.setSize(pageSize);
        addObserver(walletDataManager.getUserBillList(queryBillListReqDTO),
                new NetworkObserver<ApiResult<QueryBillListRespDTO>>() {

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onReady(ApiResult<QueryBillListRespDTO> result) {
                        if (null == result.getError()) {
                            getMvpView().setRefreshComplete();
                            getMvpView().setLoadMoreComplete();
                            getMvpView().hideEmptyView();
                            getMvpView().hideErrorView();
                            if (null == result.getError()) {
                                if (result.getData() != null && result.getData().getDetailList().size() > 0) {
                                    List<BillListAdaptor.BillListAdaptorWrapper> wrappers = new ArrayList<>();
                                    for (HashMap<String, Object> billDic : result.getData().getDetailList()) {
                                        wrappers.add(new BillListAdaptor.BillListAdaptorWrapper(billDic));
                                    }
                                    getMvpView().addMore(wrappers);
                                } else {
                                    getMvpView().showEmptyView(R.string.empty_tip_1);
                                }
                            } else {
                                getMvpView().showErrorView();
                                getMvpView().onError(result.getError().getDisplayMessage());
                            }
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getMvpView().setRefreshComplete();
                        getMvpView().setLoadMoreComplete();
                        getMvpView().showErrorView();
                    }
                });

    }

    @Override
    public Long getAccountCreateTime() {
        return walletDataManager.getUser().getCreateTime();
    }

    @Override
    public List<BriefSchoolBusiness>getSchoolBizList() {
        List<BriefSchoolBusiness> list = mainDataManager.getSchoolBiz();
        return mainDataManager.getSchoolBiz();
    }
}
