package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.connecterror.DeviceConnectErrorReqDTO;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
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


    /**
     * 上报错误日志
     */
    @POST("/android/crash/upload/log")
    Observable<ApiResult<BooleanRespDTO>> uploadLog(@Body RequestBody body);
}
