package com.lilong.rxjavatest.activity;

import com.lilong.rxjavatest.R;
import com.lilong.rxjavatest.observables.observable.ObservableExamples;

import android.app.Activity;
import android.os.Bundle;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * 被观察者其中的一类：Observable接口实现类
 * 被观察者一共有几类：
 * (1) {@link Observable} 实现类:发出0个或多个事件，最后发出完成或错误的信号
 * (2) {@link Flowable} 实现类: 发出0个或多个事件，最后发出完成或错误的信号，带流量控制
 * (3) {@link Single} 实现类: 发出1个事件或错误信号
 * (4) {@link Maybe} 实现类：在耗时操作结束后发出一个maybe事件，最后发出完成或错误的信号
 * 跟{@link ObservableExamples#getObservableFromFutureTask()}有点像
 * (5) {@link Completable} 实现类：在耗时操作结束后发出完成或错误的信号（不发出任何事件）
 */
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
        //----------------测试不同种类的被观察者------------------------

        // (1)
//        ObservableTest.testObservableFromObservableOnSubscribe();

        // (2)
//        ObservableTest.testObservableJust();

        // (3)
//        ObservableTest.testObservableFromIterable();

        // (4)
//        ObservableTest.testObservableFromArray();

        // (5)
//        ObservableTest.testObservableFromCallable();

        // (6)
//        ObservableTest.testObservableFromFuture();

        // (7)
//        ObservableTest.testObservableFromFutureTask();

        // (8)
//        ObservableTest.testObservableInterval();

        //-------------------不同种类的观察者--------------------------
        // (1) 没有观察者，只有被观察者开始发送事件，并最终发送onComplete
//        ObservableExamples.getObservableFromObservableOnSubscribe().subscribe();

        // (2) 观察者是Consumer接口的实例，这种情况subscribe方法会返回disposable
        // 对比：观察者是Observer接口的实例时，会在其onSubscribe的参数里提供disposable，所以subscribe方法不需返回disposable，返回的是void
//        final Disposable disposable = ObservableExamples.getObservableFromObservableOnSubscribe().subscribe(ObserverExamples.getObserverConsumer());
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                disposable.dispose();
//            }
        // 延时500ms刚好是在Observable第一个event发出，而第二个event还没发出的时候dispose的
        // 所以第二个event的accept不会触发
//        }, 500);

        // (3) 在其它线程里订阅的情况：所有方法都运行在其它线程里
//        new Thread() {
//            @Override
//            public void run() {
//                ObservableExamples.getShowThreadObservable().subscribe(ObserverExamples.getShowThreadObserver());
//            }
//        }.start();

        // (4) 用subscribeWith方法订阅，与subscribe方法一样，只是多了个返回值就是observer本身
//        ObservableExamples.getObservableFromObservableOnSubscribe().subscribeWith(ObserverExamples.getObserverFullDefined());

        // (5) 用subscribeWith方法配合DisposableObserver，可直接用返回的DisposableObserver的dispose方法
//        final DisposableObserver<String> observer = ObservableExamples.getObservableFromObservableOnSubscribe().subscribeWith(ObserverExamples.getDisposableObserver());
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
        // 延时500ms刚好是在Observable第一个event发出，而第二个event还没发出的时候dispose的
        // 所以第二个event的onNext不会触发
//                observer.dispose();
//            }
//        }, 500);

        // (3) 观察者是Observer接口的实例
//        ObservableExamples.getObservableFromObservableOnSubscribe().subscribe(ObserverExamples.getObserverFullDefined());

    }
}
