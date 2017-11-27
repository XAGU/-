package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;

import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * 文件接口
 * <p>
 * Created by zcd on 9/19/17.
 */

public interface IFileApi {
    // 上传文件
//    @Headers("Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
    @Multipart
    @POST("file/upload")
    Observable<ApiResult<String>> uploadFile(@Part("file\"; filename=\"file.jpg\"") RequestBody images);
}
