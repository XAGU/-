package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.complaint.CheckComplaintReqDTO;
import com.xiaolian.amigo.data.network.model.cs.RemindReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.funds.FundsDTO;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalDetailPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalDetailView;

import javax.inject.Inject;

/**
 * 提现详情
 * <p>
 * Created by zcd on 10/23/17.
 */

public class WithdrawalDetailPresenter<V extends IWithdrawalDetailView> extends BasePresenter<V>
        implements IWithdrawalDetailPresenter<V> {
    private static final String TAG = WithdrawalDetailPresenter.class.getSimpleName();
    private IWalletDataManager walletDataManager;
    private ISharedPreferencesHelp sharedPreferencesHelp;

    @Inject
    public WithdrawalDetailPresenter(IWalletDataManager manager,
                                     ISharedPreferencesHelp sharedPreferencesHelp) {
        this.walletDataManager = manager;
        this.sharedPreferencesHelp = sharedPreferencesHelp;
    }

    @Override
    public void requestData(Long id) {
        SimpleReqDTO reqDTO = new SimpleReqDTO();
        reqDTO.setId(id);
        addObserver(walletDataManager.queryWithdrawRechargeDetail(reqDTO), new NetworkObserver<ApiResult<FundsDTO>>() {

            @Override
            public void onReady(ApiResult<FundsDTO> result) {
                if (null == result.getError()) {
                    getMvpView().render(result.getData());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public String getToken() {
        return sharedPreferencesHelp.getToken();
    }

    @Override
    public void remind(Long sourceId) {
        RemindReqDTO reqDTO = new RemindReqDTO();
        reqDTO.setSourceId(sourceId);
        // 1 表示提现 2 表示维修
        reqDTO.setType(1);
        addObserver(walletDataManager.remind(reqDTO), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isResult()) {
                        getMvpView().onSuccess("提醒客服成功");
                    } else {
                        getMvpView().onError("提醒客服失败");
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void cancelWithdraw(Long id) {
        SimpleReqDTO reqDTO = new SimpleReqDTO();
        reqDTO.setId(id);
        addObserver(walletDataManager.cancelWithdraw(reqDTO), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().onSuccess(R.string.cancel_withdraw_success);
                    getMvpView().gotoBack();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void complaint(Long orderId, int orderType) {
        CheckComplaintReqDTO reqDTO = new CheckComplaintReqDTO();
        reqDTO.setOrderId(orderId);
        reqDTO.setOrderType(orderType);
        addObserver(walletDataManager.checkComplaint(reqDTO), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isResult()) {
                        getMvpView().onError(R.string.complaint_error);
                    } else {
                        getMvpView().toComplaint();
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
