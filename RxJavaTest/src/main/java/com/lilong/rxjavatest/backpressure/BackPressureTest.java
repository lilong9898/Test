package com.lilong.rxjavatest.backpressure;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.lilong.rxjavatest.activity.MainActivity.TAG;

public class BackPressureTest {

    /**
     * Observable不停发送消息，Observer与它在同一线程，不停接收消息
     * 因为Observable和Observer在相同线程，所以消息的发送和接收速度一致，不会出现消息堆积
     * 故内存是基本稳定的，在平均线上下规律的波动
     */
    public static void testObservableWithMaxFlowAndObserverInSameThread() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                int eventNumber = 0;
                while (true) {
                    eventNumber++;
                    Log.i(TAG, "observable emits : " + eventNumber);
                    emitter.onNext(eventNumber);
                }
            }
        })
                .subscribeOn(Schedulers.computation())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "observer receives : " + integer);
                    }
                });
    }

    /**
     * Observable不停发送消息，Observer与它在不同线程，不停接收消息
     * 虽然他俩不在同一线程，但发送和消耗速度还是基本一致
     * 所以内存还是基本稳定的，在平均线上下规律的波动
     */
    public static void testObservableWithMaxFlowAndObserverInDifferentThread() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                int eventNumber = 0;
                while (true) {
                    eventNumber++;
                    Log.i(TAG, "observable emits : " + eventNumber);
                    emitter.onNext(eventNumber);
                }
            }
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "observer receives : " + integer);
                    }
                });
    }

    /**
     * 与{@link #testObservableWithMaxFlowAndObserverInDifferentThread()}基础上，把observer的接收速度降低
     * 于是Observable发送的事件会在rxJava内部的缓存区堆积起来得不到处理，内存会越用越多
     * */
    public static void testObservableWithMaxFlowAndObserverInDifferentThreadAndObserverSlow() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                int eventNumber = 0;
                while (true) {
                    eventNumber++;
                    Log.i(TAG, "observable emits : " + eventNumber);
                    emitter.onNext(eventNumber);
                }
            }
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Thread.sleep(100);
                        Log.i(TAG, "observer receives : " + integer);
                    }
                });
    }
}
