package com.xiaolian.amigo.ui.base;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * web页面
 *
 * @author zcd
 */
public class WebActivity extends BaseActivity {
    public static final String INTENT_KEY_URL = "intent_key_url";
    private static final int FILECHOOSER_RESULTCODE = 0x0012;
    private static final String TAG = WebActivity.class.getSimpleName();

    @BindView(R.id.webview)
    WebView webView;

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
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoading();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String tag = "tel:";
                if (url.contains(tag)) {
                    String mobile = url.substring(url.lastIndexOf(":") + 1);
                    CommonUtil.call(WebActivity.this, mobile);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

        });
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.addJavascriptInterface(new WebAppInterface(), "WebViewInterface");
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
        Log.i(TAG, url);
    }

    private class MyWebChromeClient extends WebChromeClient {

        public boolean onShowFileChooser(
                WebView webView, ValueCallback<Uri[]> filePathCallback,
                WebChromeClient.FileChooserParams fileChooserParams) {
            if (mFilePathCallback != null) return true;
            mFilePathCallback = filePathCallback;
            getImage();
            return true;
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            if (mUploadMessage != null) return;
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
    public boolean supportSlideBack() {
        return false;
    }
}

