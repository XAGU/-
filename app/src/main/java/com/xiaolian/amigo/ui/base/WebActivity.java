package com.xiaolian.amigo.ui.base;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * web页面
 * @author zcd
 */
public class WebActivity extends BaseActivity {
    public static final String INTENT_KEY_URL = "intent_key_url";

    @BindView(R.id.webview)
    WebView webView;

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
        webView.setWebViewClient(new WebViewClient(){
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
        webView.addJavascriptInterface(new WebAppInterface(), "WebViewInterface");
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
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
}

