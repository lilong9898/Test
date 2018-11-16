package com.lilong.rxjavatest.operator;

import android.util.Log;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import static com.lilong.rxjavatest.activity.MainActivity.TAG;

/**
 * RxJava中的操作符测试
 *这里都是Observable的操作符（也就是Observable身上的一些可以转换自身形态的方法）
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

    private static final String EVENT_1 = "ab";
    private static final String EVENT_2 = "cd";

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

}
