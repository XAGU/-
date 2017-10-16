package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.CmdResultReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.ConnectCommandReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.PayReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.ScanDeviceReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.CmdResultRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.ConnectCommandRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.PayRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.ScanDeviceRespDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 用水交易相关接口
 * <p>
 * Created by caidong on 2017/9/15.
 */
public interface ITradeApi {

    // 请求连接设备指令
    @POST("/trade/device/connect")
    Observable<ApiResult<ConnectCommandRespDTO>> getConnectCommand(@Body ConnectCommandReqDTO reqDTO);

    // 请求处理设备指令响应结果
    @POST("/trade/device/command-result/process")
    Observable<ApiResult<CmdResultRespDTO>> processCmdResult(@Body CmdResultReqDTO reqDTO);

    // 网络支付，创建用水订单
    @POST("/trade/pay")
    Observable<ApiResult<PayRespDTO>> pay(@Body PayReqDTO reqDTO);

    // 处理扫描结果
    @POST("/device/water/list")
    Observable<ApiResult<ScanDeviceRespDTO>> handleScanDevices(@Body ScanDeviceReqDTO reqDTO);
}
