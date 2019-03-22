package com.lilong.toucheventtest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * {@link ViewGroup#dispatchTouchEvent(MotionEvent)}的内部流程:
 * (1) 如果是DOWN事件, 重置所有状态(包括TouchTarget), 开启新的gesture, 不管之前有没有UP或CANCEL事件
 * (2) 拦截事件:
 *     (2.1) 如果ViewGroup的FLAG_DISALLOW_INTERCEPT被设置了, 则不拦截
 *     (2.2) 其余情况, 如果是Down事件, 则调{@link ViewGroup#onInterceptTouchEvent(MotionEvent)}拦截事件
 * (3) 如果事件未被拦截,
 *     (3.1) 是DOWN事件, 则向子布局分发:
 *           (3.1.1) 提取事件的坐标(是相对于本布局的坐标), 挨个遍历所有子布局
 *                   用isTransformedTouchPointInView(...)方法测试事件的坐标是否落在子布局中(涉及到同一位置在不同布局中相对坐标的转换)
 *                   最后找到可以接收事件的子布局
 *           (3.1.1) 调dispatchTransformedTouchEvent(...)方法向(3.1)中确定的子布局发送事件, 其内部会调用子布局的{@link ViewGroup#dispatchTouchEvent(MotionEvent)}方法
 *                   如果返回true, 说明子布局消费了事件, 调addTouchTarget将其标记为touchTarget
 *     (3.2) 如果事件不是DOWN事件, 会直接调dispatchTransformedTouchEvent(...)将事件发送给之前DOWN事件分发时被标记为touchTarget的view上
 *           这就实现了MOVE/UP事件根据DOWN事件的分发结果, 直接分发到合适的子view
 * */
public class MainActivity extends Activity {

    public static final String TAG = "TTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, "Activity, dispatchTouchEvent : " + verbalize(ev));
        boolean result  = super.dispatchTouchEvent(ev);
        Log.i(TAG, "Activity, dispatchTouchEvent : return " + result);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "Activity, onTouchEvent : " + verbalize(event));
        boolean result = super.onTouchEvent(event);
        Log.i(TAG, "Activity, onTouchEvent : return " + result);
        return result;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String verbalize(MotionEvent ev){

        if(ev == null){
            return "";
        }

        return MotionEvent.actionToString(ev.getAction());
    }
}
