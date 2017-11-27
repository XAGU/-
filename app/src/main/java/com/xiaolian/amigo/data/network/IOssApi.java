package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.file.OssModel;

import retrofit2.http.POST;
import rx.Observable;

/**
 * oss Api
 * <p>
 * Created by zcd on 17/11/13.
 */

public interface IOssApi {

    // oss
    @POST("oss/credential/one")
    Observable<ApiResult<OssModel>> getOssModel();
}
