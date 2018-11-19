package com.lilong.rxjavatest.operator;

import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import static com.lilong.rxjavatest.activity.MainActivity.TAG;

/**
 * RxJava中的操作符测试
 * 这里都是Observable的操作符（也就是Observable身上的一些可以转换自身形态的方法）
 * 所有Observable的操作符:
 * {@link Observable#all(Predicate)}
 * {@link Observable#ambWith(ObservableSource)}
 * {@link Observable#any(Predicate)}
 * {@link Observable#blockingFirst()}
 * {@link Observable#blockingFirst(Object)}
 * {@link Observable#blockingForEach(Consumer)}
 * {@link Observable#blockingIterable()}
 * {@link Observable#blockingIterable(int)}
 * {@link Observable#blockingLast()}
 * {@link Observable#blockingLast(Object)}
 * .................so many more
 */
public class OperatorTest {

    private static final String EVENT_1 = "event_1";
    private static final String EVENT_2 = "event_2";
    private static final String EVENT_3 = "event_3";

    /**
     * 测试map操作符
     * Observable发出的每个事件会通过map映射成另一个事件，这是个一对一的映射关系
     */
    public static void testMapOperator() {

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                Log.i(TAG, "observable emits : " + EVENT_1);
                emitter.onNext(EVENT_1);
                Log.i(TAG, "observable emits : " + EVENT_2);
                emitter.onNext(EVENT_2);
            }
        }).map(new Function<String, String>() {
            @Override
            public String apply(String s) {
                return s + "_suffix";
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "observer onNext : s = " + s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 测试flatMap操作符
     * Observable发出的每个事件，都映射成一个新Observable
     * 后续流程接收到的事件都是从新的Observable发出的
     * 因为一次flatMap操作就可以创造新一级Observable，相当于可以多处理一层嵌套的异步操作
     * 故多个flatMap操作可以实现多层嵌套的异步操作
     */
    public static void testFlatMapOperator() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                Log.i(TAG, "Observable emits : " + EVENT_1);
                emitter.onNext(EVENT_1);
                Log.i(TAG, "Observable emits : " + EVENT_2);
                emitter.onNext(EVENT_2);
            }
        }).flatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String s) throws Exception {
                ArrayList<String> newObservableDataSource = new ArrayList<String>();
                newObservableDataSource.add(s + "_1");
                newObservableDataSource.add(s + "_2");
                return Observable.fromIterable(newObservableDataSource);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i(TAG, "Observer receives : " + s);
            }
        });
    }

    /**
     * 测试flatMap操作符将原Observable发出的事件映射成乱序发射的新Observable的情况
     */
    public static void testFlapMapOperatorWithDifferentOrder() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                Log.i(TAG, "observable emits : " + EVENT_1);
                emitter.onNext(EVENT_1);
                Log.i(TAG, "observable emits : " + EVENT_2);
                emitter.onNext(EVENT_2);
                Log.i(TAG, "observable emits : " + EVENT_3);
                emitter.onNext(EVENT_3);
            }
        }).flatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String s) throws Exception {
                long delaySeconds = Long.parseLong(s.substring(6));
                // delaySeconds设置成越先来的事件反而越长
                delaySeconds = 3 - delaySeconds;
                return Observable.just(s).delay(delaySeconds, TimeUnit.SECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i(TAG, "observer receives : " + s);
            }
        });
    }

    /**
     * 测试concatMap操作符讲原Observable发出的事件映射成乱序发射的新Observable的情况
     * 使用concatMap，原Observable发出的事件会按照原顺序由映射到的新Observable发出
     */
    public static void testConcatMapOperatorWithDifferentOrder() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                Log.i(TAG, "observable emits : " + EVENT_1);
                emitter.onNext(EVENT_1);
                Log.i(TAG, "observable emits : " + EVENT_2);
                emitter.onNext(EVENT_2);
                Log.i(TAG, "observable emits : " + EVENT_3);
                emitter.onNext(EVENT_3);
            }
        }).concatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String s) throws Exception {
                long delaySeconds = Long.parseLong(s.substring(6));
                // delaySeconds设置成越先来的事件反而越长
                delaySeconds = 3 - delaySeconds;
                return Observable.just(s).delay(delaySeconds, TimeUnit.SECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i(TAG, "observer receives : " + s);
            }
        });
    }

    /**
     * 测试用flatMap实现嵌套的异步操作
     */
    public static void testNestedOperationsUsingFlatMapOperator() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                Log.i(TAG, "observable currentThread : " + Thread.currentThread());
                Log.i(TAG, "observable emits : " + EVENT_1);
                emitter.onNext(EVENT_1);
            }
        })
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(final String s) throws Exception {
                        Log.i(TAG, "async operation 1 receives : " + s + " , in thread : " + Thread.currentThread());
                        Observable<String> asyncOperationStep1 = Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(final ObservableEmitter<String> emitter) throws InterruptedException {
                                Log.i(TAG, "async operation 1 currentThread = " + Thread.currentThread());
                                Log.i(TAG, "async operation 1 starts");
                                Thread.sleep(1000);
                                Log.i(TAG, "async operation 1 completes");
                                Log.i(TAG, "async operation 1 onNext");
                                emitter.onNext(s + "_step1Processed");
                            }
                        });
                        return asyncOperationStep1;
                    }
                })
                .observeOn(Schedulers.computation())
                .flatMap(new Function<String, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(final String s) throws Exception {
                        Log.i(TAG, "async operation 2 receives : " + s + ", in thread " + Thread.currentThread());
                        Observable<String> asyncOperationStep2 = Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(final ObservableEmitter<String> emitter) throws InterruptedException {
                                Log.i(TAG, "async operation 2 currentThread = " + Thread.currentThread());
                                Log.i(TAG, "async operation 2 starts");
                                Thread.sleep(1000);
                                Log.i(TAG, "async operation 2 completes");
                                Log.i(TAG, "async operation 2 onNext");
                                emitter.onNext(s + "_step2Processed");
                            }
                        });
                        return asyncOperationStep2;
                    }
                })
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object s) {
                        Log.i(TAG, "observer receives : " + s);
                        Log.i(TAG, "observer currentThread = " + Thread.currentThread());
                    }
                });
    }

    /**
     * 测试zip操作符实现不同Observable事件的合并
     * observable1的每个事件会跟observable2的每个事件合并成一个新事件
     * 注意，observable1和observable2都在各自的线程上发送事件，而且各自的事件流有1秒的发送间隔
     * 这样才能保证线程调度时，observable1的某个序号的事件跟observable2的同序号的事件紧邻着发出并合并成新事件
     * 否则只有足够observable1和observable2的事件都能收集到1个时，才会触发合并的动作并将合并后的事件发给下游观察者
     */
    public static void testZipOperator() {

        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.i(TAG, "observable 1 currentThread " + Thread.currentThread());

                try {
                    Thread.sleep(1000);
                    Log.d(TAG, "observable1 emit 1");
                    emitter.onNext(1);

                    Thread.sleep(1000);
                    Log.d(TAG, "observable1 emit 2");
                    emitter.onNext(2);

                    Thread.sleep(1000);
                    Log.d(TAG, "observable1 emit 3");
                    emitter.onNext(3);

                    Thread.sleep(1000);
                    Log.d(TAG, "observable1 emit 4");
                    emitter.onNext(4);

                    Log.d(TAG, "emit complete1");
                    emitter.onComplete();
                } catch (Exception e) {
                }

            }
        }).subscribeOn(Schedulers.io());

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.i(TAG, "observable 2 currentThread " + Thread.currentThread());

                try {
                    Thread.sleep(1000);
                    Log.d(TAG, "observable2 emit a");
                    emitter.onNext("a");

                    Thread.sleep(1000);
                    Log.d(TAG, "observable2 emit b");
                    emitter.onNext("b");

                    Thread.sleep(1000);
                    Log.d(TAG, "observable2 emit c");
                    emitter.onNext("c");

                    // 如果两个observable的事件数量不一样多，则当较少的那个observable所有事件发送完毕后，
                    // 整个流程就结束了，zip操作符发出complete信号
                    // 因为合并要求两个observable都还有事件可发送
//                Thread.sleep(1000);
//                Log.d(TAG, "observable2 emit d");
//                emitter.onNext("d");

                    Log.d(TAG, "emit complete2");
                    emitter.onComplete();
                } catch (Exception e) {
                }

            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, Object>() {
            @Override
            public Object apply(Integer integer, String s) {
                return integer.toString() + s;
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        Log.i(TAG, "observer currentThread = " + Thread.currentThread());
                        Log.i(TAG, "observer onNext : " + o);
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "observer onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }


}
