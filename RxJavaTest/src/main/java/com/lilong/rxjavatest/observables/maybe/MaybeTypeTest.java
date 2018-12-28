package com.lilong.rxjavatest.observables.maybe;

import android.util.Log;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;

import static com.lilong.rxjavatest.activity.MainActivity.TAG;

/**
 * 测试{@link Maybe}类型的被观察者
 * */
public class MaybeTypeTest {

    public static void testMaybe(){
        MaybeExamples.getMaybe().subscribe(new MaybeObserver<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "MaybeObserver onSubscribe");
            }

            @Override
            public void onSuccess(String s) {
                Log.i(TAG, "MaybeObserver onSuccess = " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "MaybeObserver onError = " + e);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "MaybeObserver onComplete");
            }
        });
    }
}
