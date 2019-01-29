package com.lilong.okhttptest.interceptor;

import android.util.Log;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.lilong.okhttptest.MainActivity.TAG;

/**
 * {@link Request#url()}返回的是用户最开始请求的url
 * {@link Response#request()}得到的{@link Request}中的{@link Request#url()}是最终访问的url
 * 如果有重定向，则两个url不同
 * 如果无重定向，则两个url相同
 *
 * {@link OkHttpClient.Builder#addInterceptor(Interceptor)}对应的Chain是{@link RealCall.ApplicationInterceptorChain}
 * {@link OkHttpClient.Builder#addNetworkInterceptor(Interceptor)}对应的Chain是{@link HttpEngine.NetworkInterceptorChain}
 * */
public class LoggingInterceptor implements Interceptor {

    private String name;

    public LoggingInterceptor(String name){
        this.name = name;
    }

    @Override
    public Response intercept(Chain chain) {
        Log.i(TAG, "LoggingInterceptor " + name + ": intercept start, current thread = " + Thread.currentThread().getName());

        Request request = chain.request();
        Response response = null;
        try {
            Log.i(TAG, "LoggingInterceptor " + name + ": proceed");
            response = chain.proceed(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, "LoggingInterceptor " + name + ": intercept finishes");
        return response;
    }

}
