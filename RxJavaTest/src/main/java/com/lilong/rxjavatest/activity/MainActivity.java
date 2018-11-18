package com.lilong.rxjavatest.activity;

import com.lilong.rxjavatest.R;
import com.lilong.rxjavatest.observables.observable.ObservableExamples;
import com.lilong.rxjavatest.operator.OperatorTest;

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
//        ObservableTypeTest.testObservableFromObservableOnSubscribe();

        // (2)
//        ObservableTypeTest.testObservableJust();

        // (3)
//        ObservableTypeTest.testObservableFromIterable();

        // (4)
//        ObservableTypeTest.testObservableFromArray();

        // (5)
//        ObservableTypeTest.testObservableFromCallable();

        // (6)
//        ObservableTypeTest.testObservableFromFuture();

        // (7)
//        ObservableTypeTest.testObservableFromFutureTask();

        // (8)
//        ObservableTypeTest.testObservableInterval();

        //-------------------测试被观察者的不同方法------------------------
//        ObservableApiTest.testObservableMethodDoOnXXX();

        //-------------------测试不同种类的观察者--------------------------
        // (1)
//        ObserverTest.testNoObserver();

        // (2)
//        ObserverTest.testObserverFromConsumer();

        // (3)
//        ObserverTest.testWholeProcedureInAnotherThread();

        // (4)
//        ObserverTest.testSubscribeWithMethod();

        // (5)
//        ObserverTest.testDisposableObserver();

        // (6)
//        ObserverTest.testObserverFromObserver();

        //-----------------测试被观察者和观察者不在同个线程的情况----------------------
//        MultiThreadTest.testObservableAndObserverInDifferentThread();
//        MultiThreadTest.testObservableOnIoThreadAndObserverOnMainThread();

        //-----------------操作符测试----------------------------------------------
//        OperatorTest.testMapOperator();
//        OperatorTest.testFlatMapOperator();
//        OperatorTest.testFlapMapOperatorWithDifferentOrder();
//        OperatorTest.testConcatMapOperatorWithDifferentOrder();
        OperatorTest.testNestedOperationsUsingFlatMapOperator();
    }
}
