package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.IMainDataManager;
import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.user.BriefSchoolBusiness;
import com.xiaolian.amigo.data.network.model.userbill.QueryBillListReqDTO;
import com.xiaolian.amigo.data.network.model.userbill.QueryBillListRespDTO;
import com.xiaolian.amigo.data.network.model.userbill.QueryMonthlyBillReqDTO;
import com.xiaolian.amigo.data.network.model.userbill.UserMonthlyBillRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.wallet.adaptor.BillListAdaptor;
import com.xiaolian.amigo.ui.wallet.intf.IBalanceDetailListPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IBalanceDetailListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class BalanceDetailListPresenter<V extends IBalanceDetailListView> extends BasePresenter<V> implements IBalanceDetailListPresenter<V> {
    private IWalletDataManager walletDataManager;
    private IMainDataManager mainDataManager;

    private boolean isRefreshPage = true  ;

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
                                if (result.getData() != null) {
                                    List<BillListAdaptor.BillListAdaptorWrapper> wrappers = new ArrayList<>();
                                    for (HashMap<String, Object> billDic : result.getData().getDetailList()) {
                                        wrappers.add(new BillListAdaptor.BillListAdaptorWrapper(billDic));
                                    }
                                    if (isRefreshPage){
                                        getMvpView().onRefresh(wrappers);
                                    }else {
                                        getMvpView().addMore(wrappers);
                                    }
                                } else {
                                    if (isRefreshPage) {
                                        getMvpView().showEmptyView(R.string.empty_tip_1);
                                    }
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
    public void resetPage(boolean isRefreshPage) {
        this.isRefreshPage = isRefreshPage ;
    }

    @Override
    public Long getAccountCreateTime() {
        return walletDataManager.getUser().getCreateTime();
    }

    @Override
    public List<BriefSchoolBusiness> getSchoolBizList() {
        List<BriefSchoolBusiness> list = mainDataManager.getSchoolBiz();
        return mainDataManager.getSchoolBiz();
    }
}
