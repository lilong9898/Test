package com.lilong.webviewtest;

import android.app.Application;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import static com.lilong.webviewtest.MainActivity.TAG;

/**
 *  注入到webview中的java对象，让js可以调这个java对象的public方法
 *  */
public class JavaObject {

    @JavascriptInterface
    public void javaMethod(String arg){
        Log.i(TAG, "java方法被js调用，参数=" + arg + ", thread = " + Thread.currentThread().getName());
        Application context = MainApplication.getInstance();
        Toast.makeText(context, "java被js调用", Toast.LENGTH_SHORT).show();
    }
}
