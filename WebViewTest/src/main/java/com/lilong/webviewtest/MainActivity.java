package com.lilong.webviewtest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;

/**
 * 显示net::err_cache_miss，是因为没加INTERNET权限
 */
public class MainActivity extends Activity {

    private static final String TAG = "WTest";
    private static final String URL = "https://www.baidu.com";

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
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.i(TAG, "WebChromeClient : onReceivedTitle of " + title);
                getActionBar().setTitle("网页标题 = " + title);
            }
        });

        /**
         * WebView的缓存：有5种，只有(1)是默认启用的，其他需要符合启用条件
         * (1) 浏览器缓存:
         *      缓存内容：
         *              JS CSS 字体 图片 cookies
         *     缓存条件：
         *              根据http头中的cache-control(第一级缓存)和last-modified/etag(第二级缓存)来决定
         *     缓存位置：
         *              /data/data/包名/app_webview/
         *              /data/data/包名/cache/org.chromium.android_webview
         *
         * (2) Application缓存:
         *     缓存内容：
         *              由manifest文件决定
         *     缓存条件：
         *              只对于html5页面有效，而且要求其中存在manifest属性和manifest文件，否则只创建个目录，但里面没有内容
         *              还要求{@link WebSettings#setAppCacheEnabled(boolean)}为true
         *     缓存位置：
         *              {@link WebSettings#setAppCachePath(String)}设定
         *
         * (3) Dom缓存：
         *     缓存内容：
         *             ?
         *     缓存条件：
         *             {@link WebSettings#setDomStorageEnabled(boolean)}为true
         *     缓存位置：
         *             ?
         *
         * (4) SQL数据库缓存：
         *     缓存内容：
         *             ?
         *     缓存条件：
         *            {@link WebSettings#setDatabaseEnabled(boolean)}为true
         *     缓存位置：
         *             {@link WebSettings#setDatabasePath(String)}指定
         *
         * (5) Indexed数据库缓存：
         *     缓存内容：
         *            ?
         *     缓存条件：
         *            {@link WebSettings#setJavaScriptEnabled(boolean)}为true
         *     缓存位置：
         *            /data/data/包名/app_webview/Local Storage
         *
         * */
        // 浏览器缓存
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // Application缓存
        webView.getSettings().setAppCacheEnabled(true);
        String appCacheDir = getDir("cache1", Context.MODE_PRIVATE).getPath();
        webView.getSettings().setAppCachePath(appCacheDir);
        webView.getSettings().setAppCacheMaxSize(5 * 1024 * 1024);
        // Dom缓存
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(URL);
        // SQL数据库缓存
        String sqlCacheDir = getDir("cache2", Context.MODE_PRIVATE).getPath();
        webView.getSettings().setDatabasePath(sqlCacheDir);
        webView.getSettings().setDatabaseEnabled(true);
        // Indexed数据库缓存
        webView.getSettings().setJavaScriptEnabled(true);
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
