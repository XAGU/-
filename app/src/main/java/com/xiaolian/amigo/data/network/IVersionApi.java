package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.version.VersionDTO;

import retrofit2.http.POST;
import rx.Observable;

/**
 * 版本更新相关api
 *
 * @author zcd
 * @date 17/12/14
 */

public interface IVersionApi {
    /**
     * 获取更新信息
     */
    @POST("version/one")
    Observable<ApiResult<VersionDTO>> getUpdateInfo();
}
