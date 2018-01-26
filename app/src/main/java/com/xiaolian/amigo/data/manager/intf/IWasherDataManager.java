package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.trade.PayReqDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeGenerateRespDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeScanReqDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeScanRespDTO;
import com.xiaolian.amigo.data.network.model.trade.WashingModeRespDTO;
import com.xiaolian.amigo.data.network.model.user.PersonalExtraInfoDTO;

import retrofit2.http.Body;
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

    // 用户个人中心额外信息：包括我的钱包余额、我的代金券数量、是否用设备报修中、目前在进行中的订单
    Observable<ApiResult<PersonalExtraInfoDTO>> getExtraInfo();

    // 获取本地缓存的余额
    Double getLocalBalance();
}
