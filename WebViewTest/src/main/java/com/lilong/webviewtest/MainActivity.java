package com.lilong.webviewtest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 显示net::err_cache_miss，是因为没加INTERNET权限
 */
public class MainActivity extends Activity {

    private static final String TAG = "WTest";
    private static final String URL = "http://www.baidu.com";

    private SwipeRefreshLayout swipeRefreshLayout;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        webView = findViewById(R.id.webView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl(URL);
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            /**
             * 一个新的链接即将被加载时，在这里进行处理
             * 这个方法用来拦截网页中的某些链接，指示webView不要动，后续由app的代码负责处理
             *
             * 注意这个方法的[触发条件]!
             *
             * 触发的情况:
             * (1) 重定向
             * (2) 点击网页中的链接
             *
             * 不触发的情况：
             * (1) POST请求不触发
             * (2) 调用{@link WebView#goBack()}加载的url不触发
             * (3) 调用{@link WebView#loadUrl(String)}加载的url不触发，也就是说必须是网页中的链接被点击才触发
             *
             * 点击网页中的链接：
             * 如果不设置{@link WebViewClient}，由ActivityManager决定由什么应用来处理这个链接
             * 如果设置{@link WebViewClient}，则肯定由本应用处理，先执行shouldOverrideUrlLoading里的代码
             *      (1) @return true 　然后就结束了
             *      (2) @return false  然后webview会加载这个链接
             * */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.i(TAG, "WebViewClient : shouldOverrideUrlLoading (isRedirect = " + request.isRedirect() + ")of " + request.getUrl());
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i(TAG, "WebViewClient : onPageStarted of " + url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                // 太多了，屏蔽
//                Log.i(TAG, "WebViewClient : onLoadResource of " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i(TAG, "WebViewClient : onPageFinished of " + url);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.i(TAG, "WebViewClient : onReceivedError of " + request.getUrl());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        webView.loadUrl(URL);
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
