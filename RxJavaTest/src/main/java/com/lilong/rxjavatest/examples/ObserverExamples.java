package com.lilong.rxjavatest.examples;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.lilong.rxjavatest.activity.MainActivity.TAG;

public class ObserverExamples {

    //------------------------------------------------------------
    private static Consumer<String> consumer;

    /**
     * 返回一个Consumer接口的实例作为观察者
     */
    public static Consumer<String> getObserverConsumer() {
        if (consumer == null) {
            consumer = new Consumer<String>() {
                @Override
                public void accept(String s) throws Exception {
                    Log.i(TAG, "observer accept : " + s);
                }
            };
        }
        return consumer;
    }

    //------------------------------------------------------------
    private static Observer<String> observerFullDefined;

    static class CustomObserver implements Observer<String> {

        private Disposable disposable;

        public Disposable getDisposable() {
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
            if (ObservableExamples.EVENT_1.equals(s)) {
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
    }

    /**
     * 返回Observer接口的实例作为观察者
     */
    public static Observer<String> getObserverFullDefined() {
        if (observerFullDefined == null) {
            observerFullDefined = new CustomObserver();
        }
        return observerFullDefined;
    }

    //------------------------------------------------------------
    private static Subscriber<String> subscriberFullDefined;

    // 另一种形式的观察者
    static class CustomSubscriber implements Subscriber<String> {

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
    }

    public static Subscriber<String> getSubscriberFullDefined() {
        if (subscriberFullDefined == null) {
            subscriberFullDefined = new CustomSubscriber();
        }
        return subscriberFullDefined;
    }


}
