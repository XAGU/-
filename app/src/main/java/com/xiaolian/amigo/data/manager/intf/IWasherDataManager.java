package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.device.BriefDeviceDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCategoryBO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckReqDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckRespDTO;
import com.xiaolian.amigo.data.network.model.device.GetDeviceDetailReqDTO;
import com.xiaolian.amigo.data.network.model.trade.PayReqDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeGenerateRespDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeScanReqDTO;
import com.xiaolian.amigo.data.network.model.trade.QrCodeScanRespDTO;
import com.xiaolian.amigo.data.network.model.trade.WashingModeRespDTO;
import com.xiaolian.amigo.data.network.model.user.PersonalExtraInfoDTO;
import com.xiaolian.amigo.data.vo.User;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 洗衣机模块
 *
 * @author zcd
 * @date 18/1/12
 */

public interface IWasherDataManager {
    /**
     * 扫描二维码结账
     */
    Observable<ApiResult<QrCodeScanRespDTO>> scanCheckout(@Body QrCodeScanReqDTO reqDTO);

    /**
     * 生成支付二维码
     */
    Observable<ApiResult<QrCodeGenerateRespDTO>> generateQRCode(@Body PayReqDTO reqDTO);

    /**
     * 请求洗衣机模式
     */
    Observable<ApiResult<WashingModeRespDTO>> getWasherMode();

    /**
     * 存储deviceToken
     */
    void setDeviceToken(String deviceNo, String deviceToken);

    /**
     * 用户个人中心额外信息：包括我的钱包余额、我的代金券数量、是否用设备报修中、目前在进行中的订单
     */
    Observable<ApiResult<PersonalExtraInfoDTO>> getExtraInfo();

    /**
     * 获取本地缓存的余额
     */
    Double getLocalBalance();

    /**
     * 首页设备用水校验
     */
    Observable<ApiResult<DeviceCheckRespDTO>> checkDeviceUseage(@Body DeviceCheckReqDTO reqDTO);


    /**
     * 设备详情 ， 扫一扫中使用
     */
    @POST("device/one")
    Observable<ApiResult<BriefDeviceDTO>>  getDeviceDetail(@Body GetDeviceDetailReqDTO reqDTO);


    void saveDeviceCategory(List<DeviceCategoryBO> devices);

    User getUserInfo();

}
