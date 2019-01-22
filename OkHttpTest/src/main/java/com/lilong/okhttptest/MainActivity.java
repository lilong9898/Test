package com.lilong.okhttptest;

import android.app.Activity;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity {

    private static final String TAG = "OkTest";

    private Button mBtnRequestSync;
    private Button mBtnRequestAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnRequestSync = findViewById(R.id.btnRequestSync);
        mBtnRequestSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        requestSync();
                    }
                }.start();
            }
        });
        mBtnRequestAsync = findViewById(R.id.btnRequestAsync);
        mBtnRequestAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAsync();
            }
        });
    }

    /**
     * 同步请求，所以要放在工作线程里，否则报{@link NetworkOnMainThreadException}
     * 默认是GET请求
     */
    private void requestSync() {
        OkHttpClient client = new OkHttpClient();
        String URL = "http://japi.juhe.cn/qqevaluate/qq?key=96efc220a4196fafdfade0c9d1e897ac&qq=295424589";
        Request request = new Request.Builder().url(URL).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.i(TAG, response.body().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步请求
     */
    private void requestAsync() {
        OkHttpClient client = new OkHttpClient();
        String URL = "http://japi.juhe.cn/qqevaluate/qq?key=96efc220a4196fafdfade0c9d1e897ac&qq=295424589";
        Request request = new Request.Builder().url(URL).build();
        Call call = client.newCall(request);
        Log.i(TAG, "newCall call = " + call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 这个call就是调enqueue方法的call
                Log.i(TAG, "onResponse call = " + call);
                // 打印headers
                Headers headers = response.headers();
                for(int i = 0; i < headers.size(); i++){
                    Log.i(TAG, "response header " + headers.name(i) + " = " + headers.value(i));
                }
                // body的字节数据解析为字符串后打印
                Log.i(TAG, "response body string = " + response.body().string());
            }
        });
    }

    /**
     * http的缓存分为三级：
     * 第一级不存在或者表示缓存无效之后，启动第二级
     * 第二级不存在或者表示缓存无效之后，启动第三级
     * 第三级是从服务端强制重新下载所有数据
     *
     * 第一级：http缓存策略：
     * http的请求头和响应头中都有pragma或者cache-control字段，pragma是旧的字段，逐步废弃，被cache-control替代
     * 请求头中的cache-control表示客户端希望采用什么样的缓存策略
     * 响应头中的cache-control表示服务端告诉客户端最终的缓存策略是什么
     *
     * 第二级：http缓存校验：
     * 客户端向服务端发送缓存校验请求，服务端响应校验结果
     * 通过last-modified和etag两个字段实现
     * (1)首次http请求后，服务端发来的响应头里有last modified=资源最后一次修改的时间和etag=资源md5两个字段
     * (2)第二次和以后的请求，客户端的请求头里带上if-modified-since=当前时间和if-none-match＝资源md5两个字段
     * (3)服务端判断客户端的if-modified-since时间之后，资源是否有改变，以及资源md5是否有变化
     *    (3.1) 如果都不变，返回304(意思是无变化)
     *    (3.2) 如果有变化，返回200和新的资源，新的响应头（包括新的last-modified和etag）
     *
     * 第三级：重新下载所有数据
     *
     * */
    private void testRequestCache(){

    }

}
