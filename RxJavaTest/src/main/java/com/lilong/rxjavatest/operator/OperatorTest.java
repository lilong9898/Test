package com.lilong.rxjavatest.operator;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

import static com.lilong.rxjavatest.activity.MainActivity.TAG;

/**
 * RxJava中的操作符测试
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
}
