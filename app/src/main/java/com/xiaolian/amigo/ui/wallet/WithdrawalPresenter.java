package com.xiaolian.amigo.ui.wallet;

import android.text.TextUtils;

import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.funds.QueryRechargeTypeListRespDTO;
import com.xiaolian.amigo.data.network.model.funds.SchoolWechatAccountRespDTO;
import com.xiaolian.amigo.data.network.model.funds.WechatWithdrawReqDTO;
import com.xiaolian.amigo.data.network.model.userthirdaccount.QueryUserThirdAccountReqDTO;
import com.xiaolian.amigo.data.network.model.funds.WithdrawReqDTO;
import com.xiaolian.amigo.data.network.model.userthirdaccount.QueryUserThirdAccountRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalView;

import javax.inject.Inject;

/**
 * 提现
 *
 * @author zcd
 * @date 17/10/14
 */

public class WithdrawalPresenter<V extends IWithdrawalView> extends BasePresenter<V>
        implements IWithdrawalPresenter<V> {
    private IWalletDataManager walletDataManager;

    @Inject
    WithdrawalPresenter(IWalletDataManager manager) {
        this.walletDataManager = manager;
    }

    @Override
    public void withdraw(String amount, String withdrawName, Long withdrawId) {
        WithdrawReqDTO reqDTO = new WithdrawReqDTO();
        reqDTO.setAmount(amount);
        reqDTO.setUserThirdAccountId(withdrawId);
        addObserver(walletDataManager.withdraw(reqDTO), new NetworkObserver<ApiResult<SimpleRespDTO>>() {

            @Override
            public void onReady(ApiResult<SimpleRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().gotoWithdrawDetail(result.getData().getId());
                    walletDataManager.setLastWithdrawId(withdrawId);
                    walletDataManager.setLastWithdrawName(withdrawName);
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void requestAccounts(int type) {
        String withdrawName = walletDataManager.getLastWithdrawName();
        Long withdrawId = walletDataManager.getLastWithdrawId();
        if (withdrawId != -1 && !TextUtils.isEmpty(withdrawName)) {
            getMvpView().showWithdrawAccount(withdrawName, withdrawId);
            return;
        }
        QueryUserThirdAccountReqDTO reqDTO = new QueryUserThirdAccountReqDTO();
        reqDTO.setType(type);
        addObserver(walletDataManager.requestThirdAccounts(reqDTO), new NetworkObserver<ApiResult<QueryUserThirdAccountRespDTO>>() {

            @Override
            public void onReady(ApiResult<QueryUserThirdAccountRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getThirdAccounts() != null && result.getData().getThirdAccounts().size() > 0) {
                        String withdrawName = result.getData().getThirdAccounts().get(0).getAccountName();
                        Long withdrawId = result.getData().getThirdAccounts().get(0).getId();
                        getMvpView().showWithdrawAccount(withdrawName, withdrawId);
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }

            }
        });
    }

    @Override
    public void clearAccount() {
        walletDataManager.setLastWithdrawName("");
        walletDataManager.setLastWithdrawId(null);
    }

    @Override
    public void getWechatAppid() {
        addObserver(walletDataManager.wechatAccountInfoAppid() ,new NetworkObserver<ApiResult<SchoolWechatAccountRespDTO>>(){

            @Override
            public void onReady(ApiResult<SchoolWechatAccountRespDTO> result) {
                if (result.getError() == null) {
                    getMvpView().setAppid(result.getData().getAppId());
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });

    }

    @Override
    public void wechatWithdraw(float amount, String code, String userRealName) {
        WechatWithdrawReqDTO reqDTO = new WechatWithdrawReqDTO();
        reqDTO.setAmount(amount);
        reqDTO.setCode(code);
        reqDTO.setUserRealName(userRealName);
        // 应用类型 1 - 原生 ； 2 -小程序
        reqDTO.setAppSource(1);
        addObserver(walletDataManager.wechatWithdraw(reqDTO) , new NetworkObserver<ApiResult<SimpleRespDTO>>(){

            @Override
            public void onReady(ApiResult<SimpleRespDTO> result) {
                if (result.getError() == null){  // 退款成功
//                    getMvpView().
                }else{
                    getMvpView().onError("退款失败");
                }
            }
        });
    }

    @Override
    public void withdrawType() {
        addObserver(walletDataManager.typeList(), new NetworkObserver<ApiResult<QueryRechargeTypeListRespDTO>>(){

            @Override
            public void onReady(ApiResult<QueryRechargeTypeListRespDTO> result) {
                if (result.getError() == null){
                    getMvpView().showTypeList(result.getData());
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

}
