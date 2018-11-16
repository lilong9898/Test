package com.lilong.rxjavatest.operator;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;

import static com.lilong.rxjavatest.activity.MainActivity.TAG;

/** RxJava中的操作符测试*/
public class OperatorTest {

    private static final String EVENT_1 = "ab";
    private static final String EVENT_2 = "cd";

    /** 测试map操作符*/
    public static void testMapOperator(){

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                emitter.onNext(EVENT_1);
                Log.i(TAG, "observable emits : " + EVENT_1);
                emitter.onNext(EVENT_2);
                Log.i(TAG, "observable emits : " + EVENT_2);
            }
        }).map(new Function<String, Object>() {
            @Override
            public Object apply(String s) {
                return null;
            }
        });
    }
}
