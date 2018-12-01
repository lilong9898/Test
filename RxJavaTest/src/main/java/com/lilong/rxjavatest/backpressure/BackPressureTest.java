package com.lilong.rxjavatest.backpressure;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import android.util.Log;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.operators.observable.ObservableObserveOn;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
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
     * 这个所谓“缓存区”实际上是observeOn操作符生成的{@link ObservableObserveOn}内部订阅的观察者{@link ObservableObserveOn.ObserveOnObserver}的内部的一个{@link SpscLinkedArrayQueue}
     * 而{@link ObservableObserveOn.ObserveOnObserver}会调用用户指定的最终观察者的onSubscribe和onNext等方法
     */
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

    /**
     * 测试Flowable的使用
     * <p>
     * 数据源端：
     * 继承关系：
     * 支持backpressure:
     * {@link Flowable}->{@link Publisher}
     * 如同前面的
     * 不支持backpressure:
     * {@link Observable}->{@link ObservableSource}
     * <p>
     * 观察者端：
     * {@link Subscriber}对应于{@link Observer}
     * <p>
     * {@link Flowable}内部也有队列来实现backpressure功能所要求的事件缓存
     * 但跟observeOn操作符生成的{@link ObservableObserveOn}内的缓存不同
     * 前者是{@link FlowableEmitter}内的{@link SimplePlainQueue}
     * 后者是{@link ObservableObserveOn.ObserveOnObserver}内的{@link SpscLinkedArrayQueue}
     */
    public static void testFlowableOnSameThread() {

        // 创建Flowable
        Flowable<Integer> upstream = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.i(TAG, "emit 1");
                emitter.onNext(1);
                Log.i(TAG, "emit 2");
                emitter.onNext(2);
                Log.i(TAG, "emit 3");
                emitter.onNext(3);
                Log.i(TAG, "emit 4");
                emitter.onNext(4);
                Log.i(TAG, "emit onComplete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR);

        // 创建Subscriber
        Subscriber<Integer> downstream = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.i(TAG, "Subscriber : onSubscribe");
                // 请求upstream flowable发送多少个事件
                // 如果不调用此方法，意味着不向上游请求任何事件，上游flowable发出的事件不能及时处理
                // [而且上下游工作在同个线程中（意味者不会有observeOn操作符创建的缓存）]时
                // 会抛出MissingBackpressureException
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                Log.i(TAG, "Subscriber : onNext = " + integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.i(TAG, "Subscriber : onError = " + t);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "Subscriber : onComplete");
            }
        };

        upstream.subscribe(downstream);
    }

    public static void testFlowableOnDifferentThread() {

        // 创建Flowable
        Flowable<Integer> upstream = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.i(TAG, "emit 1");
                emitter.onNext(1);
                Log.i(TAG, "emit 2");
                emitter.onNext(2);
                Log.i(TAG, "emit 3");
                emitter.onNext(3);
                Log.i(TAG, "emit 4");
                emitter.onNext(4);
                Log.i(TAG, "emit onComplete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR);

        // 创建Subscriber
        Subscriber<Integer> downstream = new Subscriber<Integer>() {

            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription s) {
                Log.i(TAG, "Subscriber : onSubscribe");
                subscription = s;
                // 请求upstream flowable发送多少个事件
                // 如果不调用此方法，意味着不向上游请求任何事件，上游flowable发出的事件不能及时处理
                // [而且上下游工作在同个线程中（意味者不会有observeOn操作符创建的缓存）]时
                // 会抛出MissingBackpressureException
                // 此例子上下游不工作在同一个线程，所以即使不调subscription.request，也不会抛出MissingBackpressureException
                // 事件会存放在Flowable(实际上是FlowableOnCreate->Flowable)内部的缓存队列里
//                s.request(Long.MAX_VALUE);
                // 下面演示subscription.request方法
                // request方法要求取几个事件，Flowable就会发几个事件
                try {
                    subscription.request(5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNext(Integer integer) {
                Log.i(TAG, "Subscriber : onNext = " + integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.i(TAG, "Subscriber : onError = " + t);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "Subscriber : onComplete");
            }
        };

        upstream
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(downstream);
    }
}
