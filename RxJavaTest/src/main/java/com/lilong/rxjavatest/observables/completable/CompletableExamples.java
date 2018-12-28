package com.lilong.rxjavatest.observables.completable;

import android.util.Log;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;

import static com.lilong.rxjavatest.activity.MainActivity.TAG;

/**
 * 测试{@link Completable}类型的被观察者
 * 只发射一条完成通知，或者一条异常通知，不能发射数据，其中完成通知与异常通知只能发射一个
 * 是{@link Observable}的简化版，只能处理单条事件
 * */
public class CompletableExamples {

    private static Completable completable;

    /**
     * 返回一个{@link CompletableOnSubscribe}接口实例当数据源的被观察者
     * */
    public static Completable getCompletable() {
        if(completable == null){
            completable = Completable.create(new CompletableOnSubscribe() {
                @Override
                public void subscribe(CompletableEmitter emitter) {
                    // 发送一个完成事件
                    Log.i(TAG, "Completable emits onComplete");
                    emitter.onComplete();
                    // 或发送一个错误事件
//                    Log.i(TAG, "Completable emits onError");
//                    emitter.onError(new Throwable());
                }
            });
        }
        return completable;
    }

}
