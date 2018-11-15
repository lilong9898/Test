package com.lilong.rxjavatest.multithread;

import android.util.Log;

import com.lilong.rxjavatest.observables.observable.ObservableExamples;
import com.lilong.rxjavatest.observers.ObserverExamples;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.lilong.rxjavatest.activity.MainActivity.TAG;

public class MultiThreadTest {

    /**
     * 测试被观察者和观察者在不同线程的情况
     * 这里被观察者在工作线程，观察者在主线程
     */
    public static void testObservableAndObserverInDifferentThread() {
        ObservableExamples.getObservableFromObservableOnSubscribe()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ObserverExamples.getShowThreadObserver());
    }

    /**
     * 一个更加具体的例子，被观察者在发出事件前有耗时操作，所以在io线程上subscribe，观察者在主线程上observe
     * android中大量存在着类似这种，工作线程结束后发送结果给主线程的情况
     */
    public static void testObservableOnIoThreadAndObserverOnMainThread() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                Log.i(TAG, "observable start working...");
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
                Log.i(TAG, "observable done working, send result");
                emitter.onNext("haha");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "observer onNext : receives result = " + s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.i(TAG, "observer onComplete");
            }
        });
    }
}
