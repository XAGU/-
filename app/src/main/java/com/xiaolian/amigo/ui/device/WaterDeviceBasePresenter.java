package com.xiaolian.amigo.ui.device;

import com.xiaolian.amigo.data.enumeration.AgreementVersion;
import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
import com.xiaolian.amigo.data.manager.intf.IDeviceDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.order.QueryPrepayOptionReqDTO;
import com.xiaolian.amigo.data.network.model.cs.CsMobileRespDTO;
import com.xiaolian.amigo.data.network.model.order.OrderPreInfoDTO;
import com.xiaolian.amigo.data.network.model.funds.PersonalWalletDTO;
import com.xiaolian.amigo.ui.device.intf.IWaterDeviceBasePresenter;
import com.xiaolian.amigo.ui.device.intf.IWaterDeviceBaseView;

/**
 * <p>
 * Created by zcd on 10/13/17.
 */

public abstract class WaterDeviceBasePresenter<V extends IWaterDeviceBaseView> extends DeviceBasePresenter<V>
        implements IWaterDeviceBasePresenter<V> {
    private IDeviceDataManager deviceDataManager;

    public WaterDeviceBasePresenter(IBleDataManager bleDataManager,
                                    IDeviceDataManager deviceDataManager) {
        super(bleDataManager, deviceDataManager);
        this.deviceDataManager = deviceDataManager;
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
        addObserver(deviceDataManager.queryWallet(), new NetworkObserver<ApiResult<PersonalWalletDTO>>() {

            @Override
            public void onReady(ApiResult<PersonalWalletDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getAllBalance() >= amount) {
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
        addObserver(deviceDataManager.queryPrepayOption(reqDTO), new NetworkObserver<ApiResult<OrderPreInfoDTO>>(false) {

            @Override
            public void onReady(ApiResult<OrderPreInfoDTO> result) {
                if (null == result.getError()) {
                    getMvpView().setPrepayOption(result.getData());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    /**
     * 是否需要显示温馨提示
     */
    protected boolean needShowGuide() {
        if (getSupplier() == null || getSupplier().getAgreement() == null) {
            return false;
        }
        switch (AgreementVersion.getAgreement(getSupplier().getAgreement())) {
            case HAONIANHUA:
                return true;
            // 辛纳设备不显示温馨提示
            case XINNA:
                return false;
            default:
                return false;
        }
    }

    @Override
    public void queryCsInfo() {
        resetSubscriptions();
        addObserver(deviceDataManager.queryCsInfo(), new NetworkObserver<ApiResult<CsMobileRespDTO>>(false) {

            @Override
            public void onReady(ApiResult<CsMobileRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().showCsCallDialog(result.getData().getMobile().toString());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }
}
