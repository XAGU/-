package com.xiaolian.amigo.data.base;

import android.text.TextUtils;
import android.util.Log;

import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Log拦截器
 *
 * @author zcd
 */

public class LogInterceptor implements Interceptor {
    private final static String TAG = LogInterceptor.class.getSimpleName();
    private final static boolean DEBUG = true;
//    private final static String TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaXNzIjoiaHR0cHM6Ly94aWFvbGlhbi5pbyIsImlhdCI6MTUwNTczOTM1NCwiZXhwIjoxNTA2NDk1MzU0fQ.0MWlDmGbf_GRb8g6zAsyN3nVSseF9Hb_mlO7vM3HN60kgkqnzOoqh2hfRc4i2CXaxMH82CX0liSneL3OTVY3wA";

    private long lastTime = 0;
    private Request lastRequest;
    private final static long NETWORK_INTERVAL = 500;

    ISharedPreferencesHelp sharedPreferencesHelp;

    public LogInterceptor(ISharedPreferencesHelp sharedPreferencesHelp) {
        this.sharedPreferencesHelp = sharedPreferencesHelp;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest;
        String token = sharedPreferencesHelp.getToken();
        if (token == null) {
            token = "";
        }

        if (request.url().url().getPath().startsWith("/trade")) {
            String deviceToken = sharedPreferencesHelp.getCurrentDeviceToken();
            if (deviceToken == null) {
                deviceToken = "";
            }

            newRequest = request.newBuilder()
                    // 添加token
                    .addHeader("token", token)
                    .addHeader("device_token", deviceToken)
                    .build();

        } else {
            newRequest = request.newBuilder()
                    // 添加token
                    .addHeader("token", token)
                    .build();

        }

        if (lastTime == 0 || lastRequest == null) {
            lastTime = Calendar.getInstance().getTimeInMillis();
            lastRequest = newRequest;
        } else {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastTime < NETWORK_INTERVAL) {
                if (isRequestEqual(newRequest, lastRequest)) {
                    return new Response.Builder()
                            .code(600) //Simply put whatever value you want to designate to aborted request.
                            .request(chain.request())
                            .build();
                }
            }
            lastRequest = newRequest;
            lastTime = Calendar.getInstance().getTimeInMillis();
        }
        String url = newRequest.url().toString();
        String header = newRequest.headers().toString();
        okhttp3.Response response = chain.proceed(newRequest);
        okhttp3.MediaType mediaType = response.body().contentType();
        String content;
        if (null != response.header("device_token")) {
            // 有device_token,一定配对一个macAddress
            String macAddress = response.header("macAddress");
            sharedPreferencesHelp.setDeviceToken(macAddress, response.header("device_token"));
        }
        if (response.body() != null) {
            content = response.body().string();
        } else {
            content = "";
        }
        int code = response.code();
        if (DEBUG) {
            if (!newRequest.method().equals("GET")) {
                String requestContent = bodyToString(newRequest.body());
                Log.d(TAG, "url: " + url + "\n" + "code: " + code + "\n" + "request header: " + header + "\n" + "request body: " + requestContent + "\n" + "response header: "
                        + response.headers().toString() + "\n" + "response body: " + content);
            } else {
                Log.d(TAG, "url: " + url + "\n" + "code: " + code + "\n" + "request header: " + header + "\n" + "response body: " + content);
            }
        }

        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }

    private boolean isRequestEqual(Request request, Request lastRequest) {
        if (!TextUtils.equals(request.url().toString(), lastRequest.url().toString())) {
            return false;
        }
        if (request.headers().size() != lastRequest.headers().size()) {
            return false;
        }
        if (!TextUtils.equals(request.headers().get("token"), lastRequest.headers().get("token"))) {
            return false;
        }
        if (!TextUtils.equals(request.headers().get("device_token"), lastRequest.headers().get("device_token"))) {
            return false;
        }
        if (!TextUtils.equals(request.method(), lastRequest.method())) {
            return false;
        }
        if (request.method().equals("POST")) {
            if (!TextUtils.equals(bodyToString(request.body()), bodyToString(lastRequest.body()))) {
                return false;
            }
        }
        return true;
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
