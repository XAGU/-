package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.file.OssModel;

import retrofit2.http.POST;
import rx.Observable;

/**
 * oss Api
 *
 * @author zcd
 * @date 17/11/13
 */

public interface IOssApi {

    /**
     * oss token
     */
    @POST("oss/credential/one")
    Observable<ApiResult<OssModel>> getOssModel();
}
