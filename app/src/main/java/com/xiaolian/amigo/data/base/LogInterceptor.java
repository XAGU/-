package com.xiaolian.amigo.data.base;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xiaolian.amigo.BuildConfig;
import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

import static com.xiaolian.amigo.util.StringUtils.appendToken;

/**
 * Log拦截器
 *
 * @author zcd
 * @date 17/9/14
 */

public class LogInterceptor implements Interceptor {
    private final static String TAG = LogInterceptor.class.getSimpleName();
    private final static boolean DEBUG = true;

    private final static String REFER_TOKEN = "refreshToken";

    private final static String ACCESS_TOKEN = "accessToken";
    private final static String DEVICE_TOKEN = "deviceToken";
    private final static String GET = "GET";
    private final static String POST = "POST";
    private final static String TRADE_PREFIX = BuildConfig.TRADE_PREFIX;
    private final static boolean isDev = !TextUtils.equals(BuildConfig.FLAVOR, "prod");
    /**
     * 该链接不上传deviceToken
     */
    private final static String ANTI_TRADE_PREFIX = "trade/qrCode/scan";
    /**
     * 该链接上传deviceToken
     */
    private final static String UPDAT_RATE_PREFIX = "device/rate/send";
    private long lastTime = 0;
    private Request lastRequest;
    private final static long NETWORK_INTERVAL = 500;
    private final static boolean ENABLE_ANTI_SHAKE = false;

    private final static String APP_VERSION = "appVersion";
    private final static String SYSTEM_VERSION = "systemVersion";
    private final static String BRAND = "brand";
    private final static String MODEL = "model";
    private final static String UNIQUE_ID = "uniqueId";
    private final static String SYSTEM = "system";
    private String appVersion;
    private String systemVersion;
    private String brand;
    private String model;
    private String uniqueId;
    private static final String system = "2";
    private String server;
    private String bathServer;
    private final String oldServer = BuildConfig.SERVER;
    private final String oldBathServer = BuildConfig.SERVER_BATHROOM;

    private ISharedPreferencesHelp sharedPreferencesHelp;

    public LogInterceptor(ISharedPreferencesHelp sharedPreferencesHelp) {
        this.sharedPreferencesHelp = sharedPreferencesHelp;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setH5Server(String h5Server) {
        Constant.H5_SERVER = h5Server;
    }

    public void setBathServer(String bathServer) {
        this.bathServer = bathServer;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest;

        String accessToken;

        String refreshToken;

        if (TextUtils.isEmpty(MvpApp.accessToken) || TextUtils.isEmpty(MvpApp.refreshToken)) {
            accessToken = MvpApp.accessToken;
            refreshToken = MvpApp.refreshToken;
        } else {
            accessToken = sharedPreferencesHelp.getAccessToken();
            refreshToken = sharedPreferencesHelp.getReferToken();
        }

        if ((request.url().url().getPath().contains(TRADE_PREFIX)
                && (!request.url().url().getPath().contains(ANTI_TRADE_PREFIX))) || request.url().url().getPath().contains(UPDAT_RATE_PREFIX)) {
            String deviceToken = sharedPreferencesHelp.getCurrentDeviceToken();
            if (deviceToken == null) {
                deviceToken = "";
            }

            if (isDev && (!TextUtils.isEmpty(bathServer) || !TextUtils.isEmpty(server))) {
                String newUrl = request.url().toString().replace(oldServer, server);
                Log.d(TAG, newUrl);
                newUrl = newUrl.replace(oldBathServer, bathServer);
                Log.d(TAG, newUrl);
                newRequest = request.newBuilder()
                        // 添加token
                        .url(newUrl)
                        .addHeader(ACCESS_TOKEN, accessToken)
                        .addHeader(REFER_TOKEN, refreshToken)
                        .addHeader(DEVICE_TOKEN, deviceToken)
                        .build();
            } else {
                newRequest = request.newBuilder()
                        // 添加token
                        .addHeader(ACCESS_TOKEN, accessToken)
                        .addHeader(REFER_TOKEN, refreshToken)
                        .addHeader(DEVICE_TOKEN, deviceToken)
                        .build();
            }

        } else {
            if (isDev && (!TextUtils.isEmpty(server) || !TextUtils.isEmpty(bathServer))) {
                String newUrl = request.url().toString().replace(oldServer, server);
                newUrl = newUrl.replace(oldBathServer, bathServer);
                newRequest = request.newBuilder()
                        // 添加token
                        .url(newUrl)
                        .addHeader(ACCESS_TOKEN, accessToken)
                        .addHeader(REFER_TOKEN, refreshToken)
                        .build();
            } else {
                newRequest = request.newBuilder()
                        // 添加token
                        .addHeader(ACCESS_TOKEN, accessToken)
                        .addHeader(REFER_TOKEN, refreshToken)
                        .build();
            }
        }

        if (lastTime == 0 || lastRequest == null) {
            lastTime = Calendar.getInstance().getTimeInMillis();
            lastRequest = newRequest;
        } else {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastTime < NETWORK_INTERVAL
                    && isRequestEqual(newRequest, lastRequest) && ENABLE_ANTI_SHAKE) {
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
            newRequest = addParam(newRequest);
            response = chain.proceed(newRequest);
        } catch (Exception e) {
            Log.wtf(TAG, "网络请求错误: " + newRequest.url(), e);
            throw e;
        }

        if (null != response.header(DEVICE_TOKEN)) {
            // 有device_token,一定配对一个macAddress
            String macAddress = response.header("macAddress");
            sharedPreferencesHelp.setDeviceToken(macAddress, response.header(DEVICE_TOKEN));
        }

        String responseAccessToken = response.header(ACCESS_TOKEN);
        String responseRefreshToken = response.header(REFER_TOKEN);
        if (!TextUtils.isEmpty(responseAccessToken)
                && TextUtils.isEmpty(responseRefreshToken)) {

            synchronized (this) {
                updateToken(responseAccessToken, responseRefreshToken);
                sharedPreferencesHelp.setAppendToken(appendToken(responseAccessToken, responseRefreshToken));
            }
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

        if (!TextUtils.equals(request.headers().get(ACCESS_TOKEN), lastRequest.headers().get(ACCESS_TOKEN))) {
            return false;
        }

        if (!TextUtils.equals(request.headers().get(REFER_TOKEN), lastRequest.headers().get(REFER_TOKEN))) {
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

    // 添加公共参数
    private Request addParam(Request oldRequest) {
        if (oldRequest.method().equals(GET)) {
            return oldRequest;
        }
        if (oldRequest.body() != null) {
            try {
                String oldBody = bodyToString(oldRequest.body());
                if (TextUtils.isEmpty(oldBody)) {
                    return oldRequest;
                }
                JSONObject json = new JSONObject(oldBody);
                if (!json.has(APP_VERSION)) {
                    json.put(APP_VERSION, appVersion);
                }
                if (!json.has(SYSTEM_VERSION)) {
                    json.put(SYSTEM_VERSION, systemVersion);
                }
                if (!json.has(BRAND)) {
                    json.put(BRAND, brand);
                }
                if (!json.has(MODEL)) {
                    json.put(MODEL, model);
                }
                if (!json.has(UNIQUE_ID)) {
                    json.put(UNIQUE_ID, uniqueId);
                }
                if (!json.has(SYSTEM)) {
                    json.put(SYSTEM, system);
                }
                MediaType contentType = oldRequest.body().contentType();
                RequestBody body = RequestBody.create(contentType, json.toString());
                return oldRequest.newBuilder()
                        .method(oldRequest.method(), body)
                        .build();
            } catch (JSONException e) {
                Log.wtf(TAG, e);
                return oldRequest;
            } catch (Exception e) {
                Log.wtf(TAG, e);
                return oldRequest;
            }
        } else {
            return oldRequest;
        }
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     *
     *
     * @param accessToken
     * @param refreshToken
     */
    private void updateToken(String accessToken, String refreshToken) {
        if (TextUtils.isEmpty(MvpApp.accessToken) || !MvpApp.accessToken.equals(accessToken))
            MvpApp.accessToken = accessToken;
        if (TextUtils.isEmpty(MvpApp.refreshToken) || !MvpApp.refreshToken.equals(refreshToken))
            MvpApp.refreshToken = refreshToken;
    }
}
