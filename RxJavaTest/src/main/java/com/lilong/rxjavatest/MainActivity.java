package com.lilong.rxjavatest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends Activity {

    private static final String TAG = "RxTag";

    // 观察者
    class CustomObserver implements Observer<String> {

        private Disposable disposable;

        public Disposable getDisposable(){
            return disposable;
        }

        @Override
        public void onSubscribe(Disposable d) {
            Log.i(TAG, "onSubscribe " + d);
            disposable = d;
        }

        @Override
        public void onNext(String s) {
            Log.i(TAG, "onNext " + s);
            // observer收到一个事件后，调disposable.dispose方法中断接收（但observable还会发送事件）
            if("a".equals(s)){
                disposable.dispose();
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.i(TAG, "onError " + Log.getStackTraceString(e));
        }

        @Override
        public void onComplete() {
            Log.i(TAG, "onComplete");
        }
    };

    // 另一种形式的观察者
    class CustomSubscriber implements Subscriber<String> {

        @Override
        public void onSubscribe(Subscription s) {
            Log.i(TAG, "onSubscribe " + s);
        }

        @Override
        public void onNext(String s) {
            Log.i(TAG, "onNext " + s);
        }

        @Override
        public void onError(Throwable t) {
            Log.i(TAG, "onError " + Log.getStackTraceString(t));
        }

        @Override
        public void onComplete() {
            Log.i(TAG, "onComplete");
        }
    };

    // 被观察者的数据源
    private final List<String> sources = Arrays.asList(new String[]{
            "a",
            "b",
    });

    // 被观察者
    private Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
        @Override
        public void subscribe(ObservableEmitter<String> emitter) throws Exception {
            try{
                for(String str : sources){
                    emitter.onNext(str);
                    Log.i(TAG, "observable emits : " + str);
                }
                // 调用完emitter.onComplete后，再调emitter.onNext也无反应了，不会被接收了
                // 调用完emitter.onError后也一样
                emitter.onComplete();
            }catch (Exception e){
                emitter.onError(e);
            }
        }
    });

    private CustomObserver customObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customObserver = new CustomObserver();
//        observable.subscribe(customObserver);
        observable.subscribe();
    }
}
