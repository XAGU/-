package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.userthirdaccount.AddThirdAccountReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.wallet.intf.IAddAccountPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IAddAccountView;

import javax.inject.Inject;

/**
 * 添加账户
 *
 * @author zcd
 * @date 17/10/27
 */

public class AddAccountPresenter<V extends IAddAccountView> extends BasePresenter<V>
        implements IAddAccountPresenter<V> {
    @SuppressWarnings("unused")
    private static final String TAG = AddAccountPresenter.class.getSimpleName();
    private IWalletDataManager walletDataManager;

    @Inject
    AddAccountPresenter(IWalletDataManager walletDataManager) {
        this.walletDataManager = walletDataManager;
    }

    @Override
    public void addAccount(String accountName, String userRealName, Integer accountType) {
        AddThirdAccountReqDTO reqDTO = new AddThirdAccountReqDTO();
        reqDTO.setAccountName(accountName);
        reqDTO.setUserRealName(userRealName);
        reqDTO.setAccountType(accountType);
        addObserver(walletDataManager.addAccount(reqDTO), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().onSuccess("添加成功");
                    getMvpView().back();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
