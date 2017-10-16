package com.xiaolian.amigo.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.xiaolian.amigo.R;

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
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.addJavascriptInterface(new WebAppInterface(), "WebViewInterface");
        webView.setWebChromeClient(new MyWebChromeClient());
//        webView.loadUrl("http://116.62.236.67:5098");
        webView.loadUrl(url);
    }

    @Override
    protected void setUp() {

    }

    class MyWebChromeClient extends WebChromeClient {
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

