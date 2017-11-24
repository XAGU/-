package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.CheckVersionUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BaseInfoDTO;
import com.xiaolian.amigo.data.network.model.dto.response.CheckVersionUpdateRespDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 系统信息
 * <p>
 * Created by zcd on 17/11/24.
 */

public interface ISystemApi {
    // 基础信息
    @POST("/system/baseinfo")
    Observable<ApiResult<BaseInfoDTO>> getSystemBaseInfo();

    // 版本更新查询
    @POST("/system/version/check/update")
    Observable<ApiResult<CheckVersionUpdateRespDTO>> checkUpdate(@Body CheckVersionUpdateReqDTO reqDTO);

}
