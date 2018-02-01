package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.connecterror.DeviceConnectErrorReqDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 设备错误信息上报
 * <p>
 * Created by zcd on 18/1/17.
 */

public interface IDeviceConnectErrorApi {
    // 上报设备错误信息
    @POST("device/connect/error/report")
    Observable<ApiResult<BooleanRespDTO>> reportDeviceConnectError(@Body DeviceConnectErrorReqDTO reqDTO);
}
