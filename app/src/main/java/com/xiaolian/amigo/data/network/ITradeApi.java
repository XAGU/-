package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.CmdResultReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.OrderReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.CmdResultRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.ConnectCommandRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.OrderRespDTO;

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
    Observable<ApiResult<ConnectCommandRespDTO>> getConnectCommand();

    // 请求处理设备指令响应结果
    Observable<ApiResult<CmdResultRespDTO>> processCmdResult(CmdResultReqDTO reqDTO);
}
