package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.userthirdaccount.QueryUserThirdAccountReqDTO;
import com.xiaolian.amigo.data.network.model.userthirdaccount.QueryUserThirdAccountRespDTO;
import com.xiaolian.amigo.data.network.model.userthirdaccount.UserThirdAccountDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.dispenser.ChooseDispenserPresenter;
import com.xiaolian.amigo.ui.wallet.adaptor.ChooseWithdrawAdapter;
import com.xiaolian.amigo.ui.wallet.intf.IChooseWithdrawPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IChooseWithdrawView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 选择提现
 *
 * @author zcd
 * @date 17/10/27
 */

public class ChooseWithdrawPresenter<V extends IChooseWithdrawView> extends BasePresenter<V>
        implements IChooseWithdrawPresenter<V> {
    private static final String TAG = ChooseDispenserPresenter.class.getSimpleName();
    private IWalletDataManager walletDataManager;

    @Inject
    ChooseWithdrawPresenter(IWalletDataManager walletDataManager) {
        this.walletDataManager = walletDataManager;
    }

    @Override
    public void requestAccounts(int type) {
        QueryUserThirdAccountReqDTO reqDTO = new QueryUserThirdAccountReqDTO();
        reqDTO.setType(type);
        addObserver(walletDataManager.requestThirdAccounts(reqDTO), new NetworkObserver<ApiResult<QueryUserThirdAccountRespDTO>>() {

            @Override
            public void onReady(ApiResult<QueryUserThirdAccountRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getThirdAccounts() != null && result.getData().getThirdAccounts().size() > 0) {
                        List<ChooseWithdrawAdapter.Item> items = new ArrayList<>();
                        for (UserThirdAccountDTO dto : result.getData().getThirdAccounts()) {
                            items.add(new ChooseWithdrawAdapter.Item(dto));
                        }
                        getMvpView().addMore(items);
                    } else {
                        getMvpView().addMore(new ArrayList<>());
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                    getMvpView().addMore(new ArrayList<>());
                }

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().addMore(new ArrayList<>());
            }
        });
    }

    @Override
    public void deleteAccount(Long id) {
        SimpleReqDTO reqDTO = new SimpleReqDTO();
        reqDTO.setId(id);
        addObserver(walletDataManager.deleteAccount(reqDTO), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().onSuccess("删除成功");
                    EventBus.getDefault().post(new WalletEvent(WalletEvent.EventType.DELETE_ACCOUNT,
                            id));
                    getMvpView().refreshList();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
