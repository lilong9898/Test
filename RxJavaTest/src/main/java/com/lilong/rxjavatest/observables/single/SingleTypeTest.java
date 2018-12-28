package com.lilong.rxjavatest.observables.single;

import android.util.Log;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

import static com.lilong.rxjavatest.activity.MainActivity.TAG;

/**
 * 测试{@link Single}类型的被观察者
 * */
public class SingleTypeTest {

    public static void testSingle() {
        SingleExamples.getSingle().subscribe(new SingleObserver<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "SingleObserver onSubscribe");
            }

            @Override
            public void onSuccess(String s) {
                Log.i(TAG, "SingleObserver onSuccess = " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "SingleObserver onError = " + e);
            }
        });
    }

}
