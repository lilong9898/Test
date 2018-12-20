package com.lilong.rxjavatest.backpressure;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Emitter;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.operators.flowable.FlowableCreate;
import io.reactivex.internal.operators.flowable.FlowableObserveOn;
import io.reactivex.internal.operators.observable.ObservableObserveOn;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.schedulers.Schedulers;

import static com.lilong.rxjavatest.activity.MainActivity.TAG;

/**
 * 不同的{@link BackpressureStrategy}对应的不同{@link Emitter}:
 *
 * {@link BackpressureStrategy#MISSING} : {@link FlowableCreate.MissingEmitter}
 * 无任何backpressure功能，不像其它策略那样考虑下游的响应式拉取（也就是{@link Subscription#request(long)}），立即向下游发送所有数据
 * 如果无observeOn操作符制造的缓存区，而下游又不能及时处理，整个过程就会卡住，必须等下游处理完当前事件，才能继续发送下个事件
 * [这是唯一一个无视响应式拉取(subscription#request)的策略]
 * 原理：MissingEmitter本身有计数器功能，调用Subscriber的onNext时只考虑事件是否发送完，不考虑/检查任何其它东西
 *
 * {@link BackpressureStrategy#ERROR} : {@link FlowableCreate.ErrorAsyncEmitter}
 * 当上游的数据无法被下游及时处理，也未通过observeOn操作符制造缓存区时，会触发Subscriber的onError，具体throwable是{@link MissingBackpressureException}
 * 原理：ErrorAsyncEmitter本身有计数器功能，{@link Subscription#request(long)}会使计数器增加，{@link Emitter#onNext(Object)}会使计数器减少
 * 当计数器减到零时，如果还有新事件通过onNext要发送，说明截止到这时，上游发出的总事件数量已经超过了下游拉取的数量，溢出了，立即触发Subscriber的onError=MissingBackpressureException
 *
 * {@link BackpressureStrategy#BUFFER} : {@link FlowableCreate.BufferAsyncEmitter}
 * BufferAsyncEmitter内部有缓存区，是SpscLinkedArrayQueue
 * 每当上游发来一个事件时，这个事件会进入缓存区，然后按照下游响应式拉取的要求，将缓存区中的数据发给下游
 * 所以如果用户定义的上下游在同一个线程里，则运行情况分三种情况：
 * (1) 下游拉取的事件数量大于上游需要发送的，按照上游emitter.onNext->下游subscriber.onNext->emitter.onNext->下游subscriber.onNext...这样的顺序，这是个同步过程，上下游速度一致
 * (2) 下游拉取的事件数量小于上游需要发送的，则(1)过程结束后，剩余的上游事件全部进入BufferAsyncEmitter的缓存区等待下游的再次拉取（此时发送过程已完成）
 * (3) 下游从未拉取事件，则上游的所有事件进入BufferAsyncEmitter的缓存区等待（此时发送过程已完成）
 *
 * [注意]这种emitter里的缓存区跟observeOn操作符产生的中间件的缓存区不同
 *
 * 前者没有调度功能，只有存储功能，也就是前者不影响上下游速度（上下游速度一致），只能缓存下游拉取范围以外的事件
 * [根本原因：emitter里的缓存区，装入数据和取出数据的操作在【相同线程】]
 *
 * 后者不仅能缓存上游发来的事件，向下游发送的速度跟上游发来事件的速度脱钩了，两者可以不同
 * [根本原因：observeOn操作符产生的缓存，装入数据和取出数据的操作在【不同线程】]
 *
 * {@link BackpressureStrategy#DROP} : {@link FlowableCreate.DropAsyncEmitter}
 * {@link BackpressureStrategy#LATEST} : {@link FlowableCreate.LatestAsyncEmitter}
 *
 * */
@SuppressWarnings("ALL")
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
     * 这个所谓“缓存区”实际上是observeOn操作符生成的{@link ObservableObserveOn}内部订阅的观察者{@link ObservableObserveOn#ObserveOnObserver}的内部的一个{@link SpscLinkedArrayQueue}
     * 而{@link ObservableObserveOn#ObserveOnObserver}会调用用户指定的最终观察者的onSubscribe和onNext等方法
     */
    public static void testObservableWithMaxFlowAndObserverInDifferentThreadAndObserverSlow() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                int eventNumber = 0;
                while (true) {
                    try{
                        Thread.sleep(50);
                    }catch (Exception e){}
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
     *
     * Observable和Flowable的共同点是，如果通过各自的observeOn方法使得数据源和观察者不在同一个线程里，则会生成包含缓存区的中间环节Observer/Subscriber，
     * 具体：
     * Observable的observeOn方法会将数据源转换成{@link ObservableObserveOn}，并给其创建一个观察者{@link ObservableOnObserver}，由它来调用用户定义的观察者
     * ObservableOnObserver内部有一个{@link SpscLinkedArrayQueue}类型的缓存区
     *
     * Flowable的observeOn方法会将上游转换成{@link FlowableObserveOn}，并给其创建一个下游{@link ObserveOnSubscriber}，由它来调用用户定义的下游
     * ObserveOnSubscriber内部有一个{@link SpscLinkedArrayQueue}类型的缓存区
     *
     */
    public static void testFlowableOnSameThreadWithStrategyError() {

        // 创建Flowable，实际类型是FlowableCreate (extends Flowable)
        Flowable<Integer> upstream = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                // emitter实际类型是FlowableCreate$ErrorAsyncEmitter
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

    public static void testFlowableOnDifferentThreadWithStrategyError() {

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
                // 上一个例子的情况：
                // 请求upstream flowable发送多少个事件
                // 如果不调用此方法，意味着不向上游请求任何事件，上游flowable发出的事件不能及时处理
                // [而且上下游工作在同个线程中（意味者不会有observeOn操作符创建的缓存）]时
                // 会抛出MissingBackpressureException

                // 这个例子的情况：
                // 此例子上下游不工作在同一个线程，所以即使不调subscription.request，也不会抛出MissingBackpressureException
                // 事件会存放在前面解释过的，observeOn操作符所产生的中间环节Subscriber的内部的缓存区里
//                s.request(Long.MAX_VALUE);
                // 下面演示subscription.request方法
                // request方法要求取几个事件，Flowable就会发几个事件
                try {
//                    subscription.request(5);
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

    public static void testFlowableOnSameThreadWithStrategyMissing() {

        // 创建Flowable，实际类型是FlowableCreate (extends Flowable)
        Flowable<Integer> upstream = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                // emitter实际类型是FlowableCreate$ErrorAsyncEmitter
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
        }, BackpressureStrategy.MISSING);

        // 创建Subscriber
        Subscriber<Integer> downstream = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.i(TAG, "Subscriber : onSubscribe");
                // 这种策略无视下游的响应式拉取，所以调不调s.request()都一样，这种策略的MissingEmitter无视这个
            }

            @Override
            public void onNext(Integer integer) {
                // 下游处理事件的卡顿会卡住上游的发送（如果是同一线程，中间没有observeOn操作符制造的缓存区的话）
                try{
                    Thread.sleep(1000);
                }catch (Exception e){

                }
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

    /**
     * 使用MISSING策略，但是上下游在不同线程
     *
     * 虽然用户定义的Flowable不考虑响应式拉取，但经过observeOn操作符，它已经被转换成FlowableObserveOn了
     * 根据前面解释的，会生成中间环节的ObserveOnSubscriber
     * 这个中间环节在调用用户定义的下游时，会考虑下游的响应式拉取
     * 所以这种情况下，用户定义的下游还是要调{@link Subscription#request(long)}才能收到事件
     * */
    public static void testFlowableOnDifferentThreadWithStrategyMissing() {

        // 创建Flowable，实际类型是FlowableCreate (extends Flowable)
        Flowable<Integer> upstream = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                // emitter实际类型是FlowableCreate$ErrorAsyncEmitter
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
        }, BackpressureStrategy.MISSING);

        // 创建Subscriber
        Subscriber<Integer> downstream = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.i(TAG, "Subscriber : onSubscribe");
                s.request(5);
            }

            @Override
            public void onNext(Integer integer) {
                // 下游处理事件的卡顿会卡住上游的发送（如果是同一线程，中间没有observeOn操作符制造的缓存区的话）
                try{
                    Thread.sleep(1000);
                }catch (Exception e){

                }
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

    /** -------为了避免observeOn操作符产生的中间件的行为对测试造成干扰，后面只测试上下游在同一个线程的情况--------------*/

    /** 使用BUFFER策略，上下游运行在同一个线程*/
    public static void testFlowableOnSameThreadWithStrategyBuffer() {

        Flowable<Integer> upstream = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                // emitter实际类型是FlowableCreate$ErrorAsyncEmitter
                Log.i(TAG, "emit 1 on thread " + Thread.currentThread().getName());
                emitter.onNext(1);
                Log.i(TAG, "emit 2 on thread " + Thread.currentThread().getName());
                emitter.onNext(2);
                Log.i(TAG, "emit 3 on thread " + Thread.currentThread().getName());
                emitter.onNext(3);
                Log.i(TAG, "emit 4 on thread " + Thread.currentThread().getName());
                emitter.onNext(4);
                Log.i(TAG, "emit onComplete on thread " + Thread.currentThread().getName());
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER);

        // 创建Subscriber
        Subscriber<Integer> downstream = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(final Subscription s) {
                Log.i(TAG, "Subscriber : onSubscribe on thread " + Thread.currentThread().getName());
                // 先拉取的3个事件，会按照发送1->接收1->发送2->接收2->发送3->接收3这样的顺序处理，上下游速度一致
                // 然后发送4，超出了下游拉取的范围，进入emitter的缓存区等待，然后产生onComplete事件，也进入缓存区等待，到此发送过程完成
                // 两秒后下游再次拉取事件，会收到onNext(事件4)和onComplete，这是从缓存区取出的
                s.request(3);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        s.request(2);
                    }
                }, 2000);
            }

            @Override
            public void onNext(Integer integer) {
                // 下游处理事件的卡顿会卡住上游的发送（如果是同一线程，中间没有observeOn操作符制造的缓存区的话）
//                try{
//                    Thread.sleep(1000);
//                }catch (Exception e){
//
//                }
                Log.i(TAG, "Subscriber : onNext = " + integer + " on thread " + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable t) {
                Log.i(TAG, "Subscriber : onError = " + t + " on thread " + Thread.currentThread().getName());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "Subscriber : onComplete" + " on thread " + Thread.currentThread().getName());
            }
        };

        upstream.
                subscribeOn(Schedulers.computation())
                .subscribe(downstream);
    }
}
