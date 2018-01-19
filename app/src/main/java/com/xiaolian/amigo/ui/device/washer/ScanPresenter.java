package com.xiaolian.amigo.ui.device.washer;

import com.xiaolian.amigo.data.manager.intf.IWasherDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.trade.QrCodeScanReqDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeScanRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;
import com.xiaolian.amigo.ui.device.washer.intf.IScanPresenter;
import com.xiaolian.amigo.ui.device.washer.intf.IScanView;

import javax.inject.Inject;

/**
 * <p>
 * Created by zcd on 18/1/17.
 */

public class ScanPresenter<V extends IScanView> extends BasePresenter<V>
    implements IScanPresenter<V> {
    private IWasherDataManager washerDataManager;

    @Inject
    public ScanPresenter(IWasherDataManager washerDataManager) {
        this.washerDataManager = washerDataManager;
    }

    @Override
    public void scanCheckout(String content) {
        QrCodeScanReqDTO reqDTO = new QrCodeScanReqDTO();
//        reqDTO.setQrCodeData(content);
        // mock data
        reqDTO.setQrCodeData("200000AABBCCDD00000000000000000000000039E40ED8CFFAEFD27CFC1C92A602AE78CA");
        addObserver(washerDataManager.scanCheckout(reqDTO),
                new NetworkObserver<ApiResult<QrCodeScanRespDTO>>() {

                    @Override
                    public void onReady(ApiResult<QrCodeScanRespDTO> result) {
                        if (null == result.getError()) {
                            washerDataManager.setDeviceToken(result.getData().getMacAddress(), result.getData().getDeviceToken());
                            getMvpView().gotoChooseModeView();
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }
                });
    }
}
