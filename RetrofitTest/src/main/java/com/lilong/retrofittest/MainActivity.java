package com.lilong.retrofittest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lilong.retrofittest.json.JSONDataRequest;
import com.lilong.retrofittest.json.JSONEntity;
import com.lilong.retrofittest.string.StringDataRequest;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Retrofit用到了很多设计模式，其中三个关键步骤：
 * (1) 接口->代理：用户定义的网络访问接口通过{@link Retrofit#create(Class)}被转换成一个代理（代理模式）
 * (2) 调用代理的方法->return Call：这个代理的网络访问方法的调用，返回值是其内部生成的{@link Call}的实现类，一般是{@link ExecutorCallAdapterFactory.ExecutorCallbackCall}（接口适配器）
 * (3) 执行Call：调用{@link Call#execute()}进行同步网路访问，或调用{@link Call#enqueue(Callback)}进行异步网络访问
 */
public class MainActivity extends Activity {

    private static final String TAG = "RetroTest";

    private Button btnRequestStringData;
    private Button btnRequestJsonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRequestStringData = findViewById(R.id.btnRequestStringData);
        btnRequestStringData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestStringData();
            }
        });
        btnRequestJsonData = findViewById(R.id.btnRequestJsonData);
        btnRequestJsonData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestJSONData();
            }
        });
    }

    /**
     * 网络访问返回的数据以字符串形式呈现
     */
    private void requestStringData() {
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                //指定baseUrl，后缀一定要带着“/”
                .baseUrl("http://www.baidu.com/")
                //设置内容格式,这种对应的数据返回值是String类型(html报文原文)
                .addConverterFactory(ScalarsConverterFactory.create())
                //定义client类型
                .client(new OkHttpClient())
                // 创建
                .build();
        // java接口转换为http request
        // 内部用到了动态代理，这个方法返回的实际上是代理类的实例
        // 同时它内部会生成委托类
        StringDataRequest request = retrofit.create(StringDataRequest.class);
        // baiduRequest叫Retrofit$1，是代理类的名字
        Log.i(TAG, "request = " + request);
        // 调用代理类的方法，得到Call接口的实现实例
        // ExecutorCallAdapterFactory$ExecutorCallbackCall
        // 这个request方法会调到委托类的request
        // 而委托类的request(在invocationHandler的invoke方法中等效触发)实际上什么都没干
        // 只是返回了CallAdapter#adapt方法返回Call接口的对象
        Call<String> call = request.request();
        Log.i(TAG, "call = " + call);
        // 执行请求（enqueue方法会让请求异步执行）
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // onResponse运行在主线程里
                Log.i(TAG, "onResponse in thread = " + Thread.currentThread().getName());
                Log.i(TAG, "onResponse string = " + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "onFailure in thread = " + Thread.currentThread().getName());
                Log.i(TAG, "throwable = " + t);
            }
        });
    }

    private void requestJSONData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://japi.juhe.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        JSONDataRequest request = retrofit.create(JSONDataRequest.class);
        Call<JSONEntity> call = request.request("96efc220a4196fafdfade0c9d1e897ac", "11111111");
        call.enqueue(new Callback<JSONEntity>() {
            @Override
            public void onResponse(Call<JSONEntity> call, Response<JSONEntity> response) {
                Log.i(TAG, "onResponse");
                JSONEntity entity = response.body();
                Log.i(TAG, entity + "");
            }

            @Override
            public void onFailure(Call<JSONEntity> call, Throwable t) {
                Log.i(TAG, "onFailure");
            }
        });
    }
}
