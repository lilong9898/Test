package com.lilong.rxjavatest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.lilong.rxjavatest.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class MainActivity extends Activity {

    public static final String TAG = "RxTag";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        // (2) 观察者是Consumer接口的实例
//        ObservableExamples.getObservableFullDefined().subscribe(ObserverExamples.getObserverConsumer());

        // (3) 观察者是Observer接口的实例
//        ObservableExamples.getObservableFullDefined().subscribe(ObserverExamples.getObserverFullDefined());

        // (4) 观察者是Subscribe接口的实例

    }


}
