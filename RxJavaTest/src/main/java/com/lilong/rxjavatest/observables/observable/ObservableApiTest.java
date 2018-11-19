package com.lilong.rxjavatest.observables.observable;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static com.lilong.rxjavatest.activity.MainActivity.TAG;

/**
 * 测试Observable数据源的不同api方法
 */
public class ObservableApiTest {

    private static final String EVENT_1 = "event_1";

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

    /**
     * {@link Observable#subscribe(Consumer, Consumer)}
     */
    public static void testObservableMethodSubscribeOnNextConsumerAndOnErrorConsumer() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                Log.i(TAG, "observable emits : " + EVENT_1);
                emitter.onNext(EVENT_1);
                Log.i(TAG, "observable emits onError");
                emitter.onError(new Throwable());
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i(TAG, "observer receives onNext: " + s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i(TAG, "observer receives onError");
            }
        });
    }
}
