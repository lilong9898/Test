package com.lilong.okhttptest.interceptor;

import android.util.Log;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.lilong.okhttptest.MainActivity.TAG;

/**
 * {@link Interceptor}以{@link Interceptor.Chain#proceed(Request)}为分界，
 * 之前拦截的是请求，之后拦截的是响应
 * */
public class ModifyRequestInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) {
        Log.i(TAG, "ModifyRequestInterceptor : intercept starts");
        Request request = chain.request();
        Log.i(TAG, "ModifyRequestInterceptor : modifies request header");
        Request modifiedRequest = request.newBuilder().header("additional_request_header", "value").build();
        Response modifiedResponse = null;
        try {
            Response response = chain.proceed(modifiedRequest);
            Log.i(TAG, "ModifyRequestInterceptor : modifies response header");
            modifiedResponse = response.newBuilder().header("additional_response_header", "value").build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "ModifyRequestInterceptor : intercept finishes");
        return modifiedResponse;
    }

}
