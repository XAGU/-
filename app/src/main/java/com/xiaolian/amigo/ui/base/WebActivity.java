package com.xiaolian.amigo.ui.base;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
import com.tencent.smtt.export.external.interfaces.ClientCertRequest;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.zxing.client.android.Intents;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
import com.xiaolian.amigo.BuildConfig;
import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.device.JsWasher;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.di.componet.DaggerMainActivityComponent;
import com.xiaolian.amigo.di.module.MainActivityModule;
import com.xiaolian.amigo.ui.device.washer.ScanActivity;
import com.xiaolian.amigo.ui.login.LoginActivity;
import com.xiaolian.amigo.ui.wallet.RechargeActivity;
import com.xiaolian.amigo.util.AppUtils;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xiaolian.amigo.util.StringUtils.appendToken;

/**
 * web页面
 *
 * @author zcd
 * @date 17/9/14
 */
public class WebActivity extends BaseActivity {
    private static final String UTF8 = "UTF-8";
    public static final String INTENT_KEY_URL = "intent_key_url";
    public static final String INTENT_KEY_WASHER_URL = "intent_key_url_washer";
    private static final String TAG = WebActivity.class.getSimpleName();
    private boolean isCharge = false;

    @BindView(R.id.webview)
    WebView webView;

    @BindView(R.id.fl_root)
    FrameLayout flRoot;

    ValueCallback<Uri> mUploadMessage;
    ValueCallback<Uri[]> mFilePathCallback;

    @Inject
    ISharedPreferencesHelp sharedPreferencesHelp ;

    ImageCallback imageCallback = new ImageCallback() {
        @Override
        public void callback(Uri imageUri) {
            if (mFilePathCallback != null) {
                Uri[] uris = new Uri[1];
                uris[0] = imageUri;
                mFilePathCallback.onReceiveValue(uris);
            } else {
                mUploadMessage.onReceiveValue(imageUri);
            }
            mFilePathCallback = null;
            mUploadMessage = null;

        }
    };
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.loading_rl)
    RelativeLayout loadingRl;
    private boolean loadError;
    private LinearLayout ll_error_view;
    private String scanResult;
    private String name;
    private String type;


    ValueAnimator loadingAnimator;

    int[] loadingRes = new int[]{
            R.drawable.loading_one, R.drawable.loading_two,
            R.drawable.loading_three, R.drawable.loading_four
    };

    private String url ;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        DaggerMainActivityComponent.builder()
                .mainActivityModule(new MainActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build().inject(this);
        url = getIntent().getStringExtra(INTENT_KEY_URL);
        android.util.Log.e(TAG, "onCreate: " + url );
        initLoadingAnim();
        if (url == null) {
            url = getIntent().getStringExtra(INTENT_KEY_WASHER_URL);
        }
        com.tencent.smtt.sdk.WebSettings webSettings = webView.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setTextZoom(100);
        //debug模式下开启调试
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
//            WebView.setWebContentsDebuggingEnabled(true);
//        }
//        webView.setWebChromeClient(new WebChromeClient());
        url = addUrlSuffix(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideBlogLoading();
                if (!isNetworkAvailable()) {
                    ll_error_view.setVisibility(View.VISIBLE);
                    return;
                }
                if (loadError) {
                    ll_error_view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showBlogLoading();
            }

//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                // ignore ssl error
//                super.onReceivedSslError(view, handler, error);
//            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String tag = "tel:";
                if (url.contains(tag)) {
                    String mobile = url.substring(url.lastIndexOf(':') + 1);
                    CommonUtil.call(WebActivity.this, mobile);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                loadError = true;
            }

            @Override
            public void onReceivedHttpError(WebView webView, com.tencent.smtt.export.external.interfaces.WebResourceRequest webResourceRequest, com.tencent.smtt.export.external.interfaces.WebResourceResponse webResourceResponse) {
                super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
                // 这个方法在6.0才出现
                int statusCode = webResourceResponse.getStatusCode();
                Log.d(TAG, "onReceivedHttpError code = " + statusCode);
                if (404 == statusCode || 500 == statusCode) {
                    loadError = true;
                }
            }

//            @TargetApi(Build.VERSION_CODES.M)
//            @Override
//            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
//                super.onReceivedHttpError(view, request, errorResponse);
//                // 这个方法在6.0才出现
//                int statusCode = errorResponse.getStatusCode();
//                Log.d(TAG, "onReceivedHttpError code = " + statusCode);
//                if (404 == statusCode || 500 == statusCode) {
//                    loadError = true;
//                }
//            }

        });
//        WebView.setWebContentsDebuggingEnabled(true);
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.addJavascriptInterface(new WebAppInterface(), "WebViewInterface");
        webSettings.setJavaScriptEnabled(true);
        if (url != null && !TextUtils.isEmpty(url)) {
            url = url.replace(BuildConfig.H5_SERVER, Constant.H5_SERVER);
            webView.loadUrl(url);
        }
        Log.i(TAG, url);

        initErrorView();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (TextUtils.isEmpty(url)) {
            if (TextUtils.isEmpty(sharedPreferencesHelp.getReferToken()) || TextUtils.isEmpty(sharedPreferencesHelp.getAccessToken())) {
                startActivity(new Intent(WebActivity.this, LoginActivity.class));
                return;
            }
            String pushUrl  ;
            XGPushClickedResult clickedResult = XGPushManager.onActivityStarted(this);
            if (clickedResult == null) return;
            //获取消息附近参数
            String customContent = clickedResult.getCustomContent();
            //获取消息标题
            String set = clickedResult.getTitle();
            //获取消息内容
            String s = clickedResult.getContent();

            String targetId = null ;

            String targetUri = null ;

            if (customContent != null && customContent.length() != 0) {
                try {
                    JSONObject obj = new JSONObject(customContent);

                    if (!obj.isNull("targetUri")) {
                        targetUri = obj.getString("targetUri");
                    }
                    if (!obj.isNull("targetId")) {
                        targetId = obj.getString("targetId");
                    }
                    pushUrl = Constant.H5_SERVER + targetUri
                            + "?accessToken=" + sharedPreferencesHelp.getAccessToken()
                            + "&refreshToken=" + sharedPreferencesHelp.getReferToken()
                            + "&id="+ targetId;
                    pushUrl = pushUrl.replace(BuildConfig.H5_SERVER, Constant.H5_SERVER);
                    pushUrl = addUrlSuffix(pushUrl);
                    webView.loadUrl(pushUrl);
                    android.util.Log.d(TAG, pushUrl);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initLoadingAnim() {
        loadingAnimator = ValueAnimator.ofInt(0, 3, 0);
        loadingAnimator.setRepeatCount(ValueAnimator.INFINITE);
        loadingAnimator.setDuration(1000);
        loadingAnimator.setInterpolator(new LinearInterpolator());
        loadingAnimator.addUpdateListener(animation -> {
            int currentValue = (int) animation.getAnimatedValue();
            if (ivLoading != null)
                ivLoading.setImageResource(loadingRes[currentValue]);
        });
    }

    private void initErrorView() {
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(v -> super.onBackPressed());
        ll_error_view =  findViewById(R.id.ll_error_view);
    }

    /**
     * 添加url后缀
     */
    private String addUrlSuffix(String url) {

        if (url == null || TextUtils.isEmpty(url)) return  "";
        String newUrl = "";
        try {
            String model = Build.MODEL;
            String brand = Build.BRAND;
            String appVersion = AppUtils.getVersionName(this);
            int systemVersion = Build.VERSION.SDK_INT;
            String mobile = sharedPreferencesHelp.getUserInfo().getMobile();
            if (sharedPreferencesHelp.getUserInfo() != null) {
                if (url.contains("?")) {
                    newUrl = url + "&model=" + URLEncoder.encode(model, "UTF-8") + "&brand=" + brand + "&appVersion=" + appVersion + "&systemVersion="
                            + systemVersion + "&mobile=" + mobile;
                } else {
                    newUrl = url + "?model=" + URLEncoder.encode(model, "UTF-8") + "&brand=" + brand + "&appVersion=" + appVersion + "&systemVersion="
                            + systemVersion + "&mobile=" + mobile;
                }
            } else {
                newUrl = url;
            }
        }catch (UnsupportedEncodingException e){
            Log.e(TAG ,e.getMessage());
        }

        //  将后面拼接的参数全部用base64加密，防止（）h5读取不了
        android.util.Log.e(TAG, "addUrlSuffix: " + newUrl );
        if (newUrl.contains("?")){
          String[] urls =   newUrl.split("\\?");
          if (urls.length >=2 ) {
              String oldString = urls[1];
              try {
               String newString =   new  String(android.util.Base64.encode(oldString.getBytes(UTF8), android.util.Base64.NO_WRAP), UTF8);
               newUrl = urls[0]+"?params="+newString ;
              } catch (UnsupportedEncodingException e) {
                  e.printStackTrace();
              }
          }
        }
        android.util.Log.e(TAG, "addUrlSuffix: " + newUrl);
        return newUrl;
    }

    public void showBlogLoading() {
        loadingRl.setVisibility(View.VISIBLE);
        if (loadingAnimator == null) return;

        if (loadingAnimator.isRunning()) {
            loadingAnimator.cancel();
        }
        loadingAnimator.start();
    }

    public void hideBlogLoading() {

        if (loadingAnimator == null) return;

        if (loadingAnimator.isRunning()) loadingAnimator.cancel();

        if (loadingRl != null) loadingRl.setVisibility(View.GONE);
    }

    private class MyWebChromeClient extends com.tencent.smtt.sdk.WebChromeClient {

        public boolean onShowFileChooser(
                WebView webView, ValueCallback<Uri[]> filePathCallback,
                FileChooserParams fileChooserParams) {
            if (mFilePathCallback != null) {
                return true;
            }
            mFilePathCallback = filePathCallback;
            getImage();
            return true;
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            if (mUploadMessage != null) {
                return;
            }
            mUploadMessage = uploadMsg;
            getImage();
        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        // For Android  > 4.1.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            openFileChooser(uploadMsg, acceptType);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            //判断标题 title 中是否包含有“error”字段，如果包含“error”字段，则设置加载失败，显示加载失败的视图
            Log.d(TAG, "title: " + title);
            if (!TextUtils.isEmpty(title) && title.toLowerCase().contains("error")) {
                loadError = true;
            } else if (!TextUtils.isEmpty(title) && title.toLowerCase().contains("404")) {
                loadError = true;
            } else if (!TextUtils.isEmpty(title) && title.toLowerCase().contains("500")) {
                loadError = true;
            }
        }

    }


    private void getImage() {
        getImage(imageCallback);
        emptyImageCallback = new EmptyImageCallback() {
            @Override
            public void callback() {
                if (mFilePathCallback != null) {
                    Uri[] uris = new Uri[]{};
                    mFilePathCallback.onReceiveValue(uris);
                } else {
                    mUploadMessage.onReceiveValue(null);
                }
                mFilePathCallback = null;
                mUploadMessage = null;
            }
        };
    }


    @Override
    protected void setUp() {

    }

    public class WebAppInterface {
        @JavascriptInterface
        public void backToNative() {
            finish();
        }

        @JavascriptInterface
        public void send(String str){
            Gson gson = new Gson();
            JsWasher waher = gson.fromJson(str,JsWasher.class);
            name = waher.getData().getName();
            type = waher.getData().getType();
            android.util.Log.e(TAG, "send: >>>>> accessToken " + str);
            if ("scan".equals(type)) {
                scan();
            }else if("recharge".equals(type)){
                goToPayActivity();
            }else if ("tokenExchange".equals(type)){
                String accessToken =waher.getData().getAccessToken();
                String refreshToken = waher.getData().getRefreshToken();

                if (!TextUtils.isEmpty(accessToken)) sharedPreferencesHelp.setAccessToken(accessToken);

                if (!TextUtils.isEmpty(refreshToken)) sharedPreferencesHelp.setReferToken(refreshToken);

            }
        }
    }

    private void goToPayActivity() {
        isCharge = true;
        Intent intent = new Intent(this,RechargeActivity.class);
        startActivityForResult(intent,0x11);

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        flRoot.removeView(webView);
        webView.destroy();
        super.onDestroy();
    }

    @Override
    public boolean supportSlideBack() {
        return false;
    }

    private void scan(){
        Intent intent = new Intent(WebActivity.this,ScanActivity.class);
        intent.putExtra(ScanActivity.INTENT_URL_WASHER,true);
        //禁止扫码成功后的声音
        intent.putExtra(Intents.Scan.BEEP_ENABLED,false);
        WebActivity.this.startActivityForResult(intent,0x11);

    }

    private void invokeJs(){
        JsWasher.DataBean data = new JsWasher.DataBean();
        data.setName(name);
        data.setType(type);
        data.setValue(scanResult);
        Gson gson = new Gson();
        String result = gson.toJson(data);
        android.util.Log.e(TAG, "invokeJs: re=" + result );
        String method = "javascript:_nativeMsgCallback('" + result + "')";

        webView.post(()->{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView.evaluateJavascript(method, new com.tencent.smtt.sdk.ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {

                    }
                });
            }else{
                webView.loadUrl(method);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0x11 && isCharge){
            invokeJs();
            android.util.Log.e(TAG, "onActivityResult: ----"+isCharge );
            isCharge = false;
            return;
        }

        if (requestCode == 0x11 && resultCode == RESULT_OK) {
            scanResult = data.getStringExtra("data");
            invokeJs();
        }
    }
}

