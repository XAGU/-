package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.QueryPersonalFundsListReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.FundsInListDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryFundsListRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.wallet.adaptor.WithdrawalAdaptor;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawRecordPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalRecordView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 充值提现记录
 * <p>
 * Created by zcd on 10/17/17.
 */

public class WithdrawalRecordPresenter<V extends IWithdrawalRecordView> extends BasePresenter<V>
        implements IWithdrawRecordPresenter<V> {
    private static final String TAG = WithdrawalRecordPresenter.class.getSimpleName();
    private IWalletDataManager manager;

    @Inject
    public WithdrawalRecordPresenter(IWalletDataManager manager) {
        super();
        this.manager = manager;
    }
    @Override
    public void requestWithdrawalRecord(int page) {
        QueryPersonalFundsListReqDTO reqDTO = new QueryPersonalFundsListReqDTO();
        reqDTO.setPage(page);
        reqDTO.setSize(Constant.PAGE_SIZE);
        addObserver(manager.queryWithdrawList(reqDTO), new NetworkObserver<ApiResult<QueryFundsListRespDTO>>(false, true) {

            @Override
            public void onReady(ApiResult<QueryFundsListRespDTO> result) {
                getMvpView().setRefreshComplete();
                getMvpView().setLoadMoreComplete();
                getMvpView().hideEmptyView();
                getMvpView().hideErrorView();
                if (null == result.getError()) {
                    if (result.getData() != null && result.getData().getFunds() != null
                            && result.getData().getFunds().size() > 0) {
                        List<WithdrawalAdaptor.WithdrawalWrapper> wrappers = new ArrayList<WithdrawalAdaptor.WithdrawalWrapper>();
                        for (FundsInListDTO dto : result.getData().getFunds()) {
                            wrappers.add(new WithdrawalAdaptor.WithdrawalWrapper(dto));
                        }
                        getMvpView().addMore(wrappers);
                        getMvpView().addPage();
                    } else {
                        getMvpView().showEmptyView(R.string.empty_tip_1);
                    }
                } else {
                    getMvpView().showErrorView();
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
}
