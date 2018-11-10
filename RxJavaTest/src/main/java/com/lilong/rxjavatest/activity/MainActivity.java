package com.lilong.rxjavatest.activity;

import com.lilong.rxjavatest.R;
import com.lilong.rxjavatest.examples.ObservableExamples;
import com.lilong.rxjavatest.examples.ObserverExamples;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import io.reactivex.disposables.Disposable;

public class MainActivity extends Activity {

    public static final String TAG = "RxTag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //----------------不同种类的被观察者------------------------
        // (1)
//        ObservableExamples.getObservableFullDefined().subscribe(ObserverExamples.getObserverFullDefined());
        // 重复调用observable的subscribe方法，会重新执行其subscribe方法的内容（这里是发送事件）
//        ObservableExamples.getObservableFullDefined().subscribe(ObserverExamples.getObserverFullDefined());

        // (2)
//        ObservableExamples.getObservableJust().subscribe(ObserverExamples.getObserverFullDefined());

        // (3)
//        ObservableExamples.getObservableFromIterable().subscribe(ObserverExamples.getObserverFullDefined());

        // (4)
//        ObservableExamples.getObservableFromArray().subscribe(ObserverExamples.getObserverFullDefined());

        // (5)
//        ObservableExamples.getObservableFromCallable().subscribe(ObserverExamples.getObserverFullDefined());

        // (6)
//        ObservableExamples.getObservableFromFuture().subscribe(ObserverExamples.getObserverFullDefined());

        // (7)
//        Executors.newScheduledThreadPool(5).schedule(ObservableExamples.getFutureTask(), 1500, TimeUnit.MILLISECONDS);
//        ObservableExamples.getObservableFromFutureTask().subscribe(ObserverExamples.getObserverFullDefined());

        // (8)
//        ObservableExamples.getObservableInterval().subscribe(new Observer<Long>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                Log.i(TAG, "long observer onSubscribe");
//            }
//
//            @Override
//            public void onNext(Long aLong) {
//                Log.i(TAG, "long observer onNext : " + aLong);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.i(TAG, "long observer onError");
//            }
//
//            @Override
//            public void onComplete() {
//                Log.i(TAG, "long observer onComplete");
//            }
//        });

        //-------------------不同种类的观察者--------------------------
        // (1) 没有观察者，只有被观察者开始发送事件，并最终发送onComplete
//        ObservableExamples.getObservableFullDefined().subscribe();

        // (2) 观察者是Consumer接口的实例，这种情况subscribe方法会返回disposable
        // 对比：观察者是Observer接口的实例时，会在其onSubscribe的参数里提供disposable，所以subscribe方法不需返回disposable，返回的是void
        final Disposable disposable = ObservableExamples.getObservableFullDefined().subscribe(ObserverExamples.getObserverConsumer());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                disposable.dispose();
            }
            // 延时500ms刚好是在Observable第一个event发出，而第二个event还没发出的时候dispose的
            // 所以第二个event的accept不会触发
        }, 500);

        // (3) 观察者是Observer接口的实例
//        ObservableExamples.getObservableFullDefined().subscribe(ObserverExamples.getObserverFullDefined());

    }
}
