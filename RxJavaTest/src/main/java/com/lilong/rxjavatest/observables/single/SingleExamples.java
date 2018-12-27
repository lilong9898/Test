package com.lilong.rxjavatest.observables.single;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

import static com.lilong.rxjavatest.activity.MainActivity.TAG;
import static com.lilong.rxjavatest.observables.observable.ObservableExamples.EVENT_1;

/**
 * 测试{@link Single}类型的被观察者
 * 只发射一条单一的数据，或者一条异常通知，不能发射完成通知，其中数据与通知只能发射一个
 * 是{@link Observable}的简化版，只能处理单条事件
 * */
public class SingleExamples {

    private static Single single;

    /**
     * 返回一个{@link SingleOnSubscribe}接口实例当数据源的被观察者
     * */
    public static Single<String> getSingle() {
        if(single == null){
            single = Single.create(new SingleOnSubscribe<String>() {
                @Override
                public void subscribe(SingleEmitter emitter) throws Exception {
                    Log.i(TAG, "Single emits : " + EVENT_1);
                    // 发送一个普通事件
                    emitter.onSuccess(EVENT_1);
                    // 或者一个错误事件
//                    Log.i(TAG, "Single emits error");
//                    emitter.onError(new Throwable());
                }
            });
        }
        return single;
    }

}
