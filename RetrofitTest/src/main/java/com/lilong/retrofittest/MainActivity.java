package com.lilong.retrofittest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends Activity {

    private static final String TAG = "RetroTest";

    private Button btnRequestBaidu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRequestBaidu = findViewById(R.id.btnRequestBaidu);
        btnRequestBaidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                BaiduNetRequest baiduRequest = retrofit.create(BaiduNetRequest.class);
                // baiduRequest叫Retrofit$1，是代理类的名字
                Log.i(TAG, "baiduRequest = " + baiduRequest);
                // 调用代理类的方法，得到Call接口的实现实例，实际就是Retrofit.Builder.client里指定的client，这里是okhttpCall
                // ExecutorCallAdapterFactory$ExecutorCallbackCall
                Call<String> baiduCall = baiduRequest.request("http://www.baidu.com");
                Log.i(TAG, "baiduCall = " + baiduCall);
                // 执行请求（enqueue方法会让请求异步执行）
                baiduCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.i(TAG, "response string = " + response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.i(TAG, "throwable = " + t);
                    }
                });
            }
        });
    }
}
