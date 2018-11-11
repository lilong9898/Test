package com.lilong.rxjavatest.observables.observable;

import android.os.Handler;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;

import static com.lilong.rxjavatest.activity.MainActivity.TAG;

/**
 * 被观察者其中的一类：Observable接口实现类
 * 被观察者一共有几类：
 * (1) {@link Observable} 实现类:发出0个或多个事件，最后发出完成或错误的信号
 * (2) {@link Flowable} 实现类: 发出0个或多个事件，最后发出完成或错误的信号，带流量控制
 * (3) {@link Single} 实现类: 发出1个事件或错误信号
 * (4) {@link Maybe} 实现类：在耗时操作结束后发出一个maybe事件，最后发出完成或错误的信号
 *      跟{@link #getObservableFromFutureTask()}有点像
 * (5) {@link Completable} 实现类：在耗时操作结束后发出完成或错误的信号（不发出任何事件）
 * */
public class ObservableExamples {

    public static final String EVENT_1 = "event_1";
    public static final String EVENT_2 = "event_2";
    public static final String EVENT_3 = "event_3";
    public static final String EVENT_4 = "event_4";
    public static final String EVENT_5 = "event_5";
    public static final String EVENT_6 = "event_6";
    public static final String EVENT_7 = "event_7";
    public static final String EVENT_8 = "event_8";
    public static final String EVENT_9 = "event_9";

    // 被观察者的数据源
    private static final List<String> EVENTS = Arrays.asList(new String[]{
            EVENT_1,
            EVENT_2,
    });

    //-------------------------------------------------------------------------------------------
    private static Observable<String> observableFromObservableOnSubscribe;

    /**
     * 返回一个将{@link ObservableOnSubscribe}接口实例作为数据源的被观察者
     * 每隔一秒发送一个消息
     */
    public static Observable<String> getObservableFromObservableOnSubscribe() {

        if (observableFromObservableOnSubscribe == null) {
            observableFromObservableOnSubscribe = Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(final ObservableEmitter<String> emitter) throws Exception {

                    // onSubscribe方法运行在调用被观察者subscribe方法的线程里
                    Handler handler = new Handler();
                    try {
                        long delay = 0;
                        for (final String str : EVENTS) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i(TAG, "observableFromObservableOnSubscribe emits : " + str);
                                    emitter.onNext(str);
                                }
                            }, delay);
                            delay = delay + 1000;
                        }
                        // 调用完emitter.onComplete后，再调emitter.onNext也无反应了，不会被接收了
                        // 调用完emitter.onError后也一样
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TAG, "observableFromObservableOnSubscribe emits : onComplete");
                                emitter.onComplete();
                            }
                        }, delay);
                    } catch (Exception e) {
                        emitter.onError(e);
                    }
                }
            });
        }
        return observableFromObservableOnSubscribe;
    }

    //-------------------------------------------------------------------------------------------
    private static Observable<String> observableJust;

    /**
     * 返回以其它任意容器为数据源的被观察者，subscribe方法调用后会立即发送事件，发送完成后自动调onComplete
     */
    public static Observable<String> getObservableJust() {
        if (observableJust == null) {
            observableJust = Observable.just("haha");
        }
        return observableJust;
    }

    //-------------------------------------------------------------------------------------------
    private static Observable<String> observableFromIterable;

    /**
     * 返回以iterable接口实现实例为数据源的被观察者，subscribe方法调用后会立即发送事件，发送完成后自动调onComplete
     */
    public static Observable<String> getObservableFromIterable() {
        if (observableFromIterable == null) {
            observableFromIterable = Observable.fromIterable(new Iterable<String>() {
                @Override
                public Iterator<String> iterator() {
                    HashSet<String> hashSet = new HashSet<String>();
                    hashSet.add(EVENT_4);
                    hashSet.add(EVENT_3);
                    return hashSet.iterator();
                }
            });
        }
        return observableFromIterable;
    }

    //-------------------------------------------------------------------------------------------
    private static Observable<String> observableFromArray;

    /**
     * 返回以数组为数据源的被观察者，subscribe方法调用后会立即发送事件，发送完成后自动调onComplete
     */
    public static Observable<String> getObservableFromArray() {
        if (observableFromArray == null) {
            observableFromArray = Observable.fromArray(new String[]{EVENT_5, EVENT_6});
        }
        return observableFromArray;
    }

    //-------------------------------------------------------------------------------------------
    private static Observable<String> observableFromCallable;

    /**
     * 返回以callable接口实例为数据源的被观察者，subscribe方法调用后会立即触发实例的call方法，然后其返回值被emit出来，最后自动调onComplete
     * 注意：call方法可能有耗时操作，执行完前整体流程处于阻塞状态
     */
    public static Observable<String> getObservableFromCallable() {
        if (observableFromCallable == null) {
            observableFromCallable = Observable.fromCallable(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    Log.i(TAG, "callable start executing....");
                    Thread.sleep(1000);
                    Log.i(TAG, "callable end executing....");
                    return EVENT_7;
                }
            });
        }
        return observableFromCallable;
    }

    //-------------------------------------------------------------------------------------------
    private static Observable<String> observableFromFuture;

    /**
     * 返回以Future接口实例为数据源的被观察者，subscribe方法调用后会立即调用实例的get方法，然后其返回值被emit出来，最后自动调onComplete
     * 注意：get方法可能有耗时操作，执行完前整体流程处于阻塞状态
     */
    public static Observable<String> getObservableFromFuture() {
        if (observableFromFuture == null) {
            observableFromFuture = Observable.fromFuture(new Future<String>() {
                @Override
                public boolean cancel(boolean mayInterruptIfRunning) {
                    return false;
                }

                @Override
                public boolean isCancelled() {
                    return false;
                }

                @Override
                public boolean isDone() {
                    return false;
                }

                @Override
                public String get() throws ExecutionException, InterruptedException {
                    return EVENT_8;
                }

                @Override
                public String get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
                    return null;
                }
            });
        }
        return observableFromFuture;
    }

    //-------------------------------------------------------------------------------------------
    private static Observable<String> observableFromFutureTask;

    /**
     * 属于observable from future情况的另一个例子，futureTask是Future接口的实现类，
     * 但get方法被实现成阻塞的，必须由线程池执行完futureTask后其get方法才能返回
     * <p>
     * 注意：因为get方法是阻塞的，而且在subscribe方法里会被调到，所以在subscribe之前必须将futureTask提交到线程池运行！
     * 否则会阻塞整个线程，程序无法继续，后续即使有提交futureTask的动作也无法执行了，因为线程整个阻塞了
     */
    private static FutureTask<String> futureTask = new FutureTask<String>(new Callable<String>() {
        @Override
        public String call() throws Exception {
            return EVENT_9;
        }
    });

    public static FutureTask<String> getFutureTask() {
        return futureTask;
    }

    public static Observable<String> getObservableFromFutureTask() {
        if (observableFromFutureTask == null) {
            observableFromFutureTask = Observable.fromFuture(futureTask);
        }
        return observableFromFutureTask;
    }

    //-------------------------------------------------------------------------------------------
    private static Observable<Long> observableInterval;

    /**
     * 返回一个以一定初始延时，一定时间间隔从0递增emit数字的被观察者，永不停止除非程序退出
     */
    public static Observable<Long> getObservableInterval() {
        if (observableInterval == null) {
            observableInterval = Observable.interval(1000, 1000, TimeUnit.MILLISECONDS);
        }
        return observableInterval;
    }

    //------------------------------------------------------------
    private static Observable showThreadObservable;

    /** 返回一个{@link ObservableOnSubscribe}接口实例当数据源的被观察者，并显示方法所在线程*/
    public static Observable getShowThreadObservable (){
       if(showThreadObservable == null){
           showThreadObservable = Observable.create(new ObservableOnSubscribe() {
               @Override
               public void subscribe(ObservableEmitter e) throws Exception {
                   Log.i(TAG, "observable method subscribe, is in thread = " + Thread.currentThread().getName());
                   Log.i(TAG, "observable emits : " + EVENT_1);
                   e.onNext(EVENT_1);
               }
           });
       }
       return showThreadObservable;
    }
}
