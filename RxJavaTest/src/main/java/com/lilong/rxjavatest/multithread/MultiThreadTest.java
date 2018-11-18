package com.lilong.rxjavatest.multithread;

import com.lilong.rxjavatest.observables.observable.ObservableExamples;
import com.lilong.rxjavatest.observers.ObserverExamples;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.lilong.rxjavatest.activity.MainActivity.TAG;

public class MultiThreadTest {

    /**
     * 测试被观察者和观察者在不同线程的情况
     * 这里被观察者在工作线程，观察者在主线程
     * (1)在RxJava中, 已经内置了很多线程选项供我们选择, 例如有
     * Schedulers.io() 代表io操作的线程, 通常用于网络,读写文件等io密集型的操作
     * Schedulers.computation() 代表CPU计算密集型的操作, 例如需要大量计算的操作
     * Schedulers.newThread() 代表一个常规的新线程
     * 这些线程里都是没有looper的!所以里面不能用new Handler()
     * <p>
     * (2){@link Observable#observeOn(Scheduler)}方法的作用，不只是改变观察者所在的线程
     * 实际上是[改变调用此方法后后续流程所有操作符和被观察者所在的线程]
     * 所以此方法的调用位置会影响程序执行情况
     * (3){@link Observable#subscribeOn(Scheduler)}方法的作用，是影响所有subscribe方法所执行的线程
     * 当然这个还要按第(2)条考虑{@link Observable#observeOn(Scheduler)}方法的影响
     * 所以此方法的调用位置不影响程序执行情况
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
