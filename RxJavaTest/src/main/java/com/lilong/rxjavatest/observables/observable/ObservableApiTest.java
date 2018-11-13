package com.lilong.rxjavatest.observables.observable;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static com.lilong.rxjavatest.activity.MainActivity.TAG;

/**
 * 测试Observable数据源的不同api方法
 */
public class ObservableApiTest {

    /**
     * {@link Observable#doOnSubscribe(Consumer)}
     * {@link Observable#doOnNext(Consumer)}
     * {@link Observable#doOnComplete(Action)}
     */
    public static void testObservableMethodDoOnXXX() {
        ObservableExamples.getObservableFromObservableOnSubscribe().doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                Log.i(TAG, "doOnSubscribe called, disposable = " + disposable);
            }
        }).doOnNext(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i(TAG, "doOnNext called, s = " + s);
            }
        }).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                Log.i(TAG, "doOnComplete called");
            }
        }).subscribe();
    }

}
