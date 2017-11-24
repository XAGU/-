package com.xiaolian.amigo.data.base;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.util.Log;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Calendar;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Log拦截器
 *
 * @author zcd
 */

public class LogInterceptor implements Interceptor {
    private final static String TAG = LogInterceptor.class.getSimpleName();
    private final static boolean DEBUG = true;
    private final static String TOKEN = "token";
    private final static String DEVICE_TOKEN = "deviceToken";
    private final static String GET = "GET";
    private final static String POST = "POST";
    private long lastTime = 0;
    private Request lastRequest;
    private final static long NETWORK_INTERVAL = 500;

    private ISharedPreferencesHelp sharedPreferencesHelp;

    public LogInterceptor(ISharedPreferencesHelp sharedPreferencesHelp) {
        this.sharedPreferencesHelp = sharedPreferencesHelp;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
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
                    .addHeader(TOKEN, token)
                    .addHeader(DEVICE_TOKEN, deviceToken)
                    .build();

        } else {
            newRequest = request.newBuilder()
                    // 添加token
                    .addHeader(TOKEN, token)
                    .build();

        }

        if (lastTime == 0 || lastRequest == null) {
            lastTime = Calendar.getInstance().getTimeInMillis();
            lastRequest = newRequest;
        } else {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastTime < NETWORK_INTERVAL
                    && isRequestEqual(newRequest, lastRequest)) {
                Log.w(TAG, "请求间隔过短 url:" + request.url());
                lastRequest = null;
                lastTime = Calendar.getInstance().getTimeInMillis();
                return new Response.Builder()
                        .code(600) //Simply put whatever value you want to designate to aborted request.
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_1)
                        .message("ignore message")
                        .body(ResponseBody.create(null, new byte[0]))
                        .build();
            }
            lastRequest = newRequest;
            lastTime = Calendar.getInstance().getTimeInMillis();
        }
        String url = newRequest.url().toString();
        String header = newRequest.headers().toString();
        Response response;
        try {
            response = chain.proceed(newRequest);
        } catch (Exception e) {
            Log.wtf(TAG, "网络请求错误: " + newRequest.url(), e);
            if (e instanceof ConnectException) {
                throw e;
            } else {
                return new Response.Builder()
                        .code(600) //Simply put whatever value you want to designate to aborted request.
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_1)
                        .message("ignore message")
                        .body(ResponseBody.create(null, new byte[0]))
                        .build();
            }
        }
        if (null != response.header(DEVICE_TOKEN)) {
            // 有device_token,一定配对一个macAddress
            String macAddress = response.header("macAddress");
            sharedPreferencesHelp.setDeviceToken(macAddress, response.header(DEVICE_TOKEN));
        }
        String content;
        MediaType mediaType;
        if (response.body() != null) {
            content = response.body().string();
            mediaType = response.body().contentType();
        } else {
            content = "";
            mediaType = MediaType.parse("application/json;charset=UTF-8");
        }
        int code = response.code();
        if (DEBUG) {
            if (!newRequest.method().equals(GET)) {
                String requestContent = bodyToString(newRequest.body());
                Log.d(TAG, "url: " + url + "\n" + "code: " + code + "\n" + "request header: " + header + "\n" + "request body: " + requestContent + "\n" + "response header: "
                        + response.headers().toString() + "\n" + "response body: " + content);
            } else {
                Log.d(TAG, "url: " + url + "\n" + "code: " + code + "\n" + "request header: " + header + "\n" + "response body: " + content);
            }
        }

        return response.newBuilder()
                .body(ResponseBody.create(mediaType, content))
                .build();
    }

    private boolean isRequestEqual(Request request, Request lastRequest) {
        if (!TextUtils.equals(request.url().toString(), lastRequest.url().toString())) {
            return false;
        }
        if (request.headers().size() != lastRequest.headers().size()) {
            return false;
        }
        if (!TextUtils.equals(request.headers().get(TOKEN), lastRequest.headers().get(TOKEN))) {
            return false;
        }
        if (!TextUtils.equals(request.headers().get(DEVICE_TOKEN), lastRequest.headers().get(DEVICE_TOKEN))) {
            return false;
        }
        if (!TextUtils.equals(request.method(), lastRequest.method())) {
            return false;
        }
        if (request.method().equals(POST)) {
            if (!TextUtils.equals(bodyToString(request.body()), bodyToString(lastRequest.body()))) {
                return false;
            }
        }
        return true;
    }

    private String bodyToString(final RequestBody request) {
        try {
            final Buffer buffer = new Buffer();
            request.writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
