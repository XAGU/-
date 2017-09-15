package com.xiaolian.amigo.data.base;

import android.util.Log;

import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.data.prefs.SharedPreferencesHelp;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Log拦截器
 * @author zcd
 */

public class LogInterceptor implements Interceptor {
    private final static String TAG = LogInterceptor.class.getSimpleName();
    private final static boolean DEBUG = true;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest;
        newRequest = request.newBuilder()
                // 添加token
                .addHeader("Authorization", "123")
                .build();
        String url = newRequest.url().toString();
        String header = newRequest.headers().toString();
        okhttp3.Response response = chain.proceed(newRequest);
        okhttp3.MediaType mediaType = response.body().contentType();
        String content;
        if (response.body() != null) {
            content = response.body().string();
        } else {
            content = "";
        }
        if (DEBUG) {
            if (!newRequest.method().equals("GET")) {
                String requestContent = bodyToString(newRequest.body());
                Log.d(TAG, "url: " + url + "\n" + "header: " + header + "\n" + "request body: " + requestContent
                        + "\n" + "response body: " + content);
            } else {
                Log.d(TAG, "url: " + url + "\n" + "header: " + header + "\n" + "response body: " + content);
            }
        }

        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }

    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            copy.writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
