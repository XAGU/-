package com.xiaolian.amigo.ui.device;

import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
import com.xiaolian.amigo.data.manager.intf.IDeviceDataManager;
import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.data.manager.intf.ITradeDataManager;
import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.QueryPrepayOptionReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.OrderPreInfoDTO;
import com.xiaolian.amigo.data.network.model.dto.response.PersonalWalletDTO;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.ui.device.intf.IWaterDeviceBasePresenter;
import com.xiaolian.amigo.ui.device.intf.IWaterDeviceBaseView;

/**
 * <p>
 * Created by zcd on 10/13/17.
 */

public abstract class WaterDeviceBasePresenter<V extends IWaterDeviceBaseView> extends DeviceBasePresenter<V>
        implements IWaterDeviceBasePresenter<V> {
    private IWalletDataManager walletDataManager;
    private IOrderDataManager orderDataManager;
    public WaterDeviceBasePresenter(IBleDataManager bleDataManager,
                                    ITradeDataManager tradeDataManager,
                                    IOrderDataManager orderDataManager,
                                    IWalletDataManager walletDataManager,
                                    ISharedPreferencesHelp sharedPreferencesHelp) {
        super(bleDataManager, tradeDataManager, orderDataManager, sharedPreferencesHelp);
        this.orderDataManager = orderDataManager;
        this.walletDataManager = walletDataManager;
    }


    @Override
    public void setBonusAmount(int amount) {
    }

    @Override
    public int getBonusAmount() {
        return 0;
    }

    @Override
    public void queryWallet(double amount) {
        addObserver(walletDataManager.queryWallet(), new NetworkObserver<ApiResult<PersonalWalletDTO>>() {

            @Override
            public void onReady(ApiResult<PersonalWalletDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getBalance() >= amount) {
                        getMvpView().startUse();
                    } else {
                        getMvpView().showRechargeDialog(amount);
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void queryPrepayOption(int deviceType) {
        QueryPrepayOptionReqDTO reqDTO = new QueryPrepayOptionReqDTO();
        reqDTO.setDeviceType(deviceType);
        addObserver(orderDataManager.queryPrepayOption(reqDTO), new NetworkObserver<ApiResult<OrderPreInfoDTO>>() {

            @Override
            public void onReady(ApiResult<OrderPreInfoDTO> result) {
                if (null == result.getError()) {
                    getMvpView().setPrepayOption(result.getData());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
