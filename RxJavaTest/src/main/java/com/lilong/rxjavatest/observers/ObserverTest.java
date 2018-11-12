package com.lilong.rxjavatest.observers;

import android.os.Handler;

import com.lilong.rxjavatest.observables.observable.ObservableExamples;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class ObserverTest {

    /**
     * 没有观察者，只有被观察者开始发送事件，并最终发送onComplete
     */
    public static void testNoObserver() {
        ObservableExamples.getObservableFromObservableOnSubscribe().subscribe();
    }

    /**
     * 观察者是Consumer接口的实例，这种情况subscribe方法会返回disposable
     */
    public static void testObserverFromConsumer() {
        // 对比：观察者是Observer接口的实例时，会在其onSubscribe的参数里提供disposable，所以subscribe方法不需返回disposable，返回的是void
        final Disposable disposable = ObservableExamples.getObservableFromObservableOnSubscribe().subscribe(ObserverExamples.getObserverConsumer());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                disposable.dispose();
            }
            // 延时500ms刚好是在Observable第一个event发出，而第二个event还没发出的时候dispose的
            // 所以第二个event的accept不会触发
        }, 500);
    }

    /**
     * 在其它线程里订阅的情况：所有方法都运行在其它线程里
     */
    public static void testWholeProcedureInAnotherThread() {
        new Thread() {
            @Override
            public void run() {
                ObservableExamples.getShowThreadObservable().subscribe(ObserverExamples.getShowThreadObserver());
            }
        }.start();
    }

    /**
     * 用subscribeWith方法订阅，与subscribe方法一样，只是多了个返回值就是observer本身
     */
    public static void testSubscribeWithMethod() {
        ObservableExamples.getObservableFromObservableOnSubscribe().subscribeWith(ObserverExamples.getObserverFromObserver());
    }

    /**
     * 用subscribeWith方法配合DisposableObserver，可直接用返回的DisposableObserver的dispose方法
     */
    public static void testDisposableObserver() {
        final DisposableObserver<String> observer = ObservableExamples.getObservableFromObservableOnSubscribe().subscribeWith(ObserverExamples.getDisposableObserver());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 延时500ms刚好是在Observable第一个event发出，而第二个event还没发出的时候dispose的
                // 所以第二个event的onNext不会触发
                observer.dispose();
            }
        }, 500);
    }

    /**
     * 观察者是Observer接口的实例
     * */
    public static void testObserverFromObserver(){
        ObservableExamples.getObservableFromObservableOnSubscribe().subscribe(ObserverExamples.getObserverFromObserver());
    }
}
