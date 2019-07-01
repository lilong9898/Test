package com.lilong.webviewtest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import static com.lilong.webviewtest.MainActivity.TAG;

/**
 * Java和JS互调
 *
 * Java调JS的方法：
 * JS的方法可有参数和返回值
 *     (1) {@link WebView#loadUrl(String)}
 *         如果js方法有返回值，则会将返回值当作html的内容，加载这个html，相当于加载了新页面
 *         如果js方法无返回值，则没影响
 *         不支持返回值给java侧
 *     (2) {@link WebView#evaluateJavascript(String, ValueCallback)}
 *         不管js方法有无返回值，都不会加载新页面
 *         支持返回值给java侧
 *         调用和接收返回值都在且只能在主线程上
 *
 * JS调Java的方法：
 * 　　(1) {@link WebView#addJavascriptInterface(Object, String)}
 *         将一个java对象以某个名字注入webview中，js就可通过这个名字调这个java对象[有{@link JavascriptInterface}注解的]的方法，[在javaBridge线程上]
 *         这保证了js只能调注解范围以内的java方法
 *    (2) {@link WebViewClient#shouldOverrideUrlLoading(WebView, WebResourceRequest)}检测到页面重定向，或者点击链接跳到了某个特定的url
 *        此时触发java的动作
 * */
public class TestJSActivity extends Activity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private WebView webView;
    private Button btnJavaCallJSViaLoadUrl;
    private Button btnJavaCallJSViaEvaluateJavascript;

    private static final String URL = "file:///android_asset/js_test.html";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_js);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavaObject(), "javaObject");
        btnJavaCallJSViaLoadUrl = findViewById(R.id.btnJavaCallJsViaLoadUrl);
        btnJavaCallJSViaEvaluateJavascript = findViewById(R.id.btnJavaCallJsViaEvaluateJavascript);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl(URL);
            }
        });
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.i(TAG, "WebViewClient : shouldOverrideUrlLoading of " + request.getUrl());
                if(request.getUrl().toString().equals("https://www.baidu.com/")){
                    Log.i(TAG, "通过检测到重定向或点击链接到某个特殊url来触发java代码, thread = " + Thread.currentThread().getName());
                    Toast.makeText(TestJSActivity.this, "java被js调用", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i(TAG, "WebViewClient : onPageStarted of " + url);
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
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getActionBar().setTitle("网页标题 = " + title);
            }
        });
        btnJavaCallJSViaLoadUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("javascript:JSMethod(\"btnJavaCallJSViaLoadUrl\")");
            }
        });
        btnJavaCallJSViaEvaluateJavascript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.evaluateJavascript("javascript:JSMethod(\"btnJavaCallJSViaEvaluateJavascript\")", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.i(TAG, "evaluateJavascript : onReceivedValue = " + value);
                    }
                });
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

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

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
}
