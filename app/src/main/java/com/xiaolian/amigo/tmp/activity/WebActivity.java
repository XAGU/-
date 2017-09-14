package com.xiaolian.amigo.tmp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
public class WebActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.loadUrl("http://116.62.236.67:5098");
    }

    class MyWebChromeClient extends WebChromeClient {
    }
}
