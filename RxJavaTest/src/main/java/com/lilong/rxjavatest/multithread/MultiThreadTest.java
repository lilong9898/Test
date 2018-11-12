package com.lilong.rxjavatest.multithread;

import com.lilong.rxjavatest.observables.observable.ObservableExamples;
import com.lilong.rxjavatest.observers.ObserverExamples;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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


}
