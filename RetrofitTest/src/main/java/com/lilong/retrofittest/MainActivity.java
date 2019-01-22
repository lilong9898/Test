package com.lilong.retrofittest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapter;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.lilong.retrofittest.bytes.ByteDataRequest;
import com.lilong.retrofittest.json.JSONDataRequest;
import com.lilong.retrofittest.json.JSONEntity;
import com.lilong.retrofittest.rxjava2.ObservableDataRequest;
import com.lilong.retrofittest.string.StringDataRequest;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.DefaultCallAdapterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Retrofit用到了很多设计模式，其中三个关键步骤：
 * (1) 接口->代理：用户定义的网络访问接口通过{@link Retrofit#create(Class)}被转换成一个代理（代理模式）
 *
 * (2) 调用代理方法->return callAdapter.adapt(代理)：这个代理方法的调用，返回值取决于使用的{@link CallAdapter}
 *    (2.1)如果使用默认的callAdapter，也就是{@link DefaultCallAdapterFactory}，则返回的是{@link ExecutorCallAdapterFactory.ExecutorCallbackCall}
 *    (2.2)如果使用非默认的callAdapter，比如{@link RxJava2CallAdapter}，则返回的是{@link Observable}
 *         其它的callAdapter可以将Call转换成其它类型T
 *
 * (3) 执行：
 *    (3.1)如果使用默认的callAdapter，则类型是{@link Call}，后面调用{@link Call#execute()}进行同步网路访问，或调用{@link Call#enqueue(Callback)}进行异步网络访问
 *    (3.2)如果使用非默认的callAdapter，则按类型T的具体后续操作进行，比如使用了{@link RxJava2CallAdapter}，则T是{@link Observable}，后面访问网络要通过{@link Observable#subscribe(Observer)}进行
 *
 * {@link CallAdapter}的作用是将输入的{@link Call}转换成指定的其它类型T并输出
 * {@link DefaultCallAdapterFactory}中生成的{@link CallAdapter}则是原样返回输入的call
 *
 */
public class MainActivity extends Activity {

    private static final String TAG = "RetroTest";

    private Button btnRequestResponseBody;
    private Button btnRequestStringData;
    private Button btnRequestJsonData;
    private Button btnRequestJsonDataWithRxJava;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRequestResponseBody = findViewById(R.id.btnRequestResponseBody);
        btnRequestResponseBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestResponseBody();
            }
        });

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
        btnRequestJsonDataWithRxJava = findViewById(R.id.btnRequestJsonDataWithRxJava);
        btnRequestJsonDataWithRxJava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestJSONDataWithRxJava();
            }
        });
    }

    /**
     * 网络访问返回的数据以ResponseBody形式呈现
     * {@link Response}的泛型就是{@link Response#body()}方法返回值的类型，就是用户想要的解析后的格式
     */
    private void requestResponseBody() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.baidu.com/")
                // 不用addConverterFactory
                .client(new OkHttpClient())
                .build();

        ByteDataRequest request = retrofit.create(ByteDataRequest.class);
        Call<ResponseBody> call = request.request();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, "onResponse");
                ResponseBody body = response.body();
                try {
                    // body只能消费一次，也就是其中的内容只能读取一次
                    // 所以body的bytes方法和string方法，后被调用者读到的内容是空的
                    // 以字节形式呈现ResponseBody
                    byte[] bytes = body.bytes();
                    Log.i(TAG, "byte array length = " + bytes.length);

                    // 以字符串形式呈现ResponseBody，字符串跟用了ScalarsConverterFactory之后解析到的字符串一样
                    String str = body.string();
                    Log.i(TAG, "str length = " + str.length());
                    Log.i(TAG, "str = " + str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "onFailure");
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
                //定义client类型，可不写，默认用OkHttpClient
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

    private void requestJSONDataWithRxJava() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://japi.juhe.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                // 注意这里用RxJava2CallAdapterFactory
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient())
                .build();
        ObservableDataRequest request = retrofit.create(ObservableDataRequest.class);
        Observable<JSONEntity> observable = request.request("96efc220a4196fafdfade0c9d1e897ac", "11111111");

        // 跟rxjava2联用后，触发网络请求不再通过Call:enqueue，而是通过Observable:subscribe，线程调度也由rxjava2负责
        observable
                // 因为请求过程使用的是RxJava2的流程，其默认是在主线程执行发送-接收代码的
                // 这样会报NetworkOnMainThreadException
                // 所以必须调用subscribeOn非主线程
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "onSubscribe");
            }

            @Override
            public void onNext(JSONEntity entity) {
                Log.i(TAG, "onNext = " + entity);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError = " + e);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete");
            }
        });
    }
}
