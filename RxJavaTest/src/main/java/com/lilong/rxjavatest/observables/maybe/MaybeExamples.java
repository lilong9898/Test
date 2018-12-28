package com.lilong.rxjavatest.observables.maybe;

import android.util.Log;

import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Observable;

import static com.lilong.rxjavatest.activity.MainActivity.TAG;
import static com.lilong.rxjavatest.observables.observable.ObservableExamples.EVENT_1;

/**
 * 测试{@link Maybe}类型的被观察者
 * * 只发射一条数据，然后发送一个完成事件或错误事件
 * * 是{@link Observable}的简化版，只能处理单条事件
 */
public class MaybeExamples {

    private static Maybe maybe;

    public static Maybe getMaybe(){
        if(maybe == null){
            maybe = Maybe.create(new MaybeOnSubscribe<String>() {
                @Override
                public void subscribe(MaybeEmitter<String> emitter) throws Exception {
                    Log.i(TAG, "Maybe emits " + EVENT_1);
                    emitter.onSuccess(EVENT_1);
                    Log.i(TAG, "Maybe emits onComplete");
                    emitter.onComplete();
//                    Log.i(TAG, "Maybe emits onError");
//                    emitter.onError(new Throwable());
                }
            });
        }
        return maybe;
    }

}
