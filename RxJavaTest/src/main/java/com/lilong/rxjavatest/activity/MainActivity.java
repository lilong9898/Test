package com.lilong.rxjavatest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.lilong.rxjavatest.R;
import com.lilong.rxjavatest.util.Util;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends Activity {

    public static final String TAG = "RxTag";

    // 观察者
    class CustomObserver implements Observer<String> {

        private Disposable disposable;

        public Disposable getDisposable(){
            return disposable;
        }

        @Override
        public void onSubscribe(Disposable d) {
            Log.i(TAG, "observer onSubscribe " + d);
            disposable = d;
        }

        @Override
        public void onNext(String s) {
            Log.i(TAG, "observer onNext " + s);
            // observer收到一个事件后，调disposable.dispose方法中断接收（但observable还会发送事件）
            if("event_1".equals(s)){
                disposable.dispose();
                Log.i(TAG, "observer disposes");
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.i(TAG, "observer onError " + Log.getStackTraceString(e));
        }

        @Override
        public void onComplete() {
            Log.i(TAG, "observer onComplete");
        }
    };

    // 另一种形式的观察者
    class CustomSubscriber implements Subscriber<String> {

        @Override
        public void onSubscribe(Subscription s) {
            Log.i(TAG, "subscriber onSubscribe " + s);
        }

        @Override
        public void onNext(String s) {
            Log.i(TAG, "subscriber onNext " + s);
        }

        @Override
        public void onError(Throwable t) {
            Log.i(TAG, "subscriber onError " + Log.getStackTraceString(t));
        }

        @Override
        public void onComplete() {
            Log.i(TAG, "subscriber onComplete");
        }
    };

    private CustomObserver customObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customObserver = new CustomObserver();
        Util.getObservableCustomClass().subscribe(customObserver);
        // 重复调用observable的subscribe方法，会重新执行其subscribe方法的内容（这里是发送事件）
        Util.getObservableCustomClass().subscribe(customObserver);
    }


}
