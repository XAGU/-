package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.trade.PayReqDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeGenerateRespDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeScanReqDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeScanRespDTO;
import com.xiaolian.amigo.data.network.model.trade.WashingModeRespDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 洗衣机模块
 * <p>
 * Created by zcd on 18/1/12.
 */

public interface IWasherDataManager {
    // 扫描二维码结账
    Observable<ApiResult<QrCodeScanRespDTO>> scanCheckout(@Body QrCodeScanReqDTO reqDTO);

    // 生成支付二维码
    Observable<ApiResult<QrCodeGenerateRespDTO>> generateQRCode(@Body PayReqDTO reqDTO);

    // 请求洗衣机模式
    Observable<ApiResult<WashingModeRespDTO>> getWasherMode();

    // 存储deviceToken
    void setDeviceToken(String deviceNo, String deviceToken);
}
