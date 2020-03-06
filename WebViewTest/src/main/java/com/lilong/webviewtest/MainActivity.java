package com.lilong.webviewtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * 显示net::err_cache_miss，是因为没加INTERNET权限
 *
 * {@link WebView}要关注的几个优化点：
 * (1) 速度：
 *     打开缓存开关
 *     耗时的资源本地化
 *     在首次使用前就创建WebView对象，在使用时就可以节省创建时间
 *     网络资源尽可能使用相同域名，并在首次使用WebView前就访问这个域名，以建立DNS缓存，节省DNS查找时间
 *     前端的工作：网页内的css标签尽量靠前，js标签尽量靠后，设置缓存，传输gzip压缩后的数据，避免重定向
 * (2) 内存：
 *     代码中创建单例的WebView对象+复用
 *     创建时使用Application作为context
 *     结束时按照完善的步骤销毁
 *     放在单独的进程中
 * (3) 安全：
 *     尽可能关闭javascript，密码保存，文件访问的权限
 */
public class MainActivity extends Activity {

    public static final String TAG = "WTest";
    private static final String URL = "https://www.baidu.com";

    private SwipeRefreshLayout swipeRefreshLayout;
    private WebView webView;
    private MenuItem menuItemTestJS;

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

            /**
             * WebView加载某个资源时，可以通过这个方法的返回值，由客户端而非网站来返回这个资源
             * 默认是null，由网站来返回资源
             * */
            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }

            /**
             * 如果是https链接，而且证书验证不通过，就会调到这里
             * 默认是按照{@link SslErrorHandler#cancel()}来处理，即显示空白页，而不像pc浏览器那样让用户选择是否继续
             * */
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.i(TAG, "WebViewClient : onReceivedSslError of " + error);
                handler.proceed();
            }

        });
        webView.setWebChromeClient(new WebChromeClient(){

            /** 接收到网页标题*/
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.i(TAG, "WebChromeClient : onReceivedTitle of " + title);
                getActionBar().setTitle("网页标题 = " + title);
            }

            /** 接收到网页favicon*/
            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                Log.i(TAG, "WebChromeClient : onReceivedIcon of " + icon);
            }

            /** 网页要求打开手机的文件选择器，通过{@link ValueCallback#onReceiveValue(Object)}来返回用户的选择给WebView*/
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
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

        /**
         * 是否允许在https的网页中访问http链接
         * */
        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }

    /**
     * 后退键触发WebView的页面回退
     * */
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 暂停一切可以安全暂停的webview活动，比如动画和定位
     * */
    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    /**
     * 恢复webview活动
     * */
    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    /**
     * WebView持有Activity的引用，为了防止内存泄露，需要按下面步骤销毁WebView
     * */
    @Override
    protected void onDestroy() {
        if(webView != null){
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            swipeRefreshLayout.removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action, menu);
        menuItemTestJS = menu.findItem(R.id.menuItemTestJs);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item == menuItemTestJS){
            Intent intent = new Intent(this, TestJSActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
