package com.lilong.okhttptest;

import android.util.Log;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LoggingInterceptor implements Interceptor {

    private static final String TAG = "InterceptorTest";

    @Override
    public Response intercept(Chain chain) {
        Request request = chain.request();
        Log.i(TAG, "url = " + request.url());
        Log.i(TAG, "request headers = ");
        for (int i = 0; i < request.headers().size(); i++) {
            Log.i(TAG, request.headers().name(i) + " : " + request.headers().value(i));
        }
        Response response = null;
        try {
            chain.proceed(request);
        } catch (Exception e) {

        }
        return response;
    }

}
