package com.lilong.rxjavatest.observables.observable;

import com.lilong.rxjavatest.observers.ObserverExamples;

import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.lilong.rxjavatest.activity.MainActivity.TAG;

public class ObservableTest {

    /**
     * 测试将{@link ObservableOnSubscribe}接口实例作为数据源的被观察者
     */
    public static void testObservableFromObservableOnSubscribe() {
        ObservableExamples.getObservableFromObservableOnSubscribe().subscribe(ObserverExamples.getObserverFromObserver());
    }

    /**
     * 测试{@link Observable#just(Object)}生成的被观察者
     */
    public static void testObservableJust() {
        ObservableExamples.getObservableJust().subscribe(ObserverExamples.getObserverFromObserver());
    }

    /**
     * 测试{@link Observable#fromIterable(Iterable)}生成的被观察者
     */
    public static void testObservableFromIterable() {
        ObservableExamples.getObservableFromIterable().subscribe(ObserverExamples.getObserverFromObserver());
    }

    /**
     * 测试{@link Observable#fromArray(Object[])}生成的被观察者
     */
    public static void testObservableFromArray() {
        ObservableExamples.getObservableFromArray().subscribe(ObserverExamples.getObserverFromObserver());
    }

    /**
     * 测试{@link Observable#fromCallable(Callable)}生成的被观察者
     */
    public static void testObservableFromCallable() {
        ObservableExamples.getObservableFromCallable().subscribe(ObserverExamples.getObserverFromObserver());
    }

    /**
     * 测试{@link Observable#fromFuture(Future)}生成的被观察者
     */
    public static void testObservableFromFuture() {
        ObservableExamples.getObservableFromFuture().subscribe(ObserverExamples.getObserverFromObserver());
    }

    /**
     * 测试{@link Observable#fromFuture(Future)}生成的被观察者
     * 使用FutureTask
     */
    public static void testObservableFromFutureTask() {
        Executors.newScheduledThreadPool(5).schedule(ObservableExamples.getFutureTask(), 1500, TimeUnit.MILLISECONDS);
        ObservableExamples.getObservableFromFutureTask().subscribe(ObserverExamples.getObserverFromObserver());
    }

    /**
     * 测试{@link Observable#interval(long, TimeUnit)}生成的被观察者
     * */
    public static void testObservableInterval(){
        ObservableExamples.getObservableInterval().subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "long observer onSubscribe");
            }

            @Override
            public void onNext(Long aLong) {
                Log.i(TAG, "long observer onNext : " + aLong);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "long observer onError");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "long observer onComplete");
            }
        });

    }
}
