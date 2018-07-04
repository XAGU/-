package com.xiaolian.amigo.ui.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xiaolian.amigo.BuildConfig;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * web页面
 *
 * @author zcd
 * @date 17/9/14
 */
public class WebActivity extends BaseActivity {
    public static final String INTENT_KEY_URL = "intent_key_url";
    private static final int FILECHOOSER_RESULTCODE = 0x0012;
    private static final String TAG = WebActivity.class.getSimpleName();

    @BindView(R.id.webview)
    WebView webView;

    @BindView(R.id.fl_root)
    FrameLayout flRoot;

    ValueCallback<Uri> mUploadMessage;
    ValueCallback<Uri[]> mFilePathCallback;

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
    private boolean loadError;
    private LinearLayout ll_error_view;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        String url = getIntent().getStringExtra(INTENT_KEY_URL);
        WebSettings webSettings = webView.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setTextZoom(100);
//        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoading();
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
                showLoading();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // ignore ssl error
//                super.onReceivedSslError(view, handler, error);
            }

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

            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                // 这个方法在6.0才出现
                int statusCode = errorResponse.getStatusCode();
                Log.d(TAG, "onReceivedHttpError code = " + statusCode);
                if (404 == statusCode || 500 == statusCode) {
                    loadError = true;
                }
            }

        });
//        WebView.setWebContentsDebuggingEnabled(true);
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.addJavascriptInterface(new WebAppInterface(), "WebViewInterface");
        webSettings.setJavaScriptEnabled(true);
        if (url != null) {
            url = url.replace(BuildConfig.H5_SERVER, Constant.H5_SERVER);
            webView.loadUrl(url);
        }
        Log.i(TAG, url);

        initErrorView();
    }

    private void initErrorView() {
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(v -> super.onBackPressed());
        ll_error_view = (LinearLayout) findViewById(R.id.ll_error_view);
    }

    private class MyWebChromeClient extends WebChromeClient {

        public boolean onShowFileChooser(
                WebView webView, ValueCallback<Uri[]> filePathCallback,
                WebChromeClient.FileChooserParams fileChooserParams) {
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
}

