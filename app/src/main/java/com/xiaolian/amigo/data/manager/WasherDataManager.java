package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IWasherDataManager;
import com.xiaolian.amigo.data.network.ITradeApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.trade.PayReqDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeScanReqDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeScanRespDTO;
import com.xiaolian.amigo.data.network.model.trade.WashingModeRespDTO;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * 洗衣机模块
 * <p>
 * Created by zcd on 18/1/12.
 */

public class WasherDataManager implements IWasherDataManager {
    private ITradeApi tradeApi;
    @Inject
    public WasherDataManager(Retrofit retrofit) {
        tradeApi = retrofit.create(ITradeApi.class);
    }

    @Override
    public Observable<ApiResult<QrCodeScanRespDTO>> scanCheckout(QrCodeScanReqDTO reqDTO) {
        return tradeApi.scanCheckout(reqDTO);
    }

    @Override
    public Observable<ApiResult<String>> generateQRCode(PayReqDTO reqDTO) {
        return tradeApi.generateQRCode(reqDTO);
    }

    @Override
    public Observable<ApiResult<WashingModeRespDTO>> getWasherMode() {
        return tradeApi.getWasherMode();
    }
}
