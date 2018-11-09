package com.lilong.rxjavatest.util;

import android.util.Log;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import static com.lilong.rxjavatest.activity.MainActivity.TAG;

public class Util {

    private static final String EVENT_1 = "event_1";
    private static final String EVENT_2 = "event_2";

    // 被观察者的数据源
    private static final List<String> EVENTS = Arrays.asList(new String[]{
            EVENT_1,
            EVENT_2,
    });

    private static Observable<String> observableCustomClass;

    /**
     * 返回一个实现了{@link Observable}接口的被观察者
     */
    public static Observable<String> getObservableCustomClass() {

        if (observableCustomClass == null) {
            observableCustomClass = Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                    try {
                        for (String str : EVENTS) {
                            Log.i(TAG, "observableCustomClass emits : " + str);
                            emitter.onNext(str);
                        }
                        // 调用完emitter.onComplete后，再调emitter.onNext也无反应了，不会被接收了
                        // 调用完emitter.onError后也一样
                        Log.i(TAG, "observableCustomClass onComplete");
                        emitter.onComplete();
                    } catch (Exception e) {
                        emitter.onError(e);
                    }
                }
            });
        }
        return observableCustomClass;
    }
}
