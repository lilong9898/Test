package com.lilong.rxjavatest.observables.completable;

import android.util.Log;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

import static com.lilong.rxjavatest.activity.MainActivity.TAG;

/**
 * 测试{@link Completable}类型的被观察者
 * */
public class CompletableTypeTest {

    public static void testCompletable(){
        CompletableExamples.getCompletable().subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "CompletableObserver onSubscribe");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "CompletableObserver onComplete");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "CompletableObserver onError");
            }
        });
    }
}
