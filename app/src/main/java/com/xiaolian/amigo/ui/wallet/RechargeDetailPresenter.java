package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.complaint.CheckComplaintReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.RemindReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.FundsDTO;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.wallet.intf.IRechargeDetailPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IRechargeDetailView;

import javax.inject.Inject;

/**
 * 充值详情
 * <p>
 * Created by zcd on 10/23/17.
 */

public class RechargeDetailPresenter<V extends IRechargeDetailView> extends BasePresenter<V>
    implements IRechargeDetailPresenter<V> {
    private static final String TAG = RechargeDetailPresenter.class.getSimpleName();
    private IWalletDataManager walletDataManager;
    private ISharedPreferencesHelp sharedPreferencesHelp;

    @Inject
    public RechargeDetailPresenter(IWalletDataManager walletDataManager, ISharedPreferencesHelp sharedPreferencesHelp) {
        this.walletDataManager = walletDataManager;
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
